package com.example.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.List;

public class AppInfoUtil {
    private static final String TAG = "AppInfo_";
    private Context mContext;
    private PackageManager mPackageManager;
    private PackageInfo mPackageInfo;
    private ApplicationInfo mApplicationInfo;

    /**
     * 初始化AppInfo对象。
     *
     * @param context 应用上下文,AppInfo自动切换为全局上下文。
     */
    public AppInfoUtil(Context context) {
        mContext = context.getApplicationContext();
        mPackageManager = mContext.getPackageManager();
        mApplicationInfo = mContext.getApplicationInfo();
        try {
            mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException e = " + e.getMessage());
        }
    }

    //获取应用的应用名称
    public String getAppName() {
        return mPackageManager.getApplicationLabel(mApplicationInfo).toString();
    }

    //获取应用的包名
    public String getPackageName() {
        return mApplicationInfo.packageName;
    }

    //获取应用的版本号
    public String getVersionName() {
        if (null == mPackageInfo) {
            return null;
        }
        return mPackageInfo.versionName;
    }

    //获取应用的内部版本号
    public int getVersionCode() {
        if (null == mPackageInfo) {
            return -1;
        }
        return mPackageInfo.versionCode;
    }

    //获取应用UID,若应用不存在,获取为空
    public String getUid(String pkgName) {
        String uid = null;
        try {
            ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            uid = String.valueOf(applicationInfo.uid);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException e = " + e.getMessage());
        }
        return uid;
    }

    //获取应用图标资源ID。
    public int getAppIconResId() {
        return mPackageInfo.applicationInfo.icon;
    }

    //获取应用图标的Drawable对象。
    public Drawable getAppIconDrawable() {
        return mPackageInfo.applicationInfo.loadIcon(mPackageManager);
    }

    //获取应用程序名称
    public String getAppName(Context context) {
        return context.getResources().getString(mPackageInfo.applicationInfo.labelRes);
    }

    //获取所有已安装应用
    public static void getAllPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            Log.i(TAG, "getAllAppInfo: size = " + packageInfos.size());
            for (int i = 0; i < packageInfos.size(); i++) {
                String packageName = packageInfos.get(i).packageName;
                Log.d(TAG, "packageName = " + packageName);
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllAppInfo: e = " + e.getMessage());
        }
    }

    //获取所有已安装应用
    public static void getAllApplicationInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
            Log.i(TAG, "getAppInfo: size = " + applicationInfos.size());
            for (int i = 0; i < applicationInfos.size(); i++) {
                ApplicationInfo applicationInfo = applicationInfos.get(i);
                //获取该应用的权限列表
                String[] permissions = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (permissions != null) {
                    Log.i(TAG, "getAppInfo: packageName = " + applicationInfo.packageName + ", permission size =  " + permissions.length);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getAppInfo: e = " + e.getMessage());
        }
    }

    //获取所有已安装桌面应用
    public static void getMainAppInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
            Log.i(TAG, "getMainAppInfo: size = " + resolveInfos.size());
            for (int i = 0; i < resolveInfos.size(); i++) {
                ActivityInfo activityInfo = resolveInfos.get(i).activityInfo;
                String app = activityInfo.applicationInfo.loadLabel(packageManager).toString();
                String pkg = activityInfo.packageName;
                Log.d(TAG, "appName = " + app + ", pkg = " + pkg);
            }
        } catch (Exception e) {
            Log.e(TAG, "getMainAppInfo: e = " + e.getMessage());
        }
    }
}
