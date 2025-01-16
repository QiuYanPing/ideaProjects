package com.qyp.chat.config;

import com.qyp.chat.interceptor.AdminInterception;
import com.qyp.chat.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    AdminInterception adminInterception;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/account/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/doc.html","/swagger-resources/**","/webjars/**","/v2/**","/swagger-ui.html/**").order(1);
        registry.addInterceptor(adminInterception).addPathPatterns("/admin/**").order(2);
    }
}
