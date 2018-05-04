package com.paper.order.retrofit.response;

import java.io.Serializable;

/**
 * 接收服务器返回数据 的类,
 * 根据返回数据的格式和数据解析方式（Json、XML等）定义
 * Created by Jbandxs on 2018/5/4.
 */

public class ResponseByHttp implements Serializable{
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseByHttp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}