package com.mingyue.util;

import com.mingyue.vo.CaptchaVo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;

@Service
public class Captcha extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public String getVerifyCode(HttpServletRequest request) throws ServletException, IOException {
        String captcha = generateRandomString(4);

        // 记录生成时间戳，并将验证码存储在Session中
        long currentTime = System.currentTimeMillis();
        CaptchaVo captchaVo = new CaptchaVo(captcha, currentTime);
        request.getSession().setAttribute("captcha", captchaVo);

        return captcha;
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
    public boolean verifyCaptcha(String uuId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CaptchaVo captchaVo = (CaptchaVo) request.getSession().getAttribute("captcha");

        // 检查验证码是否存在且未过期
        if (captchaVo != null && !captchaVo.isExpired()) {
            // 移除验证码，确保一次性使用
            request.getSession().removeAttribute("captcha");
            // 检查验证码是否匹配
            return captchaVo.getCaptcha().equals(uuId);
        }
        return false;
    }

}
