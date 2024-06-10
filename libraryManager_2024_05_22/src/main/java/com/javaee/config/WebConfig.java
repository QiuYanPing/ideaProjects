package com.javaee.config;

import com.javaee.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) { //添加 CORS 配置来允许跨域请求
        registry.addMapping("/**") // 允许所有路径的跨域请求
                .allowedOrigins("http://localhost:8081") // 允许这个源进行跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的 HTTP 方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许发送带有凭据的请求（如 cookie、HTTP 认证及客户端 SSL 证明等）
    }
}
