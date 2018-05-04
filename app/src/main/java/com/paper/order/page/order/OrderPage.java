package com.paper.order.page.order;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paper.order.R;
import com.paper.order.page.order.adapter.OrderPageAdapter;
import com.paper.order.util.ToastUtil;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class OrderPage {
    private Context mContext;
    private View mView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_delivering;
    private RecyclerView rv_finish;
    private OrderPageAdapter adapter;

    private final int REFRESH_SUCCESS = 0;
    private final int REFRESH_FAIL = 1;

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

                default:
                    break;
            }
        }
    };

    public OrderPage(Context context){
        mContext = context;
        initView();
        setAdapter();
        setListener();
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageAtTime(REFRESH_FAIL,1500);
            }
        });

        adapter.setOnRecyclerViewItemClickListener(new OrderPageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                ToastUtil.show(mContext,"" + position);
            }
        });
    }

    private void setAdapter() {
        adapter = new OrderPageAdapter(mContext);
        rv_delivering.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rv_finish.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rv_delivering.setAdapter(adapter);
        rv_finish.setAdapter(adapter);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.page_order,null);
        rv_delivering = mView.findViewById(R.id.rv_delivering);
        rv_finish =  mView.findViewById(R.id.rv_finish);
        swipeRefreshLayout =  mView.findViewById(R.id.swipeRefreshLayout);
    }

    public View getView(){
        return mView;
    }
}
