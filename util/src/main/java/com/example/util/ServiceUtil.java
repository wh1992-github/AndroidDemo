package com.example.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {
    private static final String TAG = "ServiceUtil";

    //判断系统的服务是否在后台运行
    public static boolean isServiceRunning(Context context, String StringName) { //获取进程和服务的管理器
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(1000);
        LogUtil.i(TAG, "isServiceRunning: size = " + infos.size());
        for (ActivityManager.RunningServiceInfo info : infos) {
            String className = info.service.getClassName();
            LogUtil.i(TAG, "isServiceRunning: className = " + className);
            if (StringName.equals(className)) {
                return true;
            }
        }
        return false;
    }
}