package com.mingyue.interceptor;

import com.mingyue.util.JWT;
import com.mingyue.vo.RequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private JWT jwt;

    private final Map<String, RequestVo> ipRequests = new HashMap<>();
    private static final int REQUEST_LIMIT = 2000;
    private static final Duration TIME_WINDOW = Duration.ofMinutes(1);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取客户端 IP 地址
        String userIp = jwt.getClientIp(request);
        // 检查IP是否在Map中，如果不在则初始化请求信息
        RequestVo requestVo = ipRequests.computeIfAbsent(userIp, k -> new RequestVo(0, LocalDateTime.now()));

        // 获取上次请求时间
        LocalDateTime lastRequestTime = requestVo.getLastRequestTime();

        // 如果超过时间窗口，重置请求次数
        if (Duration.between(lastRequestTime, LocalDateTime.now()).compareTo(TIME_WINDOW) > 0) {
            requestVo.setRequestCount(0);
        }

        // 检查请求次数是否超过限制
        if (requestVo.getRequestCount() >= REQUEST_LIMIT) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("请求频繁，请稍后再试");
            return false;
        }

        // 更新请求次数和时间
        requestVo.setRequestCount(requestVo.getRequestCount() + 1);
        requestVo.setLastRequestTime(LocalDateTime.now());

        // 将JWT token添加到请求属性中
        String token = request.getHeader("Authorization");
        request.setAttribute("Authorization", token);
        return true;
    }

}
