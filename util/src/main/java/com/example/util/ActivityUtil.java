package com.example.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Activity 间通过隐式 Intent 的跳转,在发出 Intent 之前必须通过 resolveActivity 检查,
 * 避免找不到合适的调用组件,造成 ActivityNotFoundException 的异常
 */
public class ActivityUtil {
    private static final String TAG = "ActivityUtil";

    public void startActivityA(Activity activity, String url, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), mimeType);
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.d(TAG, "ActivityNotFoundException: e = " + e.getMessage());
            }
        }
    }

    public void startActivityB(Activity activity, String url, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), mimeType);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, 0);
        if (infos != null && infos.size() > 0) {
            for (ResolveInfo info : infos) {
                if (info != null && info.activityInfo != null) {
                    ComponentName componentName = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setComponent(componentName);
                    break;
                }
            }
        }
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
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
