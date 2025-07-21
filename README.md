import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.github.jsqlparser.JSQLParserException;
import com.github.jsqlparser.parser.CCJSqlParserUtil;
import com.github.jsqlparser.statement.Statement;
import com.github.jsqlparser.statement.delete.Delete;
import com.github.jsqlparser.statement.insert.Insert;
import com.github.jsqlparser.statement.select.*;
import com.github.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Intercepts({
    @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
    )
})
public class TenantIsolationInterceptor implements Interceptor {

    // 租户ID字段名（默认tenant_id）
    private String tenantIdColumn = "tenant_id";
    
    // 需要排除的表（系统表）
    private final Set<String> excludedTables = ConcurrentHashMap.newKeySet();
    
    // 需要排除的Mapper ID
    private final Set<String> excludedMappers = ConcurrentHashMap.newKeySet();
    
    // SQL解析缓存（提高性能）
    private final Map<String, String> sqlCache = new ConcurrentHashMap<>();
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        
        // 获取MappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String mapperId = mappedStatement.getId();
        
        // 检查是否排除当前Mapper
        if (excludedMappers.contains(mapperId)) {
            return invocation.proceed();
        }
        
        // 获取原始SQL
        BoundSql boundSql = handler.getBoundSql();
        String originalSql = boundSql.getSql();
        
        // 获取当前租户ID
        String tenantId = TenantContext.getCurrentTenant();
        
        // 租户ID为空时不处理
        if (tenantId == null || tenantId.isEmpty()) {
            return invocation.proceed();
        }
        
        // 尝试从缓存获取处理后的SQL
        String cacheKey = originalSql + "|" + tenantId;
        String rewrittenSql = sqlCache.get(cacheKey);
        
        if (rewrittenSql == null) {
            try {
                // 解析并重写SQL
                Statement stmt = CCJSqlParserUtil.parse(originalSql);
                TenantSqlVisitor visitor = new TenantSqlVisitor(tenantId);
                stmt.accept(visitor);
                rewrittenSql = stmt.toString();
                
                // 缓存处理结果
                sqlCache.put(cacheKey, rewrittenSql);
            } catch (JSQLParserException e) {
                // 解析失败时使用原始SQL
                rewrittenSql = originalSql;
            }
        }
        
        // 替换原始SQL
        metaObject.setValue("delegate.boundSql.sql", rewrittenSql);
        
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从配置加载属性
        if (properties.containsKey("tenantIdColumn")) {
            this.tenantIdColumn = properties.getProperty("tenantIdColumn");
        }
        
        if (properties.containsKey("excludedTables")) {
            String tables = properties.getProperty("excludedTables");
            if (StringUtils.hasText(tables)) {
                Arrays.stream(tables.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(excludedTables::add);
            }
        }
        
        if (properties.containsKey("excludedMappers")) {
            String mappers = properties.getProperty("excludedMappers");
            if (StringUtils.hasText(mappers)) {
                Arrays.stream(mappers.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(excludedMappers::add);
            }
        }
    }
    
    // 判断是否排除当前表
    private boolean isExcludedTable(String tableName) {
        return excludedTables.contains(tableName.toLowerCase());
    }
    
    /**
     * SQL访问器，用于添加租户条件
     */
    private class TenantSqlVisitor extends BaseSqlVisitor {
        private final String tenantId;
        
        public TenantSqlVisitor(String tenantId) {
            this.tenantId = tenantId;
        }
        
        @Override
        public void visit(Select select) {
            SelectBody selectBody = select.getSelectBody();
            if (selectBody != null) {
                selectBody.accept(new TenantSelectVisitor(tenantId));
            }
        }
        
        @Override
        public void visit(Update update) {
            String tableName = update.getTable().getName();
            if (isExcludedTable(tableName)) return;
            
            // 添加租户条件
            update.getWhere().ifPresentOrElse(
                where -> where.setExpression(
                    new AndExpression(where.getExpression(), createTenantCondition(tableName))
                ),
                () -> update.setWhere(new Where(createTenantCondition(tableName)))
            );
        }
        
        @Override
        public void visit(Delete delete) {
            String tableName = delete.getTable().getName();
            if (isExcludedTable(tableName)) return;
            
            // 添加租户条件
            delete.getWhere().ifPresentOrElse(
                where -> where.setExpression(
                    new AndExpression(where.getExpression(), createTenantCondition(tableName))
                ),
                () -> delete.setWhere(new Where(createTenantCondition(tableName)))
            );
        }
        
        @Override
        public void visit(Insert insert) {
            String tableName = insert.getTable().getName();
            if (isExcludedTable(tableName)) return;
            
            // 检查是否已包含租户ID列
            boolean hasTenantColumn = insert.getColumns().stream()
                .anyMatch(column -> tenantIdColumn.equalsIgnoreCase(column.getColumnName()));
            
            // 添加租户ID列和值
            if (!hasTenantColumn) {
                insert.getColumns().add(new Column(tenantIdColumn));
                
                if (insert.getItemsList() instanceof ExpressionList) {
                    ExpressionList itemsList = (ExpressionList) insert.getItemsList();
                    itemsList.getExpressions().add(new StringValue(tenantId));
                } else if (insert.getItemsList() instanceof MultiExpressionList) {
                    MultiExpressionList multiList = (MultiExpressionList) insert.getItemsList();
                    for (ExpressionList exprList : multiList.getExpressionLists()) {
                        exprList.getExpressions().add(new StringValue(tenantId));
                    }
                }
            }
        }
        
        private Expression createTenantCondition(String tableName) {
            return new EqualsTo(
                new Column(tableName + "." + tenantIdColumn),
                new StringValue(tenantId)
            );
        }
    }
    
    /**
     * SELECT访问器，用于处理复杂查询
     */
    private class TenantSelectVisitor extends SelectVisitorAdapter {
        private final String tenantId;
        
        public TenantSelectVisitor(String tenantId) {
            this.tenantId = tenantId;
        }
        
        @Override
        public void visit(PlainSelect plainSelect) {
            // 处理主表
            FromItem fromItem = plainSelect.getFromItem();
            handleFromItem(fromItem, plainSelect);
            
            // 处理JOIN表
            if (plainSelect.getJoins() != null) {
                for (Join join : plainSelect.getJoins()) {
                    handleFromItem(join.getRightItem(), plainSelect);
                }
            }
        }
        
        @Override
        public void visit(SetOperationList setOpList) {
            for (SelectBody selectBody : setOpList.getSelects()) {
                selectBody.accept(this);
            }
        }
        
        private void handleFromItem(FromItem fromItem, PlainSelect plainSelect) {
            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                String tableName = table.getName();
                
                if (!isExcludedTable(tableName)) {
                    addTenantCondition(plainSelect, tableName);
                }
            } else if (fromItem instanceof SubSelect) {
                // 处理子查询
                SubSelect subSelect = (SubSelect) fromItem;
                subSelect.getSelectBody().accept(this);
            }
        }
        
        private void addTenantCondition(PlainSelect plainSelect, String tableName) {
            Expression tenantCondition = new EqualsTo(
                new Column(tableName + "." + tenantIdColumn),
                new StringValue(tenantId)
            );
            
            Optional<Expression> where = plainSelect.getWhere();
            if (where.isPresent()) {
                plainSelect.setWhere(new AndExpression(where.get(), tenantCondition));
            } else {
                plainSelect.setWhere(new Where(tenantCondition));
            }
        }
    }
    
    /**
     * 基础SQL访问器
     */
    private abstract static class BaseSqlVisitor implements StatementVisitor {
        @Override
        public void visit(Select select) {}
        
        @Override
        public void visit(Update update) {}
        
        @Override
        public void visit(Delete delete) {}
        
        @Override
        public void visit(Insert insert) {}
    }
}
