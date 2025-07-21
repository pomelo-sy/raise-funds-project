import com.github.jsqlparser.JSQLParserException;
import com.github.jsqlparser.parser.CCJSqlParserUtil;
import com.github.jsqlparser.statement.Statement;
import com.github.jsqlparser.statement.delete.Delete;
import com.github.jsqlparser.statement.insert.Insert;
import com.github.jsqlparser.statement.select.*;
import com.github.jsqlparser.statement.update.Update;
import com.github.jsqlparser.statement.StatementVisitorAdapter;
import com.github.jsqlparser.expression.Expression;
import com.github.jsqlparser.expression.StringValue;
import com.github.jsqlparser.expression.operators.relational.EqualsTo;
import com.github.jsqlparser.schema.Column;
import com.github.jsqlparser.schema.Table;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class TenantInterceptor implements Interceptor {

    // 租户ID字段名（默认为tenant_id）
    private String tenantIdColumn = "tenant_id";
    
    // 需要排除的表（系统表）
    private List<String> excludedTables = new ArrayList<>();
    
    // 需要排除的Mapper ID
    private List<String> excludedMappers = new ArrayList<>();
    
    // 是否启用租户过滤（默认启用）
    private boolean enabled = true;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!enabled) {
            return invocation.proceed();
        }
        
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        
        // 检查是否排除当前Mapper
        if (isExcludedMapper(ms.getId())) {
            return invocation.proceed();
        }
        
        // 获取原始SQL
        BoundSql boundSql;
        if (args.length == 6) {
            // 6个参数的情况
            boundSql = (BoundSql) args[5];
        } else {
            // 4个参数的情况
            boundSql = ms.getBoundSql(parameter);
        }
        
        String originalSql = boundSql.getSql();
        String tenantId = TenantContext.getCurrentTenant();
        
        // 租户ID为空时不处理
        if (tenantId == null || tenantId.isEmpty()) {
            return invocation.proceed();
        }
        
        try {
            // 解析并重写SQL
            Statement stmt = CCJSqlParserUtil.parse(originalSql);
            TenantStatementVisitor visitor = new TenantStatementVisitor(tenantId);
            stmt.accept(visitor);
            
            // 获取重写后的SQL
            String rewrittenSql = stmt.toString();
            
            // 创建新的BoundSql
            BoundSql newBoundSql = new BoundSql(
                ms.getConfiguration(), 
                rewrittenSql, 
                boundSql.getParameterMappings(), 
                boundSql.getParameterObject()
            );
            
            // 替换BoundSql
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", rewrittenSql);
            
            // 如果参数中包含租户ID，需要设置参数值
            for (ParameterMapping mapping : boundSql.getParameterMappings()) {
                String property = mapping.getProperty();
                if (property != null && property.contains(tenantIdColumn)) {
                    metaObject.setValue("additionalParameters." + property, tenantId);
                }
            }
            
        } catch (JSQLParserException e) {
            // 解析失败时使用原始SQL
            System.err.println("SQL解析失败, 使用原始SQL: " + e.getMessage());
        }
        
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
            if (tables != null && !tables.isEmpty()) {
                for (String table : tables.split(",")) {
                    excludedTables.add(table.trim().toLowerCase());
                }
            }
        }
        
        if (properties.containsKey("excludedMappers")) {
            String mappers = properties.getProperty("excludedMappers");
            if (mappers != null && !mappers.isEmpty()) {
                for (String mapper : mappers.split(",")) {
                    excludedMappers.add(mapper.trim());
                }
            }
        }
        
        if (properties.containsKey("enabled")) {
            this.enabled = Boolean.parseBoolean(properties.getProperty("enabled"));
        }
    }
    
    // 判断是否排除当前Mapper
    private boolean isExcludedMapper(String mapperId) {
        for (String excluded : excludedMappers) {
            if (mapperId.contains(excluded)) {
                return true;
            }
        }
        return false;
    }
    
    // 判断是否排除当前表
    private boolean isExcludedTable(Table table) {
        String tableName = table.getName().toLowerCase();
        if (excludedTables.contains(tableName)) {
            return true;
        }
        
        // 处理带模式名的表 (如 schema.table)
        int dotIndex = tableName.indexOf('.');
        if (dotIndex > 0) {
            return excludedTables.contains(tableName.substring(dotIndex + 1));
        }
        
        return false;
    }

    /**
     * 自定义SQL语句访问器，用于添加租户条件
     */
    private class TenantStatementVisitor extends StatementVisitorAdapter {
        
        private final String tenantId;
        
        public TenantStatementVisitor(String tenantId) {
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
            Table table = update.getTable();
            if (isExcludedTable(table)) return;
            
            Expression where = update.getWhere();
            Expression tenantCondition = createTenantCondition(table);
            
            if (where == null) {
                update.setWhere(tenantCondition);
            } else {
                update.setWhere(new AndExpression(where, tenantCondition));
            }
        }
        
        @Override
        public void visit(Delete delete) {
            Table table = delete.getTable();
            if (isExcludedTable(table)) return;
            
            Expression where = delete.getWhere();
            Expression tenantCondition = createTenantCondition(table);
            
            if (where == null) {
                delete.setWhere(tenantCondition);
            } else {
                delete.setWhere(new AndExpression(where, tenantCondition));
            }
        }
        
        @Override
        public void visit(Insert insert) {
            Table table = insert.getTable();
            if (isExcludedTable(table)) return;
            
            // 处理INSERT语句 - 自动添加租户ID列和值
            List<Column> columns = insert.getColumns();
            List<Expression> expressions = insert.getItemsList() != null ? 
                ((ExpressionList) insert.getItemsList()).getExpressions() : null;
            
            if (columns != null && expressions != null && columns.size() == expressions.size()) {
                // 检查是否已包含租户ID列
                boolean hasTenantColumn = false;
                for (Column col : columns) {
                    if (tenantIdColumn.equalsIgnoreCase(col.getColumnName())) {
                        hasTenantColumn = true;
                        break;
                    }
                }
                
                // 添加租户ID列和值
                if (!hasTenantColumn) {
                    columns.add(new Column(tenantIdColumn));
                    expressions.add(new StringValue(tenantId));
                }
            }
        }
        
        private Expression createTenantCondition(Table table) {
            Column tenantColumn = new Column(table, tenantIdColumn);
            return new EqualsTo(tenantColumn, new StringValue(tenantId));
        }
    }

    /**
     * 自定义SELECT访问器，用于处理复杂查询
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
            handleFromItem(fromItem);
            
            // 处理JOIN表
            List<Join> joins = plainSelect.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    FromItem joinItem = join.getRightItem();
                    handleFromItem(joinItem);
                }
            }
        }
        
        @Override
        public void visit(SetOperationList setOpList) {
            for (SelectBody selectBody : setOpList.getSelects()) {
                selectBody.accept(this);
            }
        }
        
        private void handleFromItem(FromItem fromItem) {
            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                if (isExcludedTable(table)) return;
                
                // 为表添加租户条件
                addTenantCondition(plainSelect, table);
            } else if (fromItem instanceof SubSelect) {
                // 处理子查询
                SubSelect subSelect = (SubSelect) fromItem;
                subSelect.getSelectBody().accept(this);
            }
        }
        
        private void addTenantCondition(PlainSelect plainSelect, Table table) {
            Expression where = plainSelect.getWhere();
            Column tenantColumn = new Column(table, tenantIdColumn);
            EqualsTo tenantCondition = new EqualsTo(tenantColumn, new StringValue(tenantId));
            
            if (where == null) {
                plainSelect.setWhere(tenantCondition);
            } else {
                plainSelect.setWhere(new AndExpression(where, tenantCondition));
            }
        }
    }
}
配套工具类
租户上下文管理
java
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
}
Spring 拦截器设置租户上下文
java
@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 从请求中提取租户信息
        String tenantId = request.getHeader("X-Tenant-Id");
        
        // 从JWT令牌获取（示例）
        // String token = request.getHeader("Authorization");
        // tenantId = JwtUtils.extractTenantId(token);
        
        // 从Session获取（示例）
        // tenantId = (String) request.getSession().getAttribute("tenantId");
        
        // 设置租户上下文
        if (tenantId != null && !tenantId.isEmpty()) {
            TenantContext.setCurrentTenant(tenantId);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, 
                               Exception ex) {
        // 清除租户上下文
        TenantContext.clear();
    }
}

// 注册Spring拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private TenantInterceptor tenantInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/public/**");
    }
}
配置文件示例
application.yml
yaml
mybatis:
  configuration:
    default-statement-timeout: 30
    map-underscore-to-camel-case: true

tenant:
  interceptor:
    enabled: true
    tenant-id-column: tenant_id
    excluded-tables: system_config, global_settings
    excluded-mappers: SystemMapper, PublicDataMapper
MyBatis 配置类
java
@Configuration
public class MybatisConfig {

    @Value("${tenant.interceptor.enabled:true}")
    private boolean tenantInterceptorEnabled;
    
    @Value("${tenant.interceptor.tenant-id-column:tenant_id}")
    private String tenantIdColumn;
    
    @Value("${tenant.interceptor.excluded-tables:}")
    private String excludedTables;
    
    @Value("${tenant.interceptor.excluded-mappers:}")
    private String excludedMappers;
    
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 注册租户拦截器
            TenantInterceptor interceptor = new TenantInterceptor();
            Properties properties = new Properties();
            properties.setProperty("enabled", String.valueOf(tenantInterceptorEnabled));
            properties.setProperty("tenantIdColumn", tenantIdColumn);
            properties.setProperty("excludedTables", excludedTables);
            properties.setProperty("excludedMappers", excludedMappers);
            interceptor.setProperties(properties);
            
            configuration.addInterceptor(interceptor);
        };
    }
}
处理特殊场景
1. 处理没有别名的表
java
private class TenantSelectVisitor extends SelectVisitorAdapter {
    // ...
    
    private void addTenantCondition(PlainSelect plainSelect, Table table) {
        // 处理没有别名的情况
        String tableAlias = table.getAlias() != null ? 
            table.getAlias().getName() : table.getName();
        
        // 创建条件表达式
        Column tenantColumn = new Column(tableAlias, tenantIdColumn);
        EqualsTo tenantCondition = new EqualsTo(tenantColumn, new StringValue(tenantId));
        
        // 添加到WHERE条件
        Expression where = plainSelect.getWhere();
        if (where == null) {
            plainSelect.setWhere(tenantCondition);
        } else {
            plainSelect.setWhere(new AndExpression(where, tenantCondition));
        }
    }
}
2. 处理 INSERT 语句的租户 ID 自动填充
java
@Override
public void visit(Insert insert) {
    Table table = insert.getTable();
    if (isExcludedTable(table)) return;
    
    // 获取列和值
    List<Column> columns = insert.getColumns();
    ItemsList itemsList = insert.getItemsList();
    
    // 处理多行插入
    if (itemsList instanceof MultiExpressionList) {
        MultiExpressionList multiList = (MultiExpressionList) itemsList;
        for (ExpressionList exprList : multiList.getExprList()) {
            processInsertValues(columns, exprList.getExpressions());
        }
    } 
    // 处理单行插入
    else if (itemsList instanceof ExpressionList) {
        ExpressionList exprList = (ExpressionList) itemsList;
        processInsertValues(columns, exprList.getExpressions());
    }
}

private void processInsertValues(List<Column> columns, List<Expression> expressions) {
    // 检查是否已包含租户ID列
    int tenantIndex = -1;
    for (int i = 0; i < columns.size(); i++) {
        if (tenantIdColumn.equalsIgnoreCase(columns.get(i).getColumnName())) {
            tenantIndex = i;
            break;
        }
    }
    
    // 添加或更新租户ID值
    if (tenantIndex >= 0) {
        // 更新现有租户ID值
        expressions.set(tenantIndex, new StringValue(tenantId));
    } else {
        // 添加租户ID列和值
        columns.add(new Column(tenantIdColumn));
        expressions.add(new StringValue(tenantId));
    }
}
3. 处理批量插入
java
@Intercepts({
    @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
    )
})
public class TenantInterceptor implements Interceptor {
    // ...
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // ...
        
        Object parameter = args[1];
        
        // 处理批量插入
        if (parameter instanceof Collection) {
            for (Object item : (Collection<?>) parameter) {
                if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    if (!map.containsKey(tenantIdColumn)) {
                        ((Map) map).put(tenantIdColumn, tenantId);
                    }
                }
            }
        }
        
        // ...
    }
}
