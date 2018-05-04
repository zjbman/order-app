package com.paper.order.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class ToastUtil {
    public static void show(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
