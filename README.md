import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class MybatisConfig {

    @Value("${tenant.isolation.enabled:true}")
    private boolean tenantIsolationEnabled;
    
    @Value("${tenant.isolation.column:tenant_id}")
    private String tenantIdColumn;
    
    @Value("${tenant.isolation.excluded-tables:}")
    private String excludedTables;
    
    @Value("${tenant.isolation.excluded-mappers:}")
    private String excludedMappers;
    
    @Bean
    public Interceptor tenantIsolationInterceptor() {
        TenantIsolationInterceptor interceptor = new TenantIsolationInterceptor();
        
        Properties properties = new Properties();
        properties.setProperty("enabled", String.valueOf(tenantIsolationEnabled));
        properties.setProperty("tenantIdColumn", tenantIdColumn);
        properties.setProperty("excludedTables", excludedTables);
        properties.setProperty("excludedMappers", excludedMappers);
        
        interceptor.setProperties(properties);
        return interceptor;
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactory(
            SqlSessionFactory sqlSessionFactory, 
            Interceptor tenantIsolationInterceptor) {
        
        org.apache.ibatis.session.Configuration configuration = 
            sqlSessionFactory.getConfiguration();
        
        // 添加租户隔离拦截器
        configuration.addInterceptor(tenantIsolationInterceptor);
        
        return sqlSessionFactory;
    }
}


---------------------------------------------------




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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


-------------------------------------------


import com.github.jsqlparser.JSQLParserException;
import com.github.jsqlparser.parser.CCJSqlParserUtil;
import com.github.jsqlparser.statement.Statement;
import org.junit.jupiter.api.Test;

class TenantIsolationInterceptorTest {
    
    @Test
    void testSelectWithJoin() throws JSQLParserException {
        String sql = "SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id";
        String tenantId = "tenant_001";
        
        Statement stmt = CCJSqlParserUtil.parse(sql);
        TenantIsolationInterceptor.TenantSqlVisitor visitor = 
            new TenantIsolationInterceptor().new TenantSqlVisitor(tenantId);
        stmt.accept(visitor);
        
        String result = stmt.toString();
        String expected = "SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id " +
                          "WHERE u.tenant_id = 'tenant_001' AND o.tenant_id = 'tenant_001'";
        
        assert expected.equals(result);
    }
    
    @Test
    void testInsertStatement() throws JSQLParserException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John')";
        String tenantId = "tenant_001";
        
        Statement stmt = CCJSqlParserUtil.parse(sql);
        TenantIsolationInterceptor.TenantSqlVisitor visitor = 
            new TenantIsolationInterceptor().new TenantSqlVisitor(tenantId);
        stmt.accept(visitor);
        
        String result = stmt.toString();
        String expected = "INSERT INTO users (id, name, tenant_id) VALUES (1, 'John', 'tenant_001')";
        
        assert expected.equals(result);
    }
    
    @Test
    void testUpdateStatement() throws JSQLParserException {
        String sql = "UPDATE users SET name = 'John' WHERE id = 1";
        String tenantId = "tenant_001";
        
        Statement stmt = CCJSqlParserUtil.parse(sql);
        TenantIsolationInterceptor.TenantSqlVisitor visitor = 
            new TenantIsolationInterceptor().new TenantSqlVisitor(tenantId);
        stmt.accept(visitor);
        
        String result = stmt.toString();
        String expected = "UPDATE users SET name = 'John' WHERE id = 1 AND tenant_id = 'tenant_001'";
        
        assert expected.equals(result);
    }
}
