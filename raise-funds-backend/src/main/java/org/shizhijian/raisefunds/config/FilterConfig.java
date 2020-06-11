package org.shizhijian.raisefunds.config;

import org.shizhijian.raisefunds.filter.RequestParamLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registerBean(){

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestParamLogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("requestParamLogFilter");
        registration.setOrder(1);
        return registration;
    }
}
