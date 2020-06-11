package org.shizhijian.raisefunds.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Slf4j
public class RequestParamLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig){
        log.info("RequestParamLogFilter inited");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest r = (HttpServletRequest) request;
        log.info("method: {}", r.getMethod());
        String path = r.getQueryString();
        log.info("path:{}", path);
        String url = r.getRequestURI();
        log.info("url: {}", url);
        Map<String, String[]> map = request.getParameterMap();
        map.entrySet().forEach((m)->{
            log.info("key: {}, value: {}",m.getKey(), m.getValue());

        });
        log.info("parameters: {}", map);

        long start = System.currentTimeMillis();
        chain.doFilter(request,response);
        System.out.println("Execute cost="+(System.currentTimeMillis()-start));
    }

    @Override
    public void destroy(){
        log.info("RequestParamLogFilter destroyed");
    }
}
