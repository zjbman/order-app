package com.paper.order.data;

/**
 * Created by Jbandxs on 2018/5/8.
 */

public class GoodsDataForOrderSubmit {
    private int businessId;
    private int goodsNumber;
    private int goodsId;
    private String goodsName;
    private double price;
    private String businessName;
    private String businessPicture;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessPicture() {
        return businessPicture;
    }

    public void setBusinessPicture(String businessPicture) {
        this.businessPicture = businessPicture;
    }
}
