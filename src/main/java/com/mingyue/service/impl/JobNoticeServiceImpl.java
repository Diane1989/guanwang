package com.mingyue.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyue.mapper.JobNoticeMapper;
import com.mingyue.result.Result;
import com.mingyue.service.JobNoticeService;
import com.mingyue.util.AccessToken;
import com.mingyue.util.Captcha;
import com.mingyue.util.JWT;
import com.mingyue.vo.MessageVo;
import com.mingyue.vo.ParmaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class JobNoticeServiceImpl implements JobNoticeService {

    @Autowired
    private AccessToken accessToken;

    @Autowired
    private JWT jwt;

    @Autowired
    private Captcha captcha;

    @Autowired
    private JobNoticeMapper jobNoticeMapper;

    @Override
    public Result submitMessage(ParmaVo parmaVo, HttpServletRequest req) throws Exception {
        // 获取前端提交的验证码信息
        String uuId = parmaVo.getUuid();

        // 进行验证码校验
        if (!captcha.verifyCaptcha(uuId,req)) {
            System.out.println(uuId);
            return Result.error("验证码错误，留言提交失败!");
        }

        // 获取请求头中的Authorization信息（Token）
        String token = (String) req.getAttribute("Authorization");

        // 获取客户端IP
        String userIp = jwt.getClientIp(req);

        // 进行Token的业务验证，比如验证Token是否合法、是否过期等
        if (!jwt.validateToken(token, userIp)) {
            return Result.error("Token验证失败，留言提交失败!");
        }

//        if (jobNoticeMapper.compareMessage(parmaVo.getUserName(), parmaVo.getMsg()) > 0) {
//            return Result.error("同一用户名和留言内容24小时内只能提交一次，留言提交失败!");
//        }

        // 留言提交
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(2863036127L);
        request.setUseridList("020217234422848283,203136132926278689");
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);
        msg.getMarkdown().setTitle(parmaVo.getUserName() + "发送留言:" + parmaVo.getTitle());
        msg.getMarkdown().setText(" 标题：" + parmaVo.getTitle() + "  \n" +
                "姓名：" + parmaVo.getUserName() + "  \n" +
                "时间：" + date + "  \n" +
                "内容：" + parmaVo.getMsg() + "  \n" +
                "手机号：" + parmaVo.getPhone() + "  \n" +
                "邮箱：" + parmaVo.getEmail() + "  \n" +"  </br>" +
                "以上信息来自上海铭悦软件有限公司官网，请及时查看。");


        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken.getAccessToken());
        MessageVo messageVo = parseJsonToMessage(JSON.toJSONString(rsp));
        messageVo.setUserName(parmaVo.getUserName());
        messageVo.setTitle(parmaVo.getTitle());
        messageVo.setText(parmaVo.getMsg());
        messageVo.setPhone(parmaVo.getPhone());
        messageVo.setEmail(parmaVo.getEmail());
        jobNoticeMapper.insertMessage(messageVo);

        return Result.success(rsp);
    }

    private MessageVo parseJsonToMessage(String json) throws ParseException {
        JSONObject jsonObject = JSON.parseObject(json);
        MessageVo message = new MessageVo();
        // 解析 taskId
        message.setTaskId(jsonObject.getString("taskId"));
        // 解析 params 中的 msg
        JSONObject paramsObject = jsonObject.getJSONObject("params");
        String msgString = paramsObject.getString("msg");
        JSONObject msgObject = JSON.parseObject(msgString);
        // 提取 markdown 中的 text 和 title
        JSONObject markdownObject = msgObject.getJSONObject("markdown");
        if (markdownObject != null) {
            message.setText(markdownObject.getString("text"));
            message.setTitle(markdownObject.getString("title"));
        }
        if (jsonObject.getString("success").equals("true")){
            message.setSuccess(true);
        }

//        // 获取当前时间
//        LocalDateTime now = LocalDateTime.now();
//
//        // 定义日期时间格式
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // 格式化日期时间为字符串
//        String formattedDateTime = now.format(formatter);
//
//        // 将格式化后的字符串转换为Date类型
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date createTime = sdf.parse(formattedDateTime);

        message.setCreatetime(new Date());
        return message;
    }

}
