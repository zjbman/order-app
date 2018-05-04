package com.paper.order.util;

/**
 * Created by Jbandxs on 2018/5/4.
 */

public class StringUtil {
    public static boolean isEmpty(String content){
        if(null == content || "".equals(content)){
            return true;
        }
        return false;
    }
}
