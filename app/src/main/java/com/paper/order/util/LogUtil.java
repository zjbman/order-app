package com.paper.order.util;

import android.util.Log;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class LogUtil {
    private static boolean isShow = true;
    public static void e(Object TAG,String content){
        if(isShow) {
            if(TAG instanceof Class){
                Log.e(((Class) TAG).getSimpleName(), content);
            }else if(TAG instanceof String){
                Log.e((String) TAG, content);
            }else{
                Log.e(TAG.getClass().getSimpleName(), content);
            }
        }
    }
}
