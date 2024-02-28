package com.mingyue.result;

public class Result {

    // 响应业务状态
    private Integer status;
    // 响应消息
    private Object msg;
    // 响应中的数据
    private Object data;


    public static Result build(Integer status, String msg, Object data) {
        return new Result(status, msg, data);
    }

    //成功时候的调用
    public static Result success(Object data) {
        return new Result(data);
    }

    //失败时候的调用
    public static Result error(Object msg) {
        return new Result(101, msg, null);
    }
    //失败时候的调用
    public static Result error(Integer status,Object msg) {
        return new Result(status, msg, null);
    }


    public static Result success() {
        return new Result(null);
    }

    public Result() {

    }

    public static Result build(Integer status, String msg) {
        return new Result(status, msg, null);
    }

    public Result(Integer status, Object msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(Object data) {
        this.status = 100;
        this.msg = "SUCCESS";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return status != null && status.equals(100);
    }

}
