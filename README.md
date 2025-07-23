import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Intercepts({
    @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
    )
})
public class EnhancedProductInterceptor implements Interceptor {
    
    // 配置属性
    private String productIdColumn = "product_id";
    private final Set<String> excludedTables = ConcurrentHashMap.newKeySet();
    private final Set<String> excludedMappers = ConcurrentHashMap.newKeySet();
    private final Set<String> targetTables = ConcurrentHashMap.newKeySet();
    
    // SQL解析缓存
    private final Map<String, String> sqlCache = new ConcurrentHashMap<>();
    
    // 获取当前产品ID列表（从上下文）
    private List<Long> getCurrentProductIds() {
        // 实际应用中从ThreadLocal/RequestContext获取
        return Arrays.asList(1001L, 1002L, 1003L); // 示例数据
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        MetaObject metaHandler = SystemMetaObject.forObject(handler);
        
        // 获取MappedStatement
        MappedStatement ms = (MappedStatement) metaHandler.getValue("delegate.mappedStatement");
        String mapperId = ms.getId();
        
        // 检查是否排除当前Mapper
        if (excludedMappers.contains(mapperId)) {
            return invocation.proceed();
        }
        
        // 获取原始SQL
        BoundSql boundSql = handler.getBoundSql();
        String originalSql = boundSql.getSql();
        List<Long> productIds = getCurrentProductIds();
        
        // 产品ID为空时不处理
        if (productIds == null || productIds.isEmpty()) {
            return invocation.proceed();
        }
        
        // 生成缓存键
        String cacheKey = originalSql + "|" + productIds.hashCode();
        String rewrittenSql = sqlCache.get(cacheKey);
        
        if (rewrittenSql == null) {
            try {
                Statement stmt = CCJSqlParserUtil.parse(originalSql);
                if (stmt instanceof Select) {
                    Select select = (Select) stmt;
                    SelectBody selectBody = select.getSelectBody();
                    
                    if (selectBody instanceof PlainSelect) {
                        PlainSelect plainSelect = (PlainSelect) selectBody;
                        
                        // 获取所有涉及的表
                        Set<String> tables = extractTables(plainSelect);
                        
                        // 检查是否需要处理（有目标表且无排除表）
                        if (!Collections.disjoint(tables, targetTables) && 
                            Collections.disjoint(tables, excludedTables)) {
                            
                            // 添加产品ID条件
                            addProductIdCondition(plainSelect, productIds);
                        }
                    }
                }
                
                rewrittenSql = stmt.toString();
                sqlCache.put(cacheKey, rewrittenSql);
            } catch (JSQLParserException e) {
                // 解析失败时使用原始SQL
                rewrittenSql = originalSql;
            }
        }
        
        // 替换原始SQL
        metaHandler.setValue("delegate.boundSql.sql", rewrittenSql);
        
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从配置加载属性
        if (properties.containsKey("productIdColumn")) {
            this.productIdColumn = properties.getProperty("productIdColumn");
        }
        
        loadSetProperty(properties, "targetTables", targetTables);
        loadSetProperty(properties, "excludedTables", excludedTables);
        loadSetProperty(properties, "excludedMappers", excludedMappers);
    }
    
    private void loadSetProperty(Properties properties, String key, Set<String> set) {
        String value = properties.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(set::add);
        }
    }
    
    /**
     * 提取SQL中所有涉及的表名
     */
    private Set<String> extractTables(PlainSelect plainSelect) {
        Set<String> tables = new HashSet<>();
        
        // 主表
        extractTableFromItem(plainSelect.getFromItem(), tables);
        
        // JOIN表
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                extractTableFromItem(join.getRightItem(), tables);
            }
        }
        
        return tables;
    }
    
    private void extractTableFromItem(FromItem fromItem, Set<String> tables) {
        if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            tables.add(table.getName().toLowerCase());
        } else if (fromItem instanceof SubSelect) {
            // 递归处理子查询
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() instanceof PlainSelect) {
                tables.addAll(extractTables((PlainSelect) subSelect.getSelectBody()));
            }
        }
    }
    
    /**
     * 添加产品ID条件
     */
    private void addProductIdCondition(PlainSelect plainSelect, List<Long> productIds) {
        // 构建IN表达式：product_id IN (1001,1002,...)
        Column productIdColumn = new Column(this.productIdColumn);
        InExpression inExpr = new InExpression();
        inExpr.setLeftExpression(productIdColumn);
        inExpr.setRightItemsList(
            new ExpressionList(
                productIds.stream()
                    .map(LongValue::new)
                    .collect(Collectors.toList())
            )
        );
        
        // 添加到WHERE条件
        Expression where = plainSelect.getWhere();
        if (where == null) {
            plainSelect.setWhere(inExpr);
        } else {
            plainSelect.setWhere(new AndExpression(where, inExpr));
        }
    }
    
    /**
     * 清理缓存（可定期调用）
     */
    public void clearCache() {
        sqlCache.clear();
    }
}
