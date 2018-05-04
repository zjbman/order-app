package com.paper.order.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.paper.order.R;
import com.paper.order.app.MyApplication;
import com.paper.order.page.cart.CartPage;
import com.paper.order.page.home.HomePage;
import com.paper.order.page.order.OrderPage;
import com.paper.order.page.wo.WoPage;
import com.paper.order.service.CheckNetService;
import com.paper.order.service.ICheckNet;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        (((MyApplication) getApplication()).activityManager).addActivity(this);

        startCheckNetService();
        initPage();
        initView();
        listener();
    }

    private void initPage() {
        homePage = new HomePage(this);
        orderPage = new OrderPage(this);
        cartPage = new CartPage(this);
        woPage = new WoPage(this);
    }

    private void initView() {
        ButterKnife.bind(this);

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
        frameLayout.addView(homePageView);
    }

    private void listener() {
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

    /**
     * 启动时刻检查网络状态是否可用的服务
     */
    private void startCheckNetService() {
        Intent intent = new Intent(this, CheckNetService.class);
        myServiceConn = new MyServiceConn();
        this.bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);
        this.startService(intent);
    }

    @Override
    protected void onDestroy() {
        if (myServiceConn != null) {
            this.unbindService(myServiceConn);
            this.stopService(new Intent(this, CheckNetService.class));
            myServiceConn = null;
        }

        ButterKnife.unbind(this);
        (((MyApplication) getApplication()).activityManager).removeActivity(this);
        super.onDestroy();
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
}
