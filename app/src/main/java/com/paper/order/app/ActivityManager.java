package com.paper.order.app;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class ActivityManager {

    private static ActivityManager activityManager;
    private Stack<Activity> stack;

    private ActivityManager(){
        stack = new Stack<>();
    }

    public static ActivityManager getInstance(){
        if(activityManager == null) {
            synchronized (ActivityManager.class) {
                if(activityManager == null){
                    activityManager = new ActivityManager();
                }
            }
        }
        return activityManager;
    }

    public void addActivity(Activity activity){
        if(activity == null){
            return;
        }
        stack.add(activity);
    }

    public void removeActivity(Activity activity){
        if(activity == null){
            return;
        }
        stack.remove(activity);//这里是有问题的，假设该activity不存在stack中呢，是不是就报异常了
        activity.finish();
    }

    public void removeAllActivitys(){
        for (int i = stack.size() - 1;i >= 0;i--){
            removeActivity(stack.get(i));
        }
    }

    public int Size(){
        return stack.size();
    }
}
