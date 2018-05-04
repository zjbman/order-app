package com.paper.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.paper.order.R;
import com.paper.order.app.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {

    @Bind(R.id.start)
    RelativeLayout rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        (((MyApplication) getApplication()).activityManager).addActivity(this);

        ButterKnife.bind(this);
        startMainActivity();
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
                StartActivity.this.startActivity(new Intent(StartActivity.this,MainActivity.class));
                (((MyApplication) getApplication()).activityManager).removeActivity(StartActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
