package com.example.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;

//元数据工具类, 用于获取清单内声明的元数据
public class MetaDataUtil {
    private static final String TAG = "MetaDataUtil";

    //获得 Application 节点下的元数据
    public static Bundle fromApplication() {
        try {
            String packageName = AndroidApplication.getContext().getPackageName();
            PackageManager packageManager = AndroidApplication.getContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return applicationInfo.metaData;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            LogUtil.e(TAG, "Exception: e = " + e.getMessage());
        }
        return null;
    }

    //获得 Activity 节点下的元数据
    public static Bundle fromActivity(Class<? extends Activity> clazz) {
        try {
            ComponentName componentName = new ComponentName(AndroidApplication.getContext(), clazz);
            PackageManager packageManager = AndroidApplication.getContext().getPackageManager();
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            return activityInfo.metaData;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            LogUtil.e(TAG, "Exception: e = " + e.getMessage());
        }
        return null;
    }

    //获得 Service 节点下的元数据
    public static Bundle fromService(Class<? extends Service> clazz) {
        try {
            ComponentName componentName = new ComponentName(AndroidApplication.getContext(), clazz);
            PackageManager packageManager = AndroidApplication.getContext().getPackageManager();
            ServiceInfo serviceInfo = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            return serviceInfo.metaData;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            LogUtil.e(TAG, "Exception: e = " + e.getMessage());
        }
        return null;
    }

    //获得 Receiver 节点下的元数据
    public static Bundle fromReceiver(Class<? extends BroadcastReceiver> clazz) {
        try {
            ComponentName componentName = new ComponentName(AndroidApplication.getContext(), clazz);
            PackageManager packageManager = AndroidApplication.getContext().getPackageManager();
            ActivityInfo activityInfo = packageManager.getReceiverInfo(componentName, PackageManager.GET_META_DATA);
            return activityInfo.metaData;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            LogUtil.e(TAG, "Exception: e = " + e.getMessage());
        }
        return null;
    }
}
