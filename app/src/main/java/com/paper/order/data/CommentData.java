package com.paper.order.data;

import com.paper.order.retrofit.response.ResponseByComment;

/**
 * Created by Jbandxs on 2018/5/6.
 */

public class CommentData {
    private int id;
    private String userName;
    private String comment;
    private String date;

    public CommentData(ResponseByComment.Msg msg){
        id = msg.getId();
        userName = msg.getUserName();
        comment = msg.getComment();
        date = msg.getDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
