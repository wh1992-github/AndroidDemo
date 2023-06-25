package com.example.group.activity;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.mmkv.MMKV;

import timber.log.Timber;

/**
 * create by test on 2/7/2018.
 */
public class App extends Application {
    private static final String TAG = "TimberTest -- ";
    private static final boolean BUILD_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();

        //打印日志
        ARouter.openLog();
        //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug();
        //初始化ARouter
        ARouter.init(this);

        //初始化Timber
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected boolean isLoggable(String tag, int priority) {
                //控制是否输出日志
                return BUILD_DEBUG;
            }

            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                //设置TAG
                super.log(priority, TAG + tag, message, t);
            }
        });

        //初始化MMKV
        String dir = getFilesDir().getAbsolutePath();
        String rootDir = MMKV.initialize(dir);
        Log.i(TAG, "mmkv root = " + rootDir);
    }
}
