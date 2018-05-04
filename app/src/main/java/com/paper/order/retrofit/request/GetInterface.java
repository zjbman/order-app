package com.paper.order.retrofit.request;

import com.paper.order.retrofit.response.ResponseByHttp;


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
    Call<ResponseByHttp> get(@Query("username")String username, @Query("password") String password);

    /** 多个参数*/
    @GET("user/Register.html")
    Call<ResponseByHttp> getManyParam1(@QueryMap Map<String, Object> params);

    /** 多个参数*/
    @GET("user/{api}")
    Call<ResponseByHttp> getManyParam(@Path("api")String api,@QueryMap Map<String, Object> params);
}
