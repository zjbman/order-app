package com.paper.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.app.ActivityManager;
import com.paper.order.util.LogUtil;

import butterknife.Bind;

public class StartActivity extends BaseActivity {

    @Bind(R.id.start)
    RelativeLayout rl;

    @Override
    protected View setContentView() {
        return View.inflate(this,R.layout.activity_start,null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        startMainActivity();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onRelease() {

    }

    private void startMainActivity() {
        AlphaAnimation aa = new AlphaAnimation(0,1.0f);
        aa.setDuration(2000);
        rl.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                StartActivity.this.startActivity(new Intent(StartActivity.this,LoginActivity.class));
                ActivityManager.getInstance().removeActivity(StartActivity.this);
                LogUtil.e(this,"当前activity数 ： " + ActivityManager.getInstance().size());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
