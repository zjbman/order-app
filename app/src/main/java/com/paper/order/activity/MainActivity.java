package com.paper.order.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.page.cart.CartPage;
import com.paper.order.page.home.HomePage;
import com.paper.order.page.order.OrderPage;
import com.paper.order.page.wo.WoPage;
import com.paper.order.service.CheckNetService;
import com.paper.order.service.ICheckNet;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.radiogroup)
    RadioGroup radioGroup;
    @Bind(R.id.framelayout)
    FrameLayout frameLayout;

    private HomePage homePage;
    private OrderPage orderPage;
    private CartPage cartPage;
    private WoPage woPage;
    private View homePageView;
    private View orderPageView;
    private View cartPageView;
    private View woPageView;
    private View paperLoading;
    private View paperLoadFailByNet;

    private MyServiceConn myServiceConn;
    private ICheckNet mCheckNet;

    private final int CHECK_NET = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_NET:
                    if (mCheckNet != null) {
                        if (!mCheckNet.checkNet()) {//当前网络不可用
                            Log.e("onCheckedChanged: ", " ============ " + mCheckNet.checkNet());
                            frameLayout.removeAllViews();
                            frameLayout.addView(paperLoadFailByNet);

                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_main, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {

        startCheckNetService();
        initPage();

        homePageView = homePage.getView();
        orderPageView = orderPage.getView();
        cartPageView = cartPage.getView();
        woPageView = woPage.getView();

        //当应用初始化加载时，便显示第一个页面
        radioGroup.check(R.id.guide_home);
        paperLoading = View.inflate(this, R.layout.page_loading, null);
        frameLayout.addView(paperLoading);

        paperLoadFailByNet = View.inflate(this, R.layout.page_load_fail, null);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frameLayout.removeAllViews();
//        frameLayout.addView(homePageView);

        startWhichPage();
    }

    /**
     * 其他页面打开MainActivity时显示的page,M默认打开第一个page
     */
    private void startWhichPage() {
        int position = getIntent().getIntExtra("position", 1);
        switch (position) {
            case 1:
                frameLayout.addView(homePageView);
                radioGroup.check(R.id.guide_home);
                break;
            case 2:
                frameLayout.addView(orderPageView);
                radioGroup.check(R.id.guide_order);
                break;
            case 3:
                frameLayout.addView(cartPageView);
                radioGroup.check(R.id.guide_cart);
                break;
            case 4:
                frameLayout.addView(woPageView);
                radioGroup.check(R.id.guide_wo);
                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (mCheckNet != null) {
                    if (!mCheckNet.checkNet()) {//当前网络不可用
                        Log.e("onCheckedChanged: ", " ============ " + mCheckNet.checkNet());

                        frameLayout.removeAllViews();
                        frameLayout.addView(paperLoadFailByNet);

                        return;//当没有网络的时候就不需要再显示什么了，直接返回了

                    } else {//当前网络可用
                        if (paperLoadFailByNet.isShown()) {
                            frameLayout.removeView(paperLoadFailByNet);

                            frameLayout.addView(paperLoading);
                        }
                        if (homePageView.isShown()) {
                            frameLayout.removeView(paperLoading);
                        }
                    }
                }

                frameLayout.removeAllViews();
                //当成功加载出页面的时候，才将paperLoading给remove了
                switch (i) {
                    case R.id.guide_home:
                        frameLayout.addView(homePageView);
                        break;
                    case R.id.guide_order:
                        frameLayout.addView(orderPageView);
                        break;
                    case R.id.guide_cart:
                        frameLayout.addView(cartPageView);
                        break;
                    case R.id.guide_wo:
                        frameLayout.addView(woPageView);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initPage() {
        homePage = new HomePage(this);
        orderPage = new OrderPage(this);
        cartPage = new CartPage(this);
        woPage = new WoPage(this);
    }


    /**
     * 启动时刻检查网络状态是否可用的服务
     */
    private void startCheckNetService() {
        Intent intent = new Intent(this, CheckNetService.class);
        myServiceConn = new MyServiceConn();
        this.bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);
        this.startService(intent);
    }


    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mCheckNet = (ICheckNet) iBinder;
            mHandler.sendEmptyMessage(CHECK_NET);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    protected void onRelease() {
        if (myServiceConn != null) {
            this.unbindService(myServiceConn);
            this.stopService(new Intent(this, CheckNetService.class));
            myServiceConn = null;
        }

    }
}
