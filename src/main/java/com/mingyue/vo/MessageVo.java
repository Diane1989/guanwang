package com.mingyue.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVo {

    private String taskId;

    private String userName;

    private String title;

    private String text;

    private Date createtime;

    private String phone;

    private String email;

    private Boolean success;

}
