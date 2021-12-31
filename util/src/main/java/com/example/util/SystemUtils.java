package com.example.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.os.UserManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 应用工具,用来判断应用状态、获取其他应用上下文、启动其他应用等操作。
 */
@SuppressWarnings("deprecation")
public class SystemUtils {
    private static final String TAG = "SystemUtils";

    //是否已解锁
    public static boolean isUserUnlocked(Context context) {
        boolean isUserUnlocked = true;
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isUserUnlocked = context.getSystemService(UserManager.class).isUserUnlocked();
        }
        LogUtil.w(TAG, "isUserUnlocked = " + isUserUnlocked);
        return isUserUnlocked;
    }

    //获取app version code
    public static void getVersionInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            LogUtil.i(TAG, "getVersionInfo: name = " + info.versionName + ", code = " + info.versionCode);
        } catch (Exception e) {
            LogUtil.e(TAG, "getVersionInfo: e = " + e.getMessage());
        }
    }

    //获取顶端activity的包名,Android新版本需要系统应用才可判断
    public static String getTopPkg(Context context) {
        String pkg = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (null != tasks && tasks.size() > 0) {
            RunningTaskInfo info = tasks.get(0);
            if (null != info) {
                ComponentName base = info.topActivity;
                if (null != base) {
                    pkg = base.getPackageName();
                }
            }
        }
        return pkg;
    }

    //获取顶端activity的包名,Android新版本需要系统应用才可判断
    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
        LogUtil.i(TAG, "getTopActivity: activityName = " + activityName);
        return activityName;
    }

    //判断应用是否在最前端显示,Android新版本需要系统应用才可判断
    public static boolean isTop(Context context, String destPkgName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        LogUtil.i(TAG, "isTop: packageName = " + packageName);
        if (!TextUtils.isEmpty(packageName) && packageName.equals(destPkgName)) {
            isRunning = true;
        }
        return isRunning;
    }

    //判断应用是否有Activity活着,Android新版本需要系统应用才可判断
    public static boolean isRunning(Context context, String destPkgName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(100);
        for (RunningTaskInfo info : tasks) {
            ComponentName base = info.baseActivity;
            String pkgName = base.getPackageName();
            if (!TextUtils.isEmpty(pkgName) && pkgName.equals(destPkgName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    //启动某个应用
    public static boolean startApplication(Context context, String pkgName) {
        if (context == null || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        return startActivity(context, getLauncherIntent(context, pkgName));
    }

    //获取某个应用的LauncherIntent
    public static Intent getLauncherIntent(Context context, String pkgName) {
        Intent intent = null;
        PackageManager packageManager = context.getPackageManager();
        if (null != packageManager) {
            intent = packageManager.getLaunchIntentForPackage(pkgName);
        }
        return intent;
    }

    //获取某个应用的LauncherIntent
    public static Intent getLauncherIntent2(Context context, String pkgName) {
        LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        List<LauncherActivityInfo> apps = launcherApps.getActivityList(pkgName, Process.myUserHandle());
        ComponentName componentName = apps.get(0).getComponentName();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    //启动某个指定的Activity
    private static boolean startActivity(Context context, Intent intent) {
        if (intent != null) {
            try {
                intent.putExtra("from", context.getPackageName());
                context.startActivity(intent);
                return true;
            } catch (Exception e) {
                LogUtil.i(TAG, "startActivity: e = " + e.getMessage());
            }
        }
        return false;
    }

    //强制结束指定应用
    public static void forceStopPackage(Context context, String pkgName) {
        LogUtil.i(TAG, "forceStopPackage: " + pkgName);
        try {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, pkgName);
        } catch (IllegalArgumentException e) {
            LogUtil.e(TAG, "forceStopPackage() IllegalArgumentException: " + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "forceStopPackage() Exception: " + e.getMessage());
        }
    }

    //获取指定应用的上下文
    public static Context getOtherContext(Context context, String destPkgName) {
        Context otherContext = null;
        try {
            otherContext = context.createPackageContext(destPkgName, Context.CONTEXT_IGNORE_SECURITY);
        } catch (Exception e) {
            LogUtil.e(TAG, "getOtherContext: e = " + e.getMessage());
        }
        return otherContext;
    }

    //获取进程名
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
