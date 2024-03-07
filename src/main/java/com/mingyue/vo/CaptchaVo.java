package com.mingyue.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVo implements Serializable {

    private String uuId;
    private long createTime;

    public CaptchaVo(String uuId, long createTime) {
        this.uuId = uuId;
        this.createTime = createTime;
    }
    public CaptchaVo() {
        // 无参构造方法
    }


    public String getUuId() {
        return uuId;
    }

    public long getCreateTime() {
        return createTime;
    }
}
