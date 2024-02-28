package com.mingyue.vo;

import java.time.LocalDateTime;

public class RequestVo {

    private int requestCount;
    private LocalDateTime lastRequestTime;

    public RequestVo(int requestCount, LocalDateTime lastRequestTime) {
        this.requestCount = requestCount;
        this.lastRequestTime = lastRequestTime;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public LocalDateTime getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(LocalDateTime lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }


}
