package com.paper.order.data;

import com.paper.order.retrofit.response.ResponseByGoods;

/**
 * Created by Jbandxs on 2018/5/6.
 */

public class GoodsData {
    private int id;
    private String goodsName;
    private String detail;
    private String price;
    private String picture;
    private int businessId;
    private String businessName;

    public GoodsData(ResponseByGoods.Msg msg){
        id = msg.getId();
        goodsName = msg.getGoodsName();
        detail = msg.getDetails();
        price = msg.getPrice();
        picture = msg.getPicture();
        businessId = msg.getBusinessId();
        businessName = msg.getBusinessName();
    }

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "GoodsData{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", detail='" + detail + '\'' +
                ", price='" + price + '\'' +
                ", picture='" + picture + '\'' +
                ", businessId=" + businessId +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
