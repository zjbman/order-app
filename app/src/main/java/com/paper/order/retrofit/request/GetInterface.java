package com.paper.order.retrofit.request;

import com.paper.order.retrofit.response.ResponseByBusiness;
import com.paper.order.retrofit.response.ResponseByCart;
import com.paper.order.retrofit.response.ResponseByComment;
import com.paper.order.retrofit.response.ResponseByGoods;
import com.paper.order.retrofit.response.ResponseByUsually;
import com.paper.order.retrofit.response.ResponseByUserInfo;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * 用于描述网络请求 的接口
 * Created by Jbandxs on 2018/5/4.
 */

public interface  GetInterface {

    @GET("user/Login.html")
    Call<ResponseByUsually> get(@Query("username")String username, @Query("password") String password);

    /** 登录、注册 多个参数*/
    @GET("user/{api}")
    Call<ResponseByUsually> getManyParam1(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 商家 多个参数*/
    @GET("business/{api}")
    Call<ResponseByBusiness> getManyParam2(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 商品*/
    @GET("goods/{api}")
    Call<ResponseByGoods> getGoods(@Path("api")String api,@QueryMap Map<String, Object> params);

    /** 评论*/
    @GET("comment/{api}")
    Call<ResponseByComment> getComment(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 获取评论*/
    @GET("user/{api}")
    Call<ResponseByUserInfo> getUser(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 新增评论*/
    @GET("comment/{api}")
    Call<ResponseByUsually> insertComment(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 添加购物车*/
    @GET("cart/{api}")
    Call<ResponseByUsually> addCart(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 购物车*/
    @GET("cart/{api}")
    Call<ResponseByCart> getCart(@Path("api")String api, @QueryMap Map<String, Object> params);

    /** 添加购物车 post*/
    @FormUrlEncoded
    @POST("cart/{api}")
    Call<ResponseByUsually> addCartPost1(@Path("api")String api, @FieldMap Map<String, Object> params);

    /** 添加购物车 post*/
    @FormUrlEncoded
    @POST("cart/Save.html")
    Call<ResponseByUsually> addCartPost(@FieldMap Map<String, Object> params);

}
