package com.mingyue.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JWT {

    private static final String SECRET_KEY = "OrZDncKBKgk4DCPvqab8Oei7MulXW9T2ozA1NgwYsF7";
    private static final long EXPIRATION_TIME = 3600L * 1000 * 24; // 1 day
    private static final Map<String, String> ipTokenMap = new ConcurrentHashMap<>();


    //生成token
    public static String generateToken(HttpServletRequest request , HttpServletResponse response) throws Exception {
        // 获取客户端 IP 地址
        String userIp = JWT.getClientIp(request);

        if (ipTokenMap.containsKey(userIp)) {
            return ipTokenMap.get(userIp);
        }

        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String token = Jwts.builder()
                .setSubject(userIp)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();

        ipTokenMap.put(userIp, token);
        response.setHeader("Authorization", token);
        return "Success";
    }


    public static Optional<Claims> getClaimsFromToken(String token, String secret) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    //判断token是否有效
    public static boolean isExpired(String token, String secret) {
        Optional<Claims> claims = getClaimsFromToken(token, secret);
        if (claims.isPresent()) {
            Date expiration = claims.get().getExpiration();
            return expiration.before(new Date());
        }
        return false;
    }

    //判断token是否有效
    public static boolean validateToken(String token, String userIp) {
        try {
            Optional<Claims> claims = getClaimsFromToken(token, SECRET_KEY);
            if (claims.isPresent()) {
                String tokenUserIp = claims.get().getSubject();
                if (userIp.equals(tokenUserIp)) {
                    if (!isExpired(token, SECRET_KEY)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取客户端IP
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
