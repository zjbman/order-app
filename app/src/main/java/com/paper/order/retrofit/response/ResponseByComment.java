package com.paper.order.retrofit.response;

import java.util.List;

/**
 * 服务器返回商家信息封装类
 * Created by Jbandxs on 2018/5/5.
 */

public class ResponseByComment {
    private int code;
    private List<Msg> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Msg> getMsg() {
        return msg;
    }

    public void setMsg(List<Msg> msg) {
        this.msg = msg;
    }

    public class Msg{
        private int id;
        private String userName;
        private String comment;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", comment='" + comment + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResponseByBusiness{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }
}
