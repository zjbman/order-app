package com.paper.order.retrofit.response;

import com.paper.order.data.UserData;

/**
 * 服务器返回商家信息封装类
 * Created by Jbandxs on 2018/5/5.
 */

public class ResponseByUserInfo {
    private int code;
    private UserData msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserData getMsg() {
        return msg;
    }

    public void setMsg(UserData msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseByBusiness{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }
}
