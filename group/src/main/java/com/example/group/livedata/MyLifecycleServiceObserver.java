package com.example.group.livedata;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.util.Log;
/**
 * 封装 My Lifecycle Service Observer 相关逻辑的类。
 */

public class MyLifecycleServiceObserver implements LifecycleObserver {
    private static final String TAG = "MyLifecycleServiceObser";

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void startGetLocation() {
        Log.i(TAG, "startGetLocation: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void stopGetLocation() {
        Log.i(TAG, "stopGetLocation: ");
    }
}