package com.paper.order.retrofit.response;

import java.util.List;

/**
 * 服务器返回购物车信息封装类
 * Created by Jbandxs on 2018/5/7.
 */

public class ResponseByCart {
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

    @Override
    public String toString() {
        return "ResponseByCart{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }

    public class Msg{
        private Integer id;
        private String name;
        private String username;
        private Integer businessId;
        private String businessName;
        private List<GoodsData> goodsDataList;

        public Integer getBusinessId() {
            return businessId;
        }

        public void setBusinessId(Integer businessId) {
            this.businessId = businessId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public List<GoodsData> getGoodsDataList() {
            return goodsDataList;
        }

        public void setGoodsDataList(List<GoodsData> goodsDataList) {
            this.goodsDataList = goodsDataList;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", businessName='" + businessName + '\'' +
                    ", goodsDataList=" + goodsDataList +
                    '}';
        }
    }

    public class GoodsData{
        private Integer id;
        private String goodsName;
        private String details;
        private String picture;
        private Integer number;
        private Double price;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "GoodsData{" +
                    "id=" + id +
                    ", goodsName='" + goodsName + '\'' +
                    ", details='" + details + '\'' +
                    ", picture='" + picture + '\'' +
                    ", number=" + number +
                    ", price=" + price +
                    '}';
        }
    }
}
