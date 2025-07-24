import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

    @Autowired
    private TenantInnerInterceptor tenantInterceptor;

    @Around("@annotation(tenantFilter) || @within(tenantFilter)")
    public Object around(ProceedingJoinPoint joinPoint, TenantFilter tenantFilter) throws Throwable {
        // 保存原始配置
        boolean originalEnabled = tenantInterceptor.isEnabled();
        String originalColumn = tenantInterceptor.getTenantColumn();
        Set<String> originalExcludedTables = tenantInterceptor.getExcludedTables();
        
        try {
            // 应用注解配置
            if (!tenantFilter.enabled()) {
                tenantInterceptor.setEnabled(false);
            }
            
            if (!tenantFilter.tenantColumn().isEmpty()) {
                tenantInterceptor.setTenantColumn(tenantFilter.tenantColumn());
            }
            
            if (tenantFilter.excludeTables().length > 0) {
                tenantInterceptor.getExcludedTables().addAll(Arrays.asList(tenantFilter.excludeTables()));
            }
            
            // 执行原方法
            return joinPoint.proceed();
        } finally {
            // 恢复原始配置
            tenantInterceptor.setEnabled(originalEnabled);
            tenantInterceptor.setTenantColumn(originalColumn);
            tenantInterceptor.setExcludedTables(originalExcludedTables);
        }
    }
}
