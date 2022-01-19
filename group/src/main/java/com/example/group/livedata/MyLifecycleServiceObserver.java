package com.example.group.livedata;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

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