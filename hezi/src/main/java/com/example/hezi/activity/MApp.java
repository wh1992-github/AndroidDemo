package com.example.hezi.activity;

import android.app.Application;
import android.content.Context;

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
