package com.paper.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.paper.order.R;
import com.paper.order.activity.OrderSubmitActivity;
import com.paper.order.adapter.GoodsAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.GoodsData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByGoods;
import com.paper.order.util.ToastUtil;

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
    Button btn_compute;

    private GoodsAdapter adapter;
    private Map<Integer,Double> totalMap = new HashMap<>();
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
                    break;

                case NUMBER_CHANGE:
                    Map<Integer,Double> map = (Map<Integer,Double>) msg.obj;
                    /* 每次传递过来的map集合中有且只有一对*/
                    Set<Integer> keySet = map.keySet();
                    int position = keySet.iterator().next();

                    /* 不管是原来商品基本上的增加还是减少，或者是添加新的商品，都需要往该集合中覆盖(新增)*/
                    totalMap.put(position,map.get(position));

                    Double totalPrice = 0.0;
                    Set<Integer> set = totalMap.keySet();
                    for(Integer p : set){
                        totalPrice = totalPrice + totalMap.get(p);
                    }

                    tv_accounts.setText(totalPrice + "");
                    tv_goods_number.setText(set.size());
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
                if(response.body() != null) {
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
        setListener();
        return view;
    }

    private void setAdapter() {
        if(goodsDatas != null && goodsDatas.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            adapter = new GoodsAdapter(mContext, goodsDatas);
            recyclerView.setAdapter(adapter);
        }
    }

    private void setListener() {
        btn_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, OrderSubmitActivity.class));
            }
        });

        if(adapter != null) {
            adapter.setOnBtnAddClickListener(new GoodsAdapter.OnBtnAddClickListener() {
                @Override
                public void onClick(int position, TextView tv_number,Double price) {
                    String numberStr = tv_number.getText().toString();
                    int number = Integer.parseInt(numberStr);
                    tv_number.setText((number + 1) + "");

                    Map<Integer,Double> map = new HashMap<>();
                    map.put(position,price * (number + 1));
                    Message message = Message.obtain();
                    message.what = NUMBER_CHANGE;
                    message.obj = map;
                    handler.sendMessage(message);
                }
            });

            adapter.setOnBtnDecreaseClickListener(new GoodsAdapter.OnBtnDecreaseClickListener() {
                @Override
                public void onClick(int position, TextView tv_number,Double price) {
                    String numberStr = tv_number.getText().toString();
                    int number = Integer.parseInt(numberStr);

                    Map<Integer,Double> map = new HashMap<>();
                    Message message = Message.obtain();
                    message.what = NUMBER_CHANGE;
                    if(number <= 0){
                        tv_number.setText("0");

                        map.put(position,0.0);
                        message.obj = map;
                        handler.sendMessage(message);
                    }else{
                        tv_number.setText((number - 1) + "");

                        map.put(position,price * (number - 1));
                        message.obj = map;
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
