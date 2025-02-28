package com.example.custom.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.custom.activity.BindDelayActivity;

/**
 * Created by test on 2017/10/14.
 */
public class BindDelayService extends Service {
    private static final String TAG = "BindDelayService";
    //创建一个粘合剂对象
    private final IBinder mBinder = new LocalBinder();

    //定义一个当前服务的粘合剂,用于将该服务黏到活动页面的进程中
    public class LocalBinder extends Binder {
        public BindDelayService getService() {
            return BindDelayService.this;
        }
    }

    private void refresh(String text) {
        Log.d(TAG, text);
        BindDelayActivity.showText(text);
    }

    @Override
    public void onCreate() { //创建服务
        refresh("onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //启动服务
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() { //销毁服务
        refresh("onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { //绑定服务。返回该服务的粘合剂对象
        Log.d(TAG, "绑定服务开始旅程！");
        refresh("onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) { //重新绑定服务
        refresh("onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) { //解绑服务。返回false表示只能绑定一次,返回true表示允许多次绑定
        Log.d(TAG, "绑定服务结束旅程！");
        refresh("onUnbind");
        return true;
    }

}
