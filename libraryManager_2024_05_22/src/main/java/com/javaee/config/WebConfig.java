package com.javaee.config;

import com.javaee.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration

public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/login","/register");
        /*registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                // 在这里设置你的自定义响应头
                response.setHeader("Access-Control-Allow-Origin", "*");
                // 可以添加更多的响应头设置
                response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,PUT,POST,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "*");
            }
        });*/
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) { //添加 CORS 配置来允许跨域请求
        /*registry.addMapping("/**") // 允许所有路径的跨域请求
                .allowedOrigins("http://localhost:8081") // 允许这个源进行跨域请求
                *//*.allowedHeaders("Content-Type", "Authorization","token")//允许请求头*//*
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH","OPTIONS") // 允许的 HTTP 方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许发送带有凭据的请求（如 cookie、HTTP 认证及客户端 SSL 证明等）*/


        registry.addMapping("/**") // 允许所有路径的跨域请求
                .allowedOrigins("http://localhost:8081")// 允许哪些源的请求，可以用*表示所有
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的头信息，可以用*表示所有
                .maxAge(3600) // 预检请求的缓存时间（秒），默认为1800秒
                .allowCredentials(true); // 是否允许携带凭证（cookies, HTTP认证及客户端SSL证明等），默认为false


    }
}



