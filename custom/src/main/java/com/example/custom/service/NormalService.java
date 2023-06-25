package com.example.custom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.custom.activity.ServiceNormalActivity;

/**
 * Created by test on 2017/10/14.
 */
public class NormalService extends Service {
    private static final String TAG = "NormalService";

    private void refresh(String text) {
        Log.d(TAG, text);
        ServiceNormalActivity.showText(text);
    }

    @Override
    public void onCreate() { //创建服务
        refresh("onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //启动服务,Android2.0以上使用
        Log.d(TAG, "测试服务到此一游！");
        refresh("onStartCommand. flags=" + flags);
        return START_STICKY;
    }

    @Override
    public void onDestroy() { //销毁服务
        refresh("onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { //绑定服务。普通服务不存在绑定和解绑流程
        refresh("onBind");
        return null;
    }

    @Override
    public void onRebind(Intent intent) { //重新绑定服务
        refresh("onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) { //解绑服务
        refresh("onUnbind");
        return true;
    }

}
