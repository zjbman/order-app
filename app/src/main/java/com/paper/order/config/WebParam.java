package com.paper.order.config;

/**
 * Created by Jbandxs on 2018/5/4.
 */

public class WebParam {

    /** 少聪放wifi，手机和服务器连接该WiFi*/
//    public static final String BASE_URL = "http://192.168.191.3:8080/order/";

    /** 杨哥手机wif*/
//    public static final String BASE_URL = "http://172.20.10.10:8080/order/";

    /** 加薪手机WiFi*/
    public static final String BASE_URL = "http://192.168.43.142:8080/order/";

    /** 图片基本地址*/
    public static final String PIC_BASE_URL = "http://192.168.43.142:8080";

    public static final int success = 200;
    public static final int fail = -100;

    public static final int login_not_null = 101;
    public static final int login_not_exist = 102;
    public static final int login_not_right = 103;

    public static final int register_already_exist = 104;

}
