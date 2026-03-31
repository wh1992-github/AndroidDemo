package com.example.hezi.activity;

import android.app.Application;
import android.content.Context;
/**
 * 用于维护 M App 全局初始化逻辑的应用类。
 */

public class MApp extends Application {

    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
