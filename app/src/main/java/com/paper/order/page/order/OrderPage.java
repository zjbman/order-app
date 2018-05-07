package com.paper.order.page.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.paper.order.R;
import com.paper.order.activity.StoreDetailActivity;
import com.paper.order.config.WebParam;
import com.paper.order.data.OrderData;
import com.paper.order.page.order.adapter.OrderPageAdapter;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByOrder;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class OrderPage {
    private Context mContext;
    private View mView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_delivering;
    private OrderPageAdapter adapter;

    private Toolbar toolbar;

    private List<OrderData> orderDataList;

    private final int REFRESH_SUCCESS = 0;
    private final int REFRESH_FAIL = 1;
    private final int REQUEST_SUCCESS = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_SUCCESS:
                    ToastUtil.show(mContext,"刷新成功");
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case REFRESH_FAIL:
                    ToastUtil.show(mContext,"刷新失败");
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case REQUEST_SUCCESS:
                    setAdapter();
                    setListener();
                    break;
                default:
                    break;
            }
        }
    };

    public OrderPage(Context context){
        mContext = context;
        initView();
        requestHttp();
    }


    private void initView() {
        mView = View.inflate(mContext, R.layout.page_order,null);
        rv_delivering = mView.findViewById(R.id.rv_delivering);
        swipeRefreshLayout =  mView.findViewById(R.id.swipeRefreshLayout);
        toolbar = mView.findViewById(R.id.toolbar);

         /* 设定布局中的toolbar*/
        toolbar.setTitle("点菜系统");
        toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.white));

    }

    public View getView(){
        return mView;
    }

    private void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(mContext);
        String username = user.get("username");

        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        Call<ResponseByOrder> call = request.getOrder("List.html", map);
        call.enqueue(new Callback<ResponseByOrder>() {
            @Override
            public void onResponse(Call<ResponseByOrder> call, Response<ResponseByOrder> response) {
                if(response.body() == null){
                    ToastUtil.show(mContext, "查询订单失败！ body() == null ");
                }else{
                    int code = response.body().getCode();
                    if (code == -100) {
                        ToastUtil.show(mContext, "查询订单失败！ code == -100 ");
                    } else if (code == 200) {
                        parse(response.body().getMsg());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseByOrder> call, Throwable t) {

            }
        });
    }

    private void parse(List<ResponseByOrder.Msg> msgs) {
        orderDataList = new ArrayList<>();

        for(ResponseByOrder.Msg msg : msgs){
            orderDataList.add(new OrderData(msg));
        }

        ToastUtil.show(mContext, "查询订单成功！");
        mHandler.sendEmptyMessage(REQUEST_SUCCESS);
    }


    private void setAdapter() {
        adapter = new OrderPageAdapter(mContext,orderDataList);

        rv_delivering.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rv_delivering.setAdapter(adapter);
    }


    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageAtTime(REFRESH_SUCCESS,1500);
            }
        });

        adapter.setOnAgainButtonClickListener(new OrderPageAdapter.OnAgainButtonClickListener() {
            @Override
            public void onClick(int position) {
                startStoreDetailActivity(position);
            }
        });
    }

    private void startStoreDetailActivity(int position) {
        OrderData orderData = orderDataList.get(position);
        Intent intent = new Intent(mContext, StoreDetailActivity.class);
        intent.putExtra("businessId", orderData.getBusinessId());
        intent.putExtra("businessName", orderData.getBusinessName());
        intent.putExtra("businessPicture", orderData.getBusinessPicture());
        intent.putExtra("businessAddress", orderData.getBusinessAddress());
        intent.putExtra("businessTelephone", orderData.getTelephone());
        mContext.startActivity(intent);
    }
}
