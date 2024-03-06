package com.mingyue.controller;

import com.mingyue.result.Result;
import com.mingyue.service.JobNoticeService;
import com.mingyue.util.Captcha;
import com.mingyue.util.JWT;
import com.mingyue.vo.ParmaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/notice")
@Api(value="官网", produces="application/json;charset=UTF-8")
public class JobNoticeController {

    @Autowired
    private JobNoticeService jobNoticeService;

    @Autowired
    private JWT jwt;

    @Autowired
    private Captcha captcha;


    @ApiOperation(value = "提交留言", notes = "userName:用户名，title:标题，msg:留言内容，email:邮箱，phone:手机号，uuid:验证码")
    @PostMapping("/submitMessage")
    public Result submitMessage(@RequestBody ParmaVo parmaVo,HttpServletRequest request) throws Exception {
        return jobNoticeService.submitMessage(parmaVo,request);
    }

    @ApiOperation(value = "获取验证码", notes = "用户测试notes")
    @PostMapping(value = "/getVerifyCode")
    public Result getVerifyCode(HttpServletRequest request) throws ServletException, IOException {
        return Result.success(captcha.getVerifyCode(request));
    }

    @ApiOperation(value = "首页", notes = "用户测试notes")
    @PostMapping("/index")
    public String generateToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jwt.generateToken(request,response);
    }

}
