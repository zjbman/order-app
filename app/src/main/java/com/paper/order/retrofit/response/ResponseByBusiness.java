package com.paper.order.retrofit.response;

import java.util.List;

/**
 * 服务器返回商家信息封装类
 * Created by Jbandxs on 2018/5/5.
 */

public class ResponseByBusiness {
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
        private String businessName;
        private String picture;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "id=" + id +
                    ", businessName='" + businessName + '\'' +
                    ", picture='" + picture + '\'' +
                    ", address='" + address + '\'' +
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
