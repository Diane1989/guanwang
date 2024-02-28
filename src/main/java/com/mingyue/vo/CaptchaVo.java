package com.mingyue.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVo implements Serializable {

    private String captcha;
    private long createTime;

    public CaptchaVo(String captcha, long createTime) {
        this.captcha = captcha;
        this.createTime = createTime;
    }

    // 检查验证码是否过期
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        // 验证码有效期为1分钟
        return currentTime - createTime > 60000;
    }

}
