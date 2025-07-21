#raise-funds-project started
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class TenantInterceptor implements Interceptor {

    private final JsqlParserSupport sqlParser = new JsqlParserSupport();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        
        // 只处理需要租户隔离的 Mapper
        if (shouldApplyTenantFilter(mappedStatement.getId())) {
            BoundSql boundSql = handler.getBoundSql();
            String originalSql = boundSql.getSql();
            String tenantId = TenantContext.getCurrentTenant();
            
            // 使用 SQL 解析器修改 SQL
            Statement stmt = CCJSqlParserUtil.parse(originalSql);
            if (stmt instanceof Select) {
                stmt = processSelectStatement((Select) stmt, tenantId);
            } else if (stmt instanceof Update) {
                stmt = processUpdateStatement((Update) stmt, tenantId);
            } else if (stmt instanceof Delete) {
                stmt = processDeleteStatement((Delete) stmt, tenantId);
            }
            
            // 替换原始 SQL
            metaObject.setValue("delegate.boundSql.sql", stmt.toString());
        }
        
        return invocation.proceed();
    }

    private Statement processSelectStatement(Select select, String tenantId) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            addTenantCondition(plainSelect, tenantId);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOpList = (SetOperationList) selectBody;
            for (SelectBody body : setOpList.getSelects()) {
                if (body instanceof PlainSelect) {
                    addTenantCondition((PlainSelect) body, tenantId);
                }
            }
        }
        return select;
    }

    private void addTenantCondition(PlainSelect plainSelect, String tenantId) {
        // 获取所有涉及的表
        List<Table> tables = new ArrayList<>();
        tables.add(plainSelect.getFromItem());
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                tables.add(join.getRightItem());
            }
        }

        // 为每张表添加租户条件
        Expression where = plainSelect.getWhere();
        for (Table table : tables) {
            Column tenantColumn = new Column(table, "tenant_id");
            EqualsTo tenantCondition = new EqualsTo(tenantColumn, new StringValue(tenantId));
            
            if (where == null) {
                where = tenantCondition;
            } else {
                where = new AndExpression(where, tenantCondition);
            }
        }
        plainSelect.setWhere(where);
    }

    private boolean shouldApplyTenantFilter(String statementId) {
        // 排除不需要租户隔离的 SQL
        return !statementId.contains("SystemMapper") 
            && !statementId.contains("TenantMapper");
    }

    // 实现其他类型的 SQL 处理...
}
