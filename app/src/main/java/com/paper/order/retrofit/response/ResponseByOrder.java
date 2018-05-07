package com.paper.order.retrofit.response;

import java.util.List;

/**
 * Created by Jbandxs on 2018/5/8.
 */

public class ResponseByOrder {
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
        private Integer businessId;
        private String businessPicture;
        private String businessName;
        private String date;
        private String businessAddress;
        private String telephone;
        private Double price;

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
    }
}
