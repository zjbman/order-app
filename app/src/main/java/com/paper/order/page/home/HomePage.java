package com.paper.order.page.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.page.home.indicator.ScaleTransitionPagerTitleView;

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

/**
 * Created by zjbman
 * on 2018/2/27.
 */

public class HomePage {

    private MagicIndicator indicator;
    private ViewPager viewpager;

    private Context mContext;
    private View view;
    private List<String> titleList;

    public HomePage(Context context) {
        mContext = context;
        initView();
        setAdapter();
        initMagicIndicator();
    }

    private void initView() {
        view = View.inflate(mContext, R.layout.page_home, null);
        indicator = view.findViewById(R.id.home_indicator);
        viewpager = view.findViewById(R.id.viewpager);

        initViewPagerTitle();

    }

    /**
     * 给当前的ViewPager设置适配器
     */
    private void setAdapter() {

        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                HomeDetailPage homeDetailPage = new HomeDetailPage(mContext,position);
                View view = homeDetailPage.getView();
                container.addView(view);
                return view;
            }
        });
    }


    /**
     * 初始化ViewPager的标题
     */
    private void initViewPagerTitle(){
        titleList = new ArrayList<>();
        titleList.add("推荐");
        titleList.add("美食");
        titleList.add("更多");
    }

    public View getView() {
        return view;
    }

    private void initMagicIndicator() {
        indicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);//让标题均分
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titleList.get(index));
                simplePagerTitleView.setTextSize(18);

                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
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
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewpager);
    }
}
