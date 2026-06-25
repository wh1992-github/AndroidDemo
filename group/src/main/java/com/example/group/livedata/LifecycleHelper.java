package com.example.group.livedata;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.group.util.LogUtil;
/**
 * 提供 Lifecycle 相关工具方法的工具类。
 */

@SuppressLint("RestrictedApi")
public class LifecycleHelper implements LifecycleObserver {
    private static final String TAG = "LifecycleHelper";

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