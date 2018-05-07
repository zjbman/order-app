package com.paper.order.page.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.paper.order.R;
import com.paper.order.page.cart.adapter.CartPageAdapter;
import com.paper.order.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class CartPage {
    private Context mContext;
    private View mView;
    private Toolbar toolbar;

    private CartPageAdapter adapter;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    private final int REFRESH_SUCCESS = 0;
    private final int REFRESH_FAIL = 1;

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

                default:
                    break;
            }
        }
    };

    public CartPage(Context context){
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

        adapter.setOnRecyclerViewItemClickListener(new CartPageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                ToastUtil.show(mContext,"" + position);
            }
        });
    }

    private void setAdapter() {
        adapter = new CartPageAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.page_cart,null);
        ButterKnife.bind(this,mView);

        toolbar = mView.findViewById(R.id.toolbar);

         /* 设定布局中的toolbar*/
        toolbar.setTitle("点菜系统");
        toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.white));
    }

    public View getView(){
        return mView;
    }
}
