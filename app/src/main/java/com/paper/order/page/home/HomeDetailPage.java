package com.paper.order.page.home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.paper.order.R;
import com.paper.order.activity.StoreDetailActivity;
import com.paper.order.app.MyApplication;
import com.paper.order.page.home.adapter.HomeDetailPageAdapter;
import com.paper.order.util.ImageUrls;
import com.paper.order.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.CubeOutTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zjbman
 * on 2018/2/27.
 * 这是home页面的viewpager的不同页面内容
 */
public class HomeDetailPage {
    private Context mContext;
    private View view;
    private RecyclerView recyclerview;
    private View mHeaderView;
    private Banner mBanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HomeDetailPageAdapter homeDetailPageAdapter;

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

    public HomeDetailPage(Context context){
        mContext = context;
        initView();
        setAdapter();
        setListener();
    }


    private void initView() {
        view = View.inflate(mContext, R.layout.page_home_recyclerview, null);
        recyclerview = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        //渲染header布局
        mHeaderView = View.inflate(mContext, R.layout.page_home_recyclerview_banner, null);
        mBanner = mHeaderView.findViewById(R.id.banner);
        //设置banner的高度为手机屏幕的四分之一
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.ScreenHeight/4));

    }

    private void setAdapter() {
        List<String> titles = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            titles.add("我是标题" + i);
        }
        homeDetailPageAdapter = new HomeDetailPageAdapter(mContext, titles);

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
        homeDetailPageAdapter.setHeaderView(mBanner);
        recyclerview.setAdapter(homeDetailPageAdapter);

        startBanner();
    }

    private void startBanner() {
        List<String> images = Arrays.asList(ImageUrls.IMAGES);
        List<String> titles = new ArrayList<>();

        for (int i = 0;i < images.size();i++){
            titles.add("我是标题" + i);
        }

        mBanner.setImages(images)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                })
                .setBannerAnimation(CubeOutTransformer.class)//设置为方块滚动
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)//设置为有圆圈指示器带标题的形式
                .setBannerTitles(titles)
                .start();
    }

    private void setListener(){
        homeDetailPageAdapter.setOnListItemClickListener(new HomeDetailPageAdapter.OnListItemClickListener(){
            @Override
            public void onListItemClick(View view,int position){
                ToastUtil.show(mContext,"list : " + position);
                startStoreDetailActivity();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_FAIL,1500);
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtil.show(mContext,"banner : " + position);
            }
        });
    }

    private void startStoreDetailActivity() {
        mContext.startActivity(new Intent(mContext, StoreDetailActivity.class));
    }

    public View getView(){
        return view;
    }

}
