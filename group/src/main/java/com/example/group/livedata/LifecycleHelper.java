package com.example.group.livedata;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.app.AppCompatActivity;

import com.example.group.util.LogUtil;

@SuppressLint("RestrictedApi")
public class LifecycleHelper implements LifecycleObserver {
    private static final String TAG = "LifecycleHelper";
    private AppCompatActivity mActivity;

    public LifecycleHelper(AppCompatActivity activity) {
        mActivity = activity;
    }

    public void addObserver() {
        if (mActivity != null) {
            mActivity.getLifecycle().addObserver(this);
        }
    }

    public void removeObserver() {
        if (mActivity != null) {
            mActivity.getLifecycle().removeObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        LogUtil.i(TAG, "create: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        LogUtil.i(TAG, "start: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        LogUtil.i(TAG, "resume: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        LogUtil.i(TAG, "pause: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        LogUtil.i(TAG, "stop: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        LogUtil.i(TAG, "destroy: ");
    }


}