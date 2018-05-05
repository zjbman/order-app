package com.paper.order.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.app.ActivityManager;
import com.paper.order.app.MyApplication;
import com.paper.order.fragment.CommentFragment;
import com.paper.order.fragment.GoodsFragment;
import com.paper.order.fragment.StoreInfoFragment;
import com.paper.order.page.home.indicator.ScaleTransitionPagerTitleView;
import com.paper.order.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.paper.order.R.id.viewpager;

public class StoreDetailActivity extends FragmentActivity {
    @Bind(R.id.iv_icon)
    ImageView iv_icon;
    @Bind(R.id.tv_business_name)
    TextView tv_business_name;

    @Bind(viewpager)
    ViewPager viewPager;
    @Bind(R.id.magicindicator)
    MagicIndicator magicIndicator;

    private List<String> indicators;
    private List<Fragment> fragments;

    /** 当前商家的id*/
    private int businessId;
    private String businessName;
    private String businessPicture;
    private String businessAddress;
    private String businessTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ActivityManager.getInstance().addActivity(this);

        businessId = getIntent().getIntExtra("businessId", 1);
        businessName = getIntent().getStringExtra("businessName");
        businessPicture = getIntent().getStringExtra("businessPicture");
        businessAddress = getIntent().getStringExtra("businessAddress");
        businessTelephone = getIntent().getStringExtra("businessTelephone");

        initView();
        initData();
        initFragment();
        initMagicIndicator();
        setAdapter();
        setListener();
    }

    private void initView() {
        ButterKnife.bind(this);
        tv_business_name.setText(businessName);
        Glide.with(this).load(businessPicture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon)//加载时的图片
                .error(R.mipmap.icon)  //加载错误时的图片
                .override(800, 800)
                .into(iv_icon);
    }


    private void initData() {
        indicators = new ArrayList<>();
        indicators.add("点菜");
        indicators.add("评论");
        indicators.add("商家");
    }

    private void setListener() {
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new GoodsFragment(this,businessId));
        fragments.add(new CommentFragment(this,businessId));
        fragments.add(new StoreInfoFragment(this,businessId,businessAddress,businessTelephone));
    }

    private void setAdapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//让标题均分
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return indicators == null ? 0 : indicators.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(indicators.get(index));
                simplePagerTitleView.setTextSize(18);

                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#f57c00"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
