package com.paper.order.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.paper.order.app.ActivityManager;

import butterknife.ButterKnife;

/**
 * 这是所有Activity的基类
 *
 * Created by zjbman
 * on 2018/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(bindActivity());
        ActivityManager.getInstance().addActivity(bindActivity());
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(bindActivity());
        ButterKnife.unbind(bindActivity());
        onRelease();
        super.onDestroy();
    }

    /**
     * 在这个方法里进行布局文件的初始化
     *
     * @return
     */
    protected abstract View setContentView();

    /**
     * 布局控件的绑定
     * @return
     */
    protected abstract Activity bindActivity();

    /**
     * view的初始化,同时使用butterknife进行对view的绑定
     * @return
     */
    protected abstract void initView();

    /**
     * view的监听
     */
    protected abstract void setListener();

    /**
     * 当Activity销毁时，做的一些资源的释放操作。
     */
    protected abstract void onRelease();

}
