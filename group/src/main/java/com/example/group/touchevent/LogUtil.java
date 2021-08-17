package com.example.group.touchevent;

import android.util.Log;

/**
 * Log.v 的输出为黑色,输出大于或等于VERBOSE日志级别的信息
 * Log.d 的输出是蓝色,输出大于或等于DEBUG日志级别的信息
 * Log.i 的输出为绿色,输出大于或等于INFO日志级别的信息
 * Log.w 的输出为橙色,输出大于或等于WARN日志级别的信息
 * Log.e 的输出为红色,仅输出ERROR日志级别的信息
 */
public final class LogUtil {
    private static final String APP_TAG = "TouchEventDemo";
    private static final boolean LOGV_ON = true;
    private static final boolean LOGD_ON = true;
    private static final boolean LOGI_ON = true;

    public static void v(String tag, String msg) {
        if (LOGV_ON) {
            Log.v(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOGD_ON) {
            Log.d(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOGI_ON) {
            Log.i(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOGI_ON) {
            Log.w(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOGI_ON) {
            Log.e(APP_TAG, tag + " -- " + msg);
        }
    }
}