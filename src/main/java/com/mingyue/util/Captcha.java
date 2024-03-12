package com.mingyue.util;

import com.mingyue.vo.CaptchaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class Captcha extends HttpServlet {

    @Autowired
    private JWT jwt;

    @Autowired
    private RedisCache redisCache;


    public String getVerifyCode(HttpServletRequest request) throws ServletException, IOException {
        String uuId = generateRandomString(4);
        long currentTime = System.currentTimeMillis();
        CaptchaVo captchaVo = new CaptchaVo(uuId, currentTime);
        String iPKey = jwt.getClientIp(request);
        String key = "captcha:" + iPKey;
        redisCache.setCacheObject(key, captchaVo, 1, TimeUnit.MINUTES);
        return uuId;
    }

    public boolean verifyCaptcha(String uuId, HttpServletRequest request) {
        // 从Redis中获取验证码对象
        String iPKey = jwt.getClientIp(request);
        String key = "captcha:" + iPKey;
        CaptchaVo captchaVo = redisCache.getCacheObject(key);
        if (captchaVo != null && captchaVo.getUuId().equalsIgnoreCase(uuId)) {
            // 验证通过后删除验证码
            redisCache.remove(key);
            return true;
        }
        return false;
    }

    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }

    //verifyCaptcha
//    public boolean verifyCaptcha(String uuId,HttpServletRequest req) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        CaptchaVo captchaVo = (CaptchaVo) request.getSession().getAttribute("captcha");
//        CaptchaVo captchaVo = redisCache.getCacheObject(jwt.getClientIp(req));
//        System.out.println(captchaVo);
//        // 检查验证码是否存在且未过期
//        if (captchaVo != null && !captchaVo.isExpired()) {
//            // 移除验证码，确保一次性使用
////            request.getSession().removeAttribute("captcha");
//            // 检查验证码是否匹配
//            return captchaVo.getCaptcha().equals(uuId);
//        }
//        return false;
//    }


}
