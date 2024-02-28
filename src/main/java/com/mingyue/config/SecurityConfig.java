package com.mingyue.config;

import com.mingyue.interceptor.RateLimitInterceptor;
import io.swagger.models.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Spring Security配置
        http.csrf().disable()
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("*");  // 允许所有来源
                    config.addAllowedMethod("*");  // 允许所有 HTTP 方法
                    config.addAllowedHeader("*");  // 允许所有请求头
                    return config;
                }).and()
                .headers().addHeaderWriter((request, response) -> {
                    response.setHeader("Pragma", "No-cache");
                    response.setHeader("Cache-Control", "no-cache");
                    response.setDateHeader("Expires", 0);
                    response.setHeader("access-control-allow-methods", "POST, GET");
                    response.setHeader("x-xss-protection", "0");
                    response.setHeader("x-frame-options", "SAMEORIGIN");
                    response.setHeader("content-security-policy", "frame-ancestors 'self'; upgrade-insecure-requests; block-all-mixed-content");
                }).and();

    }

//        http.headers()
//                .contentSecurityPolicy("default-src 'self'; img-src 'self' data:; style-src 'self' 'unsafe-inline';")



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Spring MVC拦截器配置
        registry.addInterceptor(new RateLimitInterceptor())
                .addPathPatterns("/**");
    }


}
