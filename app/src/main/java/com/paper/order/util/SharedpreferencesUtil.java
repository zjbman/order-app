package com.paper.order.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.paper.order.config.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jbandxs on 2018/5/5.
 */

public class SharedpreferencesUtil {
    private static SharedpreferencesUtil instance;

    private SharedpreferencesUtil(){}

    public static SharedpreferencesUtil getInstance(){
        if(instance == null){
            synchronized (SharedpreferencesUtil.class){
                if(instance == null){
                    instance = new SharedpreferencesUtil();
                }
            }
        }
        return instance;
    }

    public void cacheUser(Context context,String username, String password){
        SharedPreferences sp = context.getSharedPreferences(Constant.CECHA_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username",username);
        edit.putString("password",password);
        edit.commit();
    }

    public Map<String,String> getUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constant.CECHA_FILE_NAME, Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password","");
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        return map;
    }
}
