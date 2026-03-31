package com.example.group.livedata;

import androidx.lifecycle.LifecycleService;
/**
 * 用于处理 My Lifecycle 相关后台工作的 Service。
 */

public class MyLifecycleService extends LifecycleService {
    private MyLifecycleServiceObserver mServiceObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceObserver = new MyLifecycleServiceObserver();
        getLifecycle().addObserver(mServiceObserver);
    }

}
