package com.paper.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paper.order.R;
import com.paper.order.adapter.GoodsAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.GoodsData;
import com.paper.order.data.GoodsList;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByGoods;
import com.paper.order.retrofit.response.ResponseByUsually;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.ToastUtil;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

@SuppressLint("ValidFragment")
public class GoodsFragment extends Fragment {
    private Context mContext;
    private View view;
    private int businessId;
    private List<GoodsData> goodsDatas;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.tv_goods_number)
    TextView tv_goods_number;
    @Bind(R.id.tv_accounts)
    TextView tv_accounts;
    @Bind(R.id.btn_add_cart)
    Button btn_add_cart;

    private GoodsAdapter adapter;
    /**
     * 这是记录当前总共选择了多少样商品及总价钱的map
     */
    private Map<Integer, Double> totalMap = new HashMap<>();
    /**
     * 这是记录当前每一个item有多少数量的map
     */
    private Map<Integer, Integer> numberMap = new HashMap<>();
    private final int REQUEST_SECCESS = 1;
    private final int NUMBER_CHANGE = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SECCESS:
                    setAdapter();
                    setListener();
                    break;

                case NUMBER_CHANGE:
                    Map<Integer, Double> map = (Map<Integer, Double>) msg.obj;
                    /* 每次传递过来的map集合中有且只有一对*/
                    Set<Integer> keySet = map.keySet();
                    int position = keySet.iterator().next();

                    /* 如果对应的value为0，则说明没有选择该商品，那么就要移除该key-value*/
                    Double vaule = (map.get(position));
                    if (vaule <= 0) {
                        //移除
                        totalMap.remove(position);

                    } else {
                        //添加
                        /* 不管是原来商品基本上的增加还是减少，或者是添加新的商品，都需要往该集合中覆盖(新增)*/
                        totalMap.put(position, map.get(position));
                    }

                    Double totalPrice = 0.0;
                    Set<Integer> set = totalMap.keySet();
                    if (set != null) {
                        for (Integer p : set) {
                            totalPrice = totalPrice + totalMap.get(p);
                        }

                        tv_accounts.setText(totalPrice + "");
                        tv_goods_number.setText(set.size() + "");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("ValidFragment")
    public GoodsFragment(Context context, int businessId) {
        mContext = context;
        this.businessId = businessId;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods, null);
        ButterKnife.bind(this, view);
        requestHttp();
        return view;
    }

    /**
     * 请求服务器，根据商家id获取相应信息
     */
    private void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("id", businessId);
        Call<ResponseByGoods> call = request.getGoods("Get.html", map);
        call.enqueue(new Callback<ResponseByGoods>() {
            @Override
            public void onResponse(Call<ResponseByGoods> call, Response<ResponseByGoods> response) {
                if (response.body() != null) {
                    parse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseByGoods> call, Throwable t) {

            }
        });
    }

    private void parse(ResponseByGoods body) {
        List<ResponseByGoods.Msg> msgs = body.getMsg();
        goodsDatas = new ArrayList<>();
        for (ResponseByGoods.Msg msg : msgs) {
            goodsDatas.add(new GoodsData(msg));
        }

        handler.sendEmptyMessage(REQUEST_SECCESS);
    }

    private void setAdapter() {
        if (goodsDatas != null && goodsDatas.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            adapter = new GoodsAdapter(mContext, goodsDatas, numberMap);
            recyclerView.setAdapter(adapter);
        }
    }

    private void setListener() {
        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<Integer> keys = numberMap.keySet();
                if (keys == null || keys.size() <= 0) {
                    ToastUtil.show(mContext, "未选择任何商品！");
                    return;
                }

                requestHttpForAddCart(keys);
            }
        });

        if (adapter != null) {
            adapter.setOnBtnAddClickListener(new GoodsAdapter.OnBtnAddClickListener() {
                @Override
                public void onClick(int position, TextView tv_number, Double price) {
                    boolean containsKey = numberMap.containsKey(position);
                    if (!containsKey) {
                        /* 如果numberMap中没有包含该key，则说明一定是第一次点击，value为1*/
                        numberMap.put(position, 1);
                    } else {
                        /* 包含了该key，则value累加*/
                        numberMap.put(position, numberMap.get(position) + 1);
                    }

                    /* 调用方法，通知适配器相关数据已经发生了改变*/
                    adapter.notify(numberMap);

                    String numberStr = tv_number.getText().toString();
                    int number = Integer.parseInt(numberStr);
                    tv_number.setText((number + 1) + "");

                    Map<Integer, Double> map = new HashMap<>();
                    map.put(position, price * (number + 1));
                    Message message = Message.obtain();
                    message.what = NUMBER_CHANGE;
                    message.obj = map;
                    handler.sendMessage(message);
                }
            });

            adapter.setOnBtnDecreaseClickListener(new GoodsAdapter.OnBtnDecreaseClickListener() {
                @Override
                public void onClick(int position, TextView tv_number, Double price) {
                    boolean containsKey = numberMap.containsKey(position);
                    if (!containsKey) {
                        /* 如果numberMap中没有包含该key，则说明一定是第一次点击，*/
//                        numberMap.put(position,0);//增加了这句，会出现负数
                    } else {
                        /* 包含了该key，则value累减*/
                        numberMap.put(position, numberMap.get(position) - 1);

                        /* 当value减至0的时候，则移除该key-value*/
                        if (numberMap.get(position) <= 0) {
                            numberMap.remove(position);
                        }
                    }

                    /* 调用方法，通知适配器相关数据已经发生了改变*/
                    adapter.notify(numberMap);

                    String numberStr = tv_number.getText().toString();
                    int number = Integer.parseInt(numberStr);

                    Map<Integer, Double> map = new HashMap<>();
                    Message message = Message.obtain();
                    message.what = NUMBER_CHANGE;
                    if (number <= 0) {
                        tv_number.setText("0");

                        map.put(position, 0.0);
                        message.obj = map;
                        handler.sendMessage(message);
                    } else {
                        tv_number.setText((number - 1) + "");

                        map.put(position, price * (number - 1));
                        message.obj = map;
                        handler.sendMessage(message);
                    }
                }
            });
        }

    }

    /**
     * 请求服务器 加入购物车
     */
    private void requestHttpForAddCart(Set<Integer> keys) {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(mContext);
        String username = user.get("username");

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer key : keys) {
            Map<String, Object> goodsMap = new HashMap<>();
            GoodsData goodsData = goodsDatas.get(key);
            Integer goodsId = goodsData.getId();
            Integer goodsNumber = numberMap.get(key);
            goodsMap.put("goodsId", goodsId);
            goodsMap.put("goodsNumber", goodsNumber);
            list.add(goodsMap);
        }

        GoodsList goodsList = new GoodsList();
        goodsList.setGoodsList(list);

        /* 封装成json字符串传递过去！！！！！！，不然不管是get还是post都接收不了对象*/
        Gson gson = new Gson();
        String s = gson.toJson(goodsList);

        map.put("username", username);
        map.put("businessId", businessId);
        map.put("goodsList", s);

        Call<ResponseByUsually> call = request.addCartPost(map);
        call.enqueue(new Callback<ResponseByUsually>() {
            @Override
            public void onResponse(Call<ResponseByUsually> call, Response<ResponseByUsually> response) {
                if(response.body() == null){
                    ToastUtil.show(mContext, "添加购物车失败! body == null");
                }else {
                    int code = response.body().getCode();
                    if (code == 200) {
                        ToastUtil.show(mContext, "成功添加购物车! 快去购物车查看吧");
                    } else if (code == -100) {
                        ToastUtil.show(mContext, "添加购物车失败! code == -100");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseByUsually> call, Throwable t) {
                ToastUtil.show(mContext, "添加购物车 连接服务器失败!");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
