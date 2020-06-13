package org.shizhijian.raisefunds.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateInitializer {


    @Bean
    RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();

        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        converters.add(new ByteArrayHttpMessageConverter());

        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(fastJsonConverter);

        template.setMessageConverters(converters);
        return template;
    }
}
