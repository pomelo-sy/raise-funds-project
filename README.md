import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户数据隔离拦截器(MyBatis-Plus InnerInterceptor实现)
 */
public class TenantInnerInterceptor implements InnerInterceptor {

    // 缓存解析后的SQL语句
    private final Map<String, Statement> statementCache = new ConcurrentHashMap<>();
    // 缓存表名
    private final Map<String, Set<String>> tablesCache = new ConcurrentHashMap<>();
    
    // 排除的表名(不进行租户过滤)
    private Set<String> excludedTables = new HashSet<>();
    // 租户字段名(默认product_id)
    private String tenantColumn = "product_id";
    // 是否启用租户过滤(默认启用)
    private boolean enabled = true;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, 
                          RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (!enabled) {
            return;
        }
        
        // 获取租户ID列表(需要自行实现)
        List<String> tenantIds = TenantContext.getTenantIds();
        if (tenantIds == null || tenantIds.isEmpty()) {
            return;
        }
        
        // 处理原始SQL
        String originalSql = boundSql.getSql();
        String cacheKey = originalSql + "|" + String.join(",", tenantIds);
        
        try {
            // 从缓存获取或解析SQL
            Statement statement = statementCache.computeIfAbsent(cacheKey, 
                key -> parseSql(originalSql));
            
            if (statement instanceof Select) {
                Select select = (Select) statement;
                processSelectStatement(select, tenantIds);
                
                // 修改BoundSql中的SQL
                resetBoundSql(boundSql, select.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Tenant filter failed", e);
        }
    }

    private Statement parseSql(String sql) {
        try {
            return CCJSqlParserUtil.parse(sql);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SQL", e);
        }
    }

    private void processSelectStatement(Select select, List<String> tenantIds) {
        SelectBody selectBody = select.getSelectBody();
        
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody, tenantIds);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() instanceof PlainSelect) {
                processPlainSelect((PlainSelect) withItem.getSelectBody(), tenantIds);
            }
        }
    }

    private void processPlainSelect(PlainSelect plainSelect, List<String> tenantIds) {
        // 获取查询中的所有表名
        Set<String> tables = tablesCache.computeIfAbsent(
            plainSelect.toString(), 
            key -> new TablesNamesFinder().getTableList(plainSelect));
        
        // 如果所有表都被排除，则不处理
        if (tables.isEmpty() || tables.stream().allMatch(this::isTableExcluded)) {
            return;
        }
        
        // 创建租户IN条件
        ItemsList itemsList = new net.sf.jsqlparser.expression.operators.relational.ExpressionList(
            tenantIds.stream()
                .map(id -> new LongValue(Long.parseLong(id)))
                .toArray(Expression[]::new)
        );
        
        Column tenantColumn = new Column(this.tenantColumn);
        InExpression inExpression = new InExpression(tenantColumn, itemsList);
        
        // 处理JOIN条件
        processJoins(plainSelect.getJoins());
        
        // 处理WHERE条件
        if (tables.stream().anyMatch(t -> !isTableExcluded(t))) {
            Expression where = plainSelect.getWhere();
            plainSelect.setWhere(where == null ? inExpression : new AndExpression(where, inExpression));
        }
    }

    private void processJoins(List<Join> joins) {
        if (joins == null) return;
        
        for (Join join : joins) {
            if (join.isLeft() || join.isRight() || join.isFull()) {
                Table joinTable = extractTableFromJoin(join.getRightItem());
                if (joinTable != null && !isTableExcluded(joinTable.getName())) {
                    Expression onExpression = join.getOnExpression();
                    if (onExpression != null) {
                        Column joinColumn = new Column(joinTable.getName() + "." + tenantColumn);
                        join.setOnExpression(new AndExpression(onExpression, 
                            new EqualsTo(joinColumn, new Column(tenantColumn))));
                    }
                }
            }
        }
    }

    private Table extractTableFromJoin(FromItem item) {
        if (item instanceof Table) {
            return (Table) item;
        } else if (item instanceof SubJoin) {
            return extractTableFromJoin(((SubJoin) item).getLeft());
        }
        return null;
    }

    private boolean isTableExcluded(String tableName) {
        return excludedTables.contains(tableName.toLowerCase()) || 
               excludedTables.contains(tableName.toUpperCase());
    }

    private void resetBoundSql(BoundSql boundSql, String newSql) {
        try {
            java.lang.reflect.Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
        } catch (Exception e) {
            throw new RuntimeException("Failed to modify bound SQL", e);
        }
    }

    // ========== 配置方法 ==========
    
    public void setExcludedTables(Set<String> excludedTables) {
        this.excludedTables = excludedTables;
    }

    public void addExcludedTable(String tableName) {
        this.excludedTables.add(tableName);
    }

    public void setTenantColumn(String tenantColumn) {
        this.tenantColumn = tenantColumn;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
