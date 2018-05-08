package com.paper.order.page.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.activity.LoginActivity;
import com.paper.order.activity.MainActivity;
import com.paper.order.activity.OrderSubmitActivity;
import com.paper.order.adapter.GoodsAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.CartData;
import com.paper.order.data.CartOneData;
import com.paper.order.page.cart.adapter.CartPageAdapter;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByCart;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
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
 * on 2018/2/28.
 */

public class CartPage {
    private Context mContext;
    private View mView;
    private Toolbar toolbar;

    private CartPageAdapter adapter;
    private List<CartData> cartDataList;

    /**
     * 这是传给适配器的数据源
     */
    private List<CartOneData> cartOneDataList;

    /**
     * 这是记录当前总共选择了多少样商品及总价钱的map
     */
    private Map<Integer, Double> totalMap = new HashMap<>();

    /**
     * 这是记录当前每一个item有多少数量的map
     */
    private Map<Integer, Integer> numberMap = new HashMap<>();

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.tv_accounts)
    TextView tv_accounts;
    @Bind(R.id.tv_goods_number)
    TextView tv_goods_number;
    @Bind(R.id.tv_compute)
    TextView tv_compute;

    private final int REFRESH_SUCCESS = 0;
    private final int REFRESH_FAIL = 1;
    private final int REQUEST_SUCCESS = 2;
    private final int NUMBER_CHANGE = 3;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SUCCESS:
                    ToastUtil.show(mContext, "刷新成功");
//                    setAdapter(true);
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case REFRESH_FAIL:
                    ToastUtil.show(mContext, "刷新失败");
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case REQUEST_SUCCESS:
                    setAdapter(false);
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

    public CartPage(Context context) {
        mContext = context;
        initView();
        requestHttp(false);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.page_cart, null);
        ButterKnife.bind(this, mView);

        toolbar = mView.findViewById(R.id.toolbar);

         /* 设定布局中的toolbar*/
        toolbar.setTitle("点菜系统");
        toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.white));
    }

    public View getView() {
        return mView;
    }


    private void requestHttp(final boolean isRefresh) {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(mContext);
        String username = user.get("username");

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        Call<ResponseByCart> call = request.getCart("List.html", map);
        call.enqueue(new Callback<ResponseByCart>() {
            @Override
            public void onResponse(Call<ResponseByCart> call, Response<ResponseByCart> response) {
                if (response.body() == null) {
                    ToastUtil.show(mContext, "请求购物车信息失败！ body() == null");
                } else {
                    int code = response.body().getCode();
                    if (code == -100) {
                        ToastUtil.show(mContext, "请求购物车信息失败！ code == -100");
                    } else if (code == 200) {
                        parse(response.body().getMsg(),isRefresh);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseByCart> call, Throwable t) {

            }
        });
    }

    private void parse(List<ResponseByCart.Msg> msgs,boolean isRefresh) {
        cartDataList = new ArrayList<>();
        for (ResponseByCart.Msg msg : msgs) {
            cartDataList.add(new CartData(msg));
        }

        if(isRefresh){
            mHandler.sendEmptyMessage(REFRESH_SUCCESS);
        }else{
            mHandler.sendEmptyMessage(REQUEST_SUCCESS);
        }
    }


    private void setAdapter(boolean isRefresh) {
        if (cartDataList != null && cartDataList.size() > 0) {
            parseCartDataListToCartOneDataList();
            if(isRefresh){
                adapter.notifyCartDataList(cartOneDataList);
                adapter.notifyDataSetChanged();
            }else{
            /* 这里的numberMap 与商品列表中的numberMap是不同的，它是有初始值的，所以应该先给它赋值*/
                //如何赋值？？ 不想写了，累， 就先这样吧

                adapter = new CartPageAdapter(mContext, cartOneDataList, numberMap);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    /**
     * 将cartDataList 解析成cartOneDataList ,作为适配器的数据源
     */
    private void parseCartDataListToCartOneDataList() {
        cartOneDataList = new ArrayList<>();
        for (CartData cartData : cartDataList) {
            List<CartData.GoodsData> goodsDataList = cartData.getGoodsDataList();
            for (CartData.GoodsData data : goodsDataList) {
                CartOneData cartOneData = new CartOneData();
                cartOneData.setBusinessName(cartData.getBusinessName());
                cartOneData.setBusinessId(cartData.getBusinessId());
                cartOneData.setId(cartData.getId());
                cartOneData.setName(cartData.getName());
                cartOneData.setUsername(cartData.getUsername());

                cartOneData.setDetails(data.getDetails());
                cartOneData.setGoodsName(data.getGoodsName());
                cartOneData.setGoodsId(data.getId());
                cartOneData.setNumber(data.getNumber());
                cartOneData.setPicture(data.getPicture());
                cartOneData.setPrice(data.getPrice());

                cartOneDataList.add(cartOneData);
            }
        }
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                requestHttp(true);
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("position",3);
                mHandler.sendEmptyMessage(REFRESH_SUCCESS);
                mContext.startActivity(intent);
            }
        });


        if (adapter != null) {
            adapter.setOnBtnAddClickListener(new CartPageAdapter.OnBtnAddClickListener() {
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
                    mHandler.sendMessage(message);
                }
            });

            adapter.setOnBtnDecreaseClickListener(new CartPageAdapter.OnBtnDecreaseClickListener() {
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
                        mHandler.sendMessage(message);
                    } else {
                        tv_number.setText((number - 1) + "");

                        map.put(position, price * (number - 1));
                        message.obj = map;
                        mHandler.sendMessage(message);
                    }
                }
            });

            /** 移除按钮 不想做了，下面做法是错的*/
            adapter.setOnDeleteClickListener(new CartPageAdapter.OnDeleteClickListener() {
                @Override
                public void onClick(int position,TextView tv_number) {
                    cartOneDataList.remove(position);
                    tv_number.setText("0");

                    boolean containsKey = numberMap.containsKey(position);
                    if(containsKey){
                        numberMap.put(position,0);
                        numberMap.remove(position);
                    }

                    /* 调用方法，通知适配器相关数据已经发生了改变*/
                    adapter.notify(numberMap);
                    adapter.notifyCartDataList(cartOneDataList);
                }
            });


            /** 结算*/
            tv_compute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numberMap.keySet().size() <= 0) {
                        ToastUtil.show(mContext, "未选择任何商品");
                    } else {
                        startOrderSumbitActivity();
                    }
                }
            });
        }

    }

    private void startOrderSumbitActivity() {
        Set<Integer> keySet = numberMap.keySet();
        JSONArray jsonArray = new JSONArray();

        for(Integer key : keySet){
            JSONObject jsonObject = new JSONObject();
            CartOneData cartOneData = cartOneDataList.get(key);

            Integer businessId = cartOneData.getBusinessId();
            Integer goodsId = cartOneData.getGoodsId();
            String goodsName = cartOneData.getGoodsName();
            Integer goodsNumber = numberMap.get(key);
            String businessName = cartOneData.getBusinessName();
            String businessPicture = cartOneData.getPicture();
            Double price = cartOneData.getPrice();
            try {
                jsonObject.put("businessId",businessId);
                jsonObject.put("goodsId",goodsId);
                jsonObject.put("goodsName",goodsName);
                jsonObject.put("goodsNumber",goodsNumber);
                jsonObject.put("businessName",businessName);
                jsonObject.put("businessPicture",businessPicture);
                jsonObject.put("price",price);

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(mContext, OrderSubmitActivity.class);
        intent.putExtra("goodsList",jsonArray.toString());
        mContext.startActivity(intent);
    }

}
