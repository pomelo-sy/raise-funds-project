import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class TenantIsolationInterceptor implements Interceptor {

    // Cache for parsed SQL statements
    private final Map<String, Statement> statementCache = new ConcurrentHashMap<>();
    
    // Cache for tables in statements
    private final Map<String, Set<String>> tablesCache = new ConcurrentHashMap<>();
    
    // List of tables to exclude from tenant filtering
    private Set<String> excludedTables = new HashSet<>();
    
    // List of mapper IDs to exclude from tenant filtering
    private Set<String> excludedMappers = new HashSet<>();
    
    // Column name to use for tenant isolation (default is product_id)
    private String tenantColumnName = "product_id";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        
        // Skip if mapper is in excluded list
        if (excludedMappers.contains(ms.getId())) {
            return invocation.proceed();
        }

        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
        } else {
            boundSql = (BoundSql) args[5];
        }

        // Get tenant IDs from context (you need to implement this)
        List<String> tenantIds = getTenantIdsFromContext();
        if (tenantIds == null || tenantIds.isEmpty()) {
            return invocation.proceed();
        }

        String originalSql = boundSql.getSql();
        String cacheKey = originalSql + "|" + String.join(",", tenantIds);
        
        // Get or parse statement from cache
        Statement statement = statementCache.computeIfAbsent(cacheKey, key -> {
            try {
                return CCJSqlParserUtil.parse(originalSql);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse SQL", e);
            }
        });

        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody selectBody = select.getSelectBody();
            
            if (selectBody instanceof PlainSelect) {
                processPlainSelect((PlainSelect) selectBody, tenantIds, ms.getId());
            } else if (selectBody instanceof WithItem) {
                // Handle WITH clauses if needed
                WithItem withItem = (WithItem) selectBody;
                if (withItem.getSelectBody() instanceof PlainSelect) {
                    processPlainSelect((PlainSelect) withItem.getSelectBody(), tenantIds, ms.getId());
                }
            }
            
            // Update the bound SQL with modified statement
            resetBoundSql(boundSql, statement.toString(), ms);
        }

        return invocation.proceed();
    }

    private void processPlainSelect(PlainSelect plainSelect, List<String> tenantIds, String mapperId) {
        // Get all tables in the query
        Set<String> tables = tablesCache.computeIfAbsent(plainSelect.toString(), 
            key -> new TablesNamesFinder().getTableList(plainSelect));
        
        // Skip if no tables or all tables are excluded
        if (tables.isEmpty() || tables.stream().allMatch(this::isTableExcluded)) {
            return;
        }

        // Create IN expression for tenant IDs
        ItemsList itemsList = new net.sf.jsqlparser.expression.operators.relational.ExpressionList(
            tenantIds.stream()
                .map(id -> new LongValue(Long.parseLong(id)))
                .toArray(Expression[]::new)
        );
        
        Column tenantColumn = new Column(tenantColumnName);
        InExpression inExpression = new InExpression(tenantColumn, itemsList);

        // Get the WHERE clause
        Expression where = plainSelect.getWhere();
        
        // Handle LEFT JOIN tables - add tenant filter to ON clauses
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                if (join.isLeft() || join.isRight() || join.isFull()) {
                    Table joinTable = join.getRightItem() instanceof Table ? 
                        (Table) join.getRightItem() : 
                        extractTableFromJoin(join.getRightItem());
                        
                    if (joinTable != null && !isTableExcluded(joinTable.getName())) {
                        Expression onExpression = join.getOnExpression();
                        if (onExpression != null) {
                            // Add tenant filter to ON clause
                            join.setOnExpression(new AndExpression(onExpression, 
                                new EqualsTo(new Column(joinTable.getName() + "." + tenantColumnName), 
                                    tenantColumn)));
                        }
                    }
                }
            }
        }

        // Apply to main WHERE clause if not all tables are excluded
        if (tables.stream().anyMatch(t -> !isTableExcluded(t))) {
            if (where == null) {
                plainSelect.setWhere(inExpression);
            } else {
                plainSelect.setWhere(new AndExpression(where, inExpression));
            }
        }
    }

    private Table extractTableFromJoin(FromItem item) {
        if (item instanceof Table) {
            return (Table) item;
        } else if (item instanceof SubJoin) {
            return extractTableFromJoin(((SubJoin) item).getLeft());
        } else if (item instanceof SubSelect) {
            // For subselects in joins, we might need more complex handling
            return null;
        }
        return null;
    }

    private boolean isTableExcluded(String tableName) {
        return excludedTables.contains(tableName.toLowerCase()) || 
               excludedTables.contains(tableName.toUpperCase());
    }

    private void resetBoundSql(BoundSql boundSql, String newSql, MappedStatement ms) {
        try {
            // Use reflection to modify the bound SQL
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
            
            // Clear any cached statements in the mapped statement
            if (ms.getConfiguration().getEnvironment().getCache(ms.getId()) != null) {
                ms.getConfiguration().getEnvironment().getCache(ms.getId()).clear();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to modify bound SQL", e);
        }
    }

    // Implement this method to get tenant IDs from your context
    private List<String> getTenantIdsFromContext() {
        // Your implementation here
        return Collections.emptyList();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // Initialize excluded tables and mappers from properties
        String excludedTablesStr = properties.getProperty("excludedTables");
        if (excludedTablesStr != null) {
            Collections.addAll(excludedTables, excludedTablesStr.split(","));
        }
        
        String excludedMappersStr = properties.getProperty("excludedMappers");
        if (excludedMappersStr != null) {
            Collections.addAll(excludedMappers, excludedMappersStr.split(","));
        }
        
        String columnName = properties.getProperty("tenantColumnName");
        if (columnName != null) {
            this.tenantColumnName = columnName;
        }
    }
}
