package com.mingyue.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessLogServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogServiceImpl.class);

    public void logAccess(String userIp) {
        logger.info("IP {} 访问成功", userIp);
    }

    public void logRateLimitExceeded(String userIp) {
        logger.warn("IP {} 请求频率超限", userIp);
    }

}
