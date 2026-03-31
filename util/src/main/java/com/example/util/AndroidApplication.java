package com.example.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
/**
 * 用于维护 Android Application 全局初始化逻辑的应用类。
 */

@SuppressLint("StaticFieldLeak")
public class AndroidApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fresco.initialize(this.getApplicationContext(), FrescoConfigUtil.getDefaultImagePipelineConfig(this));
    }

    public static Context getContext() {
        return sContext;
    }
}
