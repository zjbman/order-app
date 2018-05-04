package com.paper.order.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

public class CheckNetService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
       return new MyBinder();
    }


    class MyBinder extends Binder implements ICheckNet{
        private boolean hasNet;
        /**
         * 检查当前网络状态
         * @return
         */
        @Override
        public Boolean checkNet() {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(manager != null){
                NetworkInfo info = manager.getActiveNetworkInfo();
                if(info != null && info.isConnected()){
                    //当前网络是连接的
                    if(info.getState() == NetworkInfo.State.CONNECTED){
                        //当前所连接的网络可用
                        return true;
                    }
                }
            }
            return false;


//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if(manager != null){
//                        NetworkInfo info = manager.getActiveNetworkInfo();
//                        if(info != null && info.isConnected()){
//                            //当前网络是连接的
//                            if(info.getState() == NetworkInfo.State.CONNECTED){
//                                //当前所连接的网络可用
//                                hasNet = true;
//                            }
//                        }
//                    }
//                    hasNet = false;
//                }
//            }.start();
//            return hasNet;
        }
    }
}
