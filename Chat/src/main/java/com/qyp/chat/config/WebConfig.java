package com.qyp.chat.config;

import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.interceptor.AdminInterception;
import com.qyp.chat.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    AdminInterception adminInterception;
    @Autowired
    AppConfig appConfig;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/account/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/doc.html","/swagger-resources/**","/webjars/**","/v2/**","/swagger-ui.html/**").order(1);
        registry.addInterceptor(adminInterception).addPathPatterns("/admin/**").addPathPatterns("/app-update/**").order(2);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE;
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:"+path);
    }
}
