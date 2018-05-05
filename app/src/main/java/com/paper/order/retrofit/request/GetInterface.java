package com.paper.order.retrofit.request;

import com.paper.order.retrofit.response.ResponseByBusiness;
import com.paper.order.retrofit.response.ResponseByComment;
import com.paper.order.retrofit.response.ResponseByGoods;
import com.paper.order.retrofit.response.ResponseByUser;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * 用于描述网络请求 的接口
 * Created by Jbandxs on 2018/5/4.
 */

public interface  GetInterface {

    @GET("user/Login.html")
    Call<ResponseByUser> get(@Query("username")String username, @Query("password") String password);

    /** 登录、注册 多个参数*/
    @GET("user/{api}")
    Call<ResponseByUser> getManyParam1(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 商家 多个参数*/
    @GET("business/{api}")
    Call<ResponseByBusiness> getManyParam2(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 商品*/
    @GET("goods/{api}")
    Call<ResponseByGoods> getGoods(@Path("api")String api,@QueryMap Map<String, Object> params);

    /** 评论*/
    @GET("comment/{api}")
    Call<ResponseByComment> getComment(@Path("api")String api, @QueryMap Map<String, Object> params);
}
