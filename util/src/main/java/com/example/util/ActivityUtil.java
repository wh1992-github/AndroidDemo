package com.example.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class ActivityUtil {
    private static final String TAG = "ActivityUtil";

    //Activity 间通过隐式 Intent 的跳转,在发出 Intent 之前必须通过 resolveActivity 检查,
    //避免找不到合适的调用组件,造成 ActivityNotFoundException 的异常
    public void startActivity(Activity activity, String url, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), mimeType);
        if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.d(TAG, "ActivityNotFoundException: e = " + e.getMessage());
            }
        }
    }

    //显式跳转
    public void startActivity(Activity activity, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        activity.startActivity(intent);
    }
}
