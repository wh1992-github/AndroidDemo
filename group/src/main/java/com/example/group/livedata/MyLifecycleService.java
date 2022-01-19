package com.example.group.livedata;

import android.arch.lifecycle.LifecycleService;

public class MyLifecycleService extends LifecycleService {
    private MyLifecycleServiceObserver mServiceObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceObserver = new MyLifecycleServiceObserver();
        getLifecycle().addObserver(mServiceObserver);
    }

}
