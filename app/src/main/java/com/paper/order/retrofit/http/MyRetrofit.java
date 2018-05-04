package com.paper.order.retrofit.http;

import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByHttp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit简单的封装类
 * Created by Jbandxs on 2018/5/5.
 */

public class MyRetrofit {
    private static MyRetrofit instance;

    private MyRetrofit(){}

    public static MyRetrofit getInstance(){
        if(instance == null){
            synchronized (MyRetrofit.class){
                if(instance == null){
                    instance = new MyRetrofit();
                }
            }
        }
        return instance;
    }

    public Call<ResponseByHttp> request(String baseUrl,String api, Map<String,Object> paramMap){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        // 创建 网络请求接口 的实例
        GetInterface request = retrofit.create(GetInterface.class);

        //对 发送请求 进行封装
        return request.getManyParam(api,paramMap);
    }
}
