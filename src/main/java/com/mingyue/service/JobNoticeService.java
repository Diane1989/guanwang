package com.mingyue.service;

import com.mingyue.result.Result;
import com.mingyue.vo.ParmaVo;

import javax.servlet.http.HttpServletRequest;

public interface JobNoticeService {


    public Result submitMessage(ParmaVo parmaVo, HttpServletRequest request) throws Exception;

}
