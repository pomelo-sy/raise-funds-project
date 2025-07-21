import java.util.Stack;

public class TenantContext {
    private static final ThreadLocal<Stack<String>> tenantStack = ThreadLocal.withInitial(Stack::new);
    
    // 设置当前租户ID
    public static void setCurrentTenant(String tenantId) {
        Stack<String> stack = tenantStack.get();
        stack.push(tenantId);
    }
    
    // 获取当前租户ID
    public static String getCurrentTenant() {
        Stack<String> stack = tenantStack.get();
        return stack.isEmpty() ? null : stack.peek();
    }
    
    // 清除当前租户ID
    public static void clear() {
        Stack<String> stack = tenantStack.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
        if (stack.isEmpty()) {
            tenantStack.remove();
        }
    }
    
    // 临时切换租户上下文（用于异步任务）
    public static void withTenant(String tenantId, Runnable runnable) {
        setCurrentTenant(tenantId);
        try {
            runnable.run();
        } finally {
            clear();
        }
    }
}
---------------------------------------------------------------------
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 从请求头获取租户ID
        String tenantId = request.getHeader("X-Tenant-ID");
        
        // 从JWT令牌获取租户ID（示例）
        // String token = request.getHeader("Authorization");
        // tenantId = JwtUtils.extractTenantId(token);
        
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
        TenantContext.clear();
    }
}

