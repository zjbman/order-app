package com.paper.order.data;

import java.io.Serializable;

/**
 * Created by Jbandxs on 2018/5/6.
 */

public class UserData implements Serializable{
    private int id;
    private String username;
    private String password;
    private String name;
    private Double money;
    private String telephone;
    private String email;
    private String qq;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", qq='" + qq + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
