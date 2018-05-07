package com.paper.order.data;

import com.paper.order.retrofit.response.ResponseByOrder;

/**
 * Created by Jbandxs on 2018/5/8.
 */

public class OrderData {
    private Integer businessId;
    private String businessPicture;
    private String businessName;
    private String date;
    private String businessAddress;
    private String telephone;
    private Double price;

    public OrderData(ResponseByOrder.Msg msg){
        businessId = msg.getBusinessId();
        businessPicture = msg.getBusinessPicture();
        businessName = msg.getBusinessName();
        date = msg.getDate();
        businessAddress = msg.getBusinessAddress();
        telephone = msg.getTelephone();
        price = msg.getPrice();
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getBusinessPicture() {
        return businessPicture;
    }

    public void setBusinessPicture(String businessPicture) {
        this.businessPicture = businessPicture;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "businessId=" + businessId +
                ", businessPicture='" + businessPicture + '\'' +
                ", businessName='" + businessName + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
