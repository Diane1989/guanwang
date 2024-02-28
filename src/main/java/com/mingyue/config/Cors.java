//package com.mingyue.config;
//
//import com.mingyue.interceptor.RateLimitInterceptor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class Cors extends OncePerRequestFilter implements WebMvcConfigurer {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest)servletRequest;
//
//        request.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=utf-8");
//        response.setHeader("Pragma", "No-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expires", 0);
////        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("access-control-allow-methods", "POST, GET");
//        response.setHeader("Access-Control-Max-Age", "3600");
////        response.setHeader("Access-Control-Allow-Headers", "XRequestedWith, Content-Type, LastModified,Accept, Origin, Authorization,contenttype,xfilecategory,xfilename,xfilesize");
////        response.setHeader("access-control-allow-credentials", "true");
////        response.setHeader("strict-transport-security","max-age=31536000; includeSubDomains; preload");
//        response.setHeader("x-xss-protection","0");
////        response.setHeader("x-frame-options","SAMEORIGIN");
////        response.setHeader("x-content-type-options","nosniff");
////        response.setHeader("referrer-policy","no-referrer-when-downgrade");
////        response.setHeader("permissions-policy","battery=(), camera=(), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), payment=(), usb=()");
//        response.setHeader("content-security-policy","frame-ancestors 'self'; upgrade-insecure-requests; block-all-mixed-content");
//
//        request.authorizeRequests().anyRequest().permitAll();
//
////        String sessionid =  request.getSession().getId();
////        Cookie cookie=new Cookie("sessionId",sessionid);
////        cookie.setHttpOnly(true);
////        cookie.setSecure(true);
////        cookie.setDomain(request.getServerName());
////        cookie.setMaxAge(3600);
////        response.addCookie(cookie);
////        String method = request.getMethod();
////        if(method.equalsIgnoreCase("OPTIONS")){
////            servletResponse.getOutputStream().write("Success".getBytes("utf-8"));
////        }else{
////            filterChain.doFilter(servletRequest, servletResponse);
////        }
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // Spring MVC拦截器配置
//        registry.addInterceptor(new RateLimitInterceptor())
//                .addPathPatterns("/**");
//    }
//
//}
