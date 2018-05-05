package com.paper.order.retrofit.response;

import java.util.List;

/**
 * 服务器返回的商品 封装类
 * Created by Jbandxs on 2018/5/6.
 */

public class ResponseByGoods {
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
        private String goodsName;
        private String details;
        private String price;
        private String picture;
        private String businessName;
        private int businessId;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public int getBusinessId() {
            return businessId;
        }

        public void setBusinessId(int businessId) {
            this.businessId = businessId;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "id=" + id +
                    ", goodsName='" + goodsName + '\'' +
                    ", details='" + details + '\'' +
                    ", price='" + price + '\'' +
                    ", picture='" + picture + '\'' +
                    ", businessName='" + businessName + '\'' +
                    ", businessId=" + businessId +
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
