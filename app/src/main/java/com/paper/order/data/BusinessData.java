package com.paper.order.data;

/**
 * 服务器返回的商家信息
 * Created by Jbandxs on 2018/5/5.
 */

public class BusinessData {
    private int id;
    private String businessName;
    private String picture;
    private String address;
    private String telephone;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "BusinessData{" +
                "id=" + id +
                ", businessName='" + businessName + '\'' +
                ", picture='" + picture + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
