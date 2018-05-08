package com.paper.order.page.home;

import android.annotation.SuppressLint;
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
import com.paper.order.config.WebParam;
import com.paper.order.data.BusinessData;
import com.paper.order.page.home.adapter.HomeDetailPageAdapter;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByBusiness;
import com.paper.order.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.CubeOutTransformer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zjbman
 * on 2018/2/27.
 * 这是home页面的viewpager的不同页面内容
 */
public class HomeDetailPage {
    private Context mContext;
    /**
     * 当前viewPager的Index
     */
    private int position;
    private View view;
    private RecyclerView recyclerview;
    private View mHeaderView;
    private Banner mBanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HomeDetailPageAdapter homeDetailPageAdapter;

    private List<BusinessData> businessDatas;

    private final int REFRESH_SUCCESS = 0;
    private final int REFRESH_FAIL = 1;
    private final int REQUEST_SUCCESS = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SUCCESS:
                    ToastUtil.show(mContext, "刷新成功");
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case REFRESH_FAIL:
                    ToastUtil.show(mContext, "刷新失败");
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

    public HomeDetailPage(Context context, int position) {
        mContext = context;
        this.position = position;
        initView();
    }


    private void initView() {
        view = View.inflate(mContext, R.layout.page_home_recyclerview, null);
        recyclerview = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        //渲染header布局
        mHeaderView = View.inflate(mContext, R.layout.page_home_recyclerview_banner, null);
        mBanner = mHeaderView.findViewById(R.id.banner);
        //设置banner的高度为手机屏幕的四分之一
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.ScreenHeight / 4));

        /* 请求服务器，返回所有商家信息*/
        requestHttp(false);
    }

    private void setAdapter() {
        if (businessDatas != null && businessDatas.size() > 0) {
            //成功请求到服务器的商家数据
            homeDetailPageAdapter = new HomeDetailPageAdapter(mContext, businessDatas);

            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            homeDetailPageAdapter.setHeaderView(mBanner);
            recyclerview.setAdapter(homeDetailPageAdapter);

            startBanner();
        }
    }


    private void requestHttp(final boolean isRefresh) {
        Map<String, Object> map = new HashMap<>();
        map.put("index", position);
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Call<ResponseByBusiness> call = request.getManyParam2("List.html", map);

        call.enqueue(new Callback<ResponseByBusiness>() {
            @Override
            public void onResponse(Call<ResponseByBusiness> call, Response<ResponseByBusiness> response) {
                if (response.body() != null) {
                    parse(response.body(),isRefresh);
                }
            }

            @Override
            public void onFailure(Call<ResponseByBusiness> call, Throwable t) {
                ToastUtil.show(mContext, "请求服务器失败 ");
            }
        });
    }

    /**
     * 解析服务器返回的数据
     *
     * @param body
     * @return
     */
    private void parse(ResponseByBusiness body,boolean isRefresh) {
        List<ResponseByBusiness.Msg> msgs = body.getMsg();
        businessDatas = new ArrayList<>();

        for (ResponseByBusiness.Msg msg : msgs) {
            BusinessData businessData = new BusinessData();
            businessData.setId(msg.getId());
            businessData.setBusinessName(msg.getBusinessName());
            businessData.setPicture(msg.getPicture());
            businessData.setAddress(msg.getAddress());
            businessData.setTelephone(msg.getTelephone());
            businessDatas.add(businessData);
        }

        /* 解析完成服务器的数据的时候 就发送消息，handler处理适配器和监听点击事件*/
        mHandler.sendEmptyMessage(REQUEST_SUCCESS);

        if(isRefresh){
            mHandler.sendEmptyMessage(REFRESH_SUCCESS);
        }
    }

    private void startBanner() {
        List<String> images = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        String s1 = "假期不打烊，舒适不打折";
        String s2 = "美食红包待领取，人气甜品精选";
        String s3 = "不能错过的小众饮料";
        titles.add(s1);
        titles.add(s2);
        titles.add(s3);

        String pic1 = WebParam.PIC_BASE_URL + "/pic/p21.jpg";
        String pic2 = WebParam.PIC_BASE_URL + "/pic/p180.jpg";
        String pic3 = WebParam.PIC_BASE_URL + "/pic/p54.jpg";
        images.add(pic1);
        images.add(pic2);
        images.add(pic3);

        mBanner.setImages(images)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                })
                .setBannerAnimation(CubeOutTransformer.class)//设置为方块滚动
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)//设置为有圆圈指示器带标题的形式
                .setBannerTitles(titles)
                .start();
    }

    private void setListener() {
        if (homeDetailPageAdapter != null) {
            homeDetailPageAdapter.setOnListItemClickListener(new HomeDetailPageAdapter.OnListItemClickListener() {
                @Override
                public void onListItemClick(View view, int index) {
                    BusinessData businessData = businessDatas.get(index);

                    if (position == 0) {
                        //当前为 “推荐”
                        startStoreDetailActivity(index + 1, businessData.getBusinessName(), WebParam.PIC_BASE_URL + businessData.getPicture(), businessData.getAddress(), businessData.getTelephone());

                    } else if (position == 1) {
                        //当前为 “美食”
                        startStoreDetailActivity(index + 8, businessData.getBusinessName(), WebParam.PIC_BASE_URL + businessData.getPicture(), businessData.getAddress(), businessData.getTelephone());

                    } else if (position == 2) {
                        //当前为 “更多”
                        startStoreDetailActivity(index + 15, businessData.getBusinessName(), WebParam.PIC_BASE_URL + businessData.getPicture(), businessData.getAddress(), businessData.getTelephone());

                    }
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestHttp(true);
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position) {
                    case 0:
                        startStoreDetailActivity(8, "新品味", WebParam.PIC_BASE_URL + "/pic/8.jpg", "嘉应学院嘉园路", "18846554886");
                        break;
                    case 1:
                        startStoreDetailActivity(3, "重庆鸡公煲", WebParam.PIC_BASE_URL + "/pic/3.jpg", "嘉应学院嘉园路", "15121354897");
                        break;
                    case 2:
                        startStoreDetailActivity(15, "家常便饭", WebParam.PIC_BASE_URL + "/pic/15.jpg", "嘉应学院西门", "18855446225");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void startStoreDetailActivity(int businessId, String businessName, String businessPicture,
                                          String businessAddress, String businessTelephone) {
        Intent intent = new Intent(mContext, StoreDetailActivity.class);
        intent.putExtra("businessId", businessId);
        intent.putExtra("businessName", businessName);
        intent.putExtra("businessPicture", businessPicture);
        intent.putExtra("businessAddress", businessAddress);
        intent.putExtra("businessTelephone", businessTelephone);
        mContext.startActivity(intent);
    }

    public View getView() {
        return view;
    }
}
