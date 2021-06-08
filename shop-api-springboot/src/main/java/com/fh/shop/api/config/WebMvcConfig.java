package com.fh.shop.api.config;

import com.fh.shop.api.interceptor.LoginIntercepotor;
import com.fh.shop.api.interceptor.TokenIntercepotor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercepotor()).addPathPatterns("/api/**");
        registry.addInterceptor(tokenIntercepotor()).addPathPatterns("/api/**");
    }


    @Bean
    public LoginIntercepotor loginIntercepotor(){
        return new LoginIntercepotor();
    }

    @Bean
    public TokenIntercepotor tokenIntercepotor(){
        return new TokenIntercepotor();
    }
}
