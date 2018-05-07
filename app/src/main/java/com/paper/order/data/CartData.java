package com.paper.order.data;

import com.paper.order.retrofit.response.ResponseByCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jbandxs on 2018/5/7.
 */

public class CartData {
    private Integer id;
    private String name;
    private String username;
    private String businessName;
    private List<GoodsData> goodsDataList;

    public CartData(ResponseByCart.Msg msg){
        id = msg.getId();
        name = msg.getName();
        username = msg.getUsername();
        businessName = msg.getBusinessName();

        goodsDataList = new ArrayList<>();
        List<ResponseByCart.GoodsData> list = msg.getGoodsDataList();
        for(ResponseByCart.GoodsData data : list){
            GoodsData goodsData = new GoodsData();
            goodsData.setDetails(data.getDetails());
            goodsData.setGoodsName(data.getGoodsName());
            goodsData.setId(data.getId());
            goodsData.setNumber(data.getNumber());
            goodsData.setPrice(data.getPrice());
            goodsData.setPicture(data.getPicture());

            goodsDataList.add(goodsData);
        }
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

    @Override
    public String toString() {
        return "CartData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", businessName='" + businessName + '\'' +
                ", goodsDataList=" + goodsDataList +
                '}';
    }
}
