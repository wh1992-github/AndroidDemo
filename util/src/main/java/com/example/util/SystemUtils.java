
package com.example.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.UserManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 应用工具,用来判断应用状态、获取其他应用上下文、启动其他应用等操作。
 */
public class SystemUtils {
    private static final String TAG = "SystemUtils";

    /**
     * 是否已解锁
     *
     * @param context 上下文
     * @return true or false
     */
    public static boolean isUserUnlocked(Context context) {
        boolean isUserUnlocked = true;
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isUserUnlocked = context.getSystemService(UserManager.class).isUserUnlocked();
        }
        LogUtil.w(TAG, "isUserUnlocked: " + isUserUnlocked);
        return isUserUnlocked;
    }

    /**
     * 获取app version code
     *
     * @return version code
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return 0;
        }
    }

    /**
     * 获取顶端activity的包名,Android新版本需要系统应用才可判断
     *
     * @param context 上下文
     * @return String pkg或null
     * @Description 获取顶端activity的包名
     */
    public static String getTopPkg(Context context) {
        String pkg = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
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

    /**
     * 获取顶端activity的包名,Android新版本需要系统应用才可判断
     *
     * @param context 上下文
     * @return String pkg或null
     * @Description 获取顶端activity的包名
     */
    public static String getTopActivity(Context context) {
        String activity = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (null != tasks && tasks.size() > 0) {
            RunningTaskInfo info = tasks.get(0);
            if (null != info) {
                ComponentName base = info.topActivity;
                if (null != base) {
                    activity = base.getClassName();
                }
            }
        }
        return activity;
    }

    /**
     * 判断应用是否在最前端显示,Android新版本需要系统应用才可判断
     *
     * @param context     上下文
     * @param destPkgName 目标包名
     * @return boolean true为在最顶层,false为否
     * @Description: 判断activity是否在最顶层
     */
    public static boolean isTop(Context context, String destPkgName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (null != tasks && tasks.size() > 0) {
            RunningTaskInfo info = tasks.get(0);
            if (null != info) {
                ComponentName base = info.topActivity;
                if (null != base) {
                    String pkgName = base.getPackageName();
                    if (!TextUtils.isEmpty(pkgName) && pkgName.equals(destPkgName)) {
                        isRunning = true;
                    }
                }
            }
        }
        return isRunning;
    }

    /**
     * 判断应用是否有Activity活着,Android新版本需要系统应用才可判断
     *
     * @param context     上下文
     * @param destPkgName 要判断的应用
     * @return boolean true为在运行,false为已结束
     * @Description: 判断应用是否有activity在运行
     */
    public static boolean isRunning(Context context, String destPkgName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasks = am.getRunningTasks(100);
        if (null != tasks && tasks.size() > 0) {
            for (RunningTaskInfo info : tasks) {
                if (null == info) {
                    continue;
                }
                ComponentName base = info.baseActivity;
                if (null == base) {
                    continue;
                }
                String pkgName = base.getPackageName();
                if (!TextUtils.isEmpty(pkgName) && pkgName.equals(destPkgName)) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 启动某个应用
     *
     * @param context 调用者上下文
     * @param pkgName 目标应用包名
     * @return true或false
     */
    public static boolean startApplication(Context context, String pkgName) {
        if (null == context || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        return startActivity(context, getLauncherIntent(context, pkgName));
    }

    public static Intent getLauncherIntent(Context context, String pkgName) {
        Intent intent = null;
        PackageManager packageManager = context.getPackageManager();
        if (null != packageManager) {
            intent = packageManager.getLaunchIntentForPackage(pkgName);
        }
        return intent;
    }

    private static boolean startActivity(Context context, Intent intent) {
        if (null != intent) {
            try {
                intent.putExtra("from", context.getPackageName());
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
            } catch (SecurityException e) {
            }
        }
        return false;
    }

    /**
     * 强制结束指定应用
     *
     * @see <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
     * @see <uses-permission android:name="android.permission.FOCE_STOP_PACKAGES" />
     */
    public static void forceStopPackage(Context context, String pkgName) {
        LogUtil.i(TAG, "forceStopPackage: " + pkgName);
        try {
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, pkgName);
        } catch (IllegalArgumentException e) {
            LogUtil.e(TAG, "forceStopPackage() IllegalArgumentException: " + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "forceStopPackage() Exception: " + e.getMessage());
        }
    }

    /**
     * 获取指定应用的上下文
     *
     * @param context     调用者的上下文
     * @param destPkgName 目标应用的包名
     * @return Context 目标应用上下文,找不到包名时返回null。
     */
    public static Context getOtherContext(Context context, String destPkgName) {
        Context otherContext = null;
        try {
            otherContext = context.createPackageContext(destPkgName,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "NameNotFoundException: " + e.getMessage());
        } catch (SecurityException e) {
            LogUtil.e(TAG, "FileNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
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
