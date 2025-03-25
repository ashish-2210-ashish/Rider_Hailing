package com.example.Rider_Co.interceptorRegistry;

import com.example.Rider_Co.interceptors.ApiServiceInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Component
public class ApiServiceRegistry implements WebMvcConfigurer {

    @Autowired
    ApiServiceInterceptors apiServiceInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiServiceInterceptors).addPathPatterns("/api/**");
    }
}
