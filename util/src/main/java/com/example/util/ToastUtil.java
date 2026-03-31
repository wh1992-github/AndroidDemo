package com.example.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
/**
 * 提供 Toast 相关工具方法的工具类。
 */

@SuppressLint("StaticFieldLeak")
public class ToastUtil {
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private static Toast sToast;

    public static void show(final Context context, final String msg) {
        if (sToast != null) {
            sToast.cancel();
        }
        mHandler.postDelayed(() -> {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            sToast.show();
        }, 100);
    }
}
