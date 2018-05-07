package com.paper.order.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 将List<Map<String, Object>>封装成一个 javabean，
 * 否则将无法将它通过get方法传递给后台接口
 * Created by Jbandxs on 2018/5/7.
 */

public class GoodsList implements Serializable{
    List<Map<String, Object>> goodsList;

    public List<Map<String, Object>> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Map<String, Object>> goodsList) {
        this.goodsList = goodsList;
    }
}
