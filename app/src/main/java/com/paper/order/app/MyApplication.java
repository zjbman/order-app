package com.paper.order.app;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class MyApplication extends Application {
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static ActivityManager activityManager;

    @Override
    public void onCreate() {
        super.onCreate();

        getScreenWidthAndHeight();

        getActivityManager();
    }

    private void getActivityManager() {
        activityManager = ActivityManager.getInstance();
    }

    private void getScreenWidthAndHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        ScreenWidth = displayMetrics.widthPixels;//以像素为单位
        ScreenHeight = displayMetrics.heightPixels;
    }


}
