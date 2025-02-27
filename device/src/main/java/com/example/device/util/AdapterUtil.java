package com.example.device.util;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class AdapterUtil {
    private static final String TAG = "AdapterUtil";
    private static Application mApplication;

    public static void setApplicationDensity(Application application, float baseDp) {
        mApplication = application;
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        Log.i(TAG, "setApplicationDensity: before widthPixels = " + appDisplayMetrics.widthPixels + ", xdpi = " + appDisplayMetrics.xdpi
                + ", ydpi = " + appDisplayMetrics.ydpi + ", densityDpi = " + appDisplayMetrics.densityDpi
                + ", density = " + appDisplayMetrics.density + ", scaledDensity = " + appDisplayMetrics.scaledDensity);
        float targetDensity = appDisplayMetrics.widthPixels / baseDp;
        int targetDensityDpi = (int) (160 * targetDensity);
        //防止字体变小
        float targetScaleDensity = targetDensity * (appDisplayMetrics.scaledDensity / appDisplayMetrics.density);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        Log.i(TAG, "setApplicationDensity: after widthPixels = " + appDisplayMetrics.widthPixels + ", xdpi = " + appDisplayMetrics.xdpi
                + ", ydpi = " + appDisplayMetrics.ydpi + ", densityDpi = " + appDisplayMetrics.densityDpi
                + ", density = " + appDisplayMetrics.density + ", scaledDensity = " + appDisplayMetrics.scaledDensity);

        //系统原来的
        float density1 = Resources.getSystem().getDisplayMetrics().density;
        //更改之后的
        float density2 = application.getResources().getDisplayMetrics().density;
        Log.i(TAG, "setApplicationDensity: 原来的density = " + density1 + ", 更改之后的density = " + density2);
        Log.i(TAG, "setApplicationDensity: AutoSizeUtils 100dp = " + AutoSizeUtils.dp2px(application, 100));
        Log.i(TAG, "setApplicationDensity: AutoSizeUtils 100sp = " + AutoSizeUtils.sp2px(application, 100));
        Log.i(TAG, "setApplicationDensity: 233 = " + AutoSizeUtils.dp2px(application, 233.25f));
    }

    public static void setActivityDensity(Activity activity, float baseDp) {
        DisplayMetrics appDisplayMetrics = activity.getResources().getDisplayMetrics();
        Log.i(TAG, "setActivityDensity: before widthPixels = " + appDisplayMetrics.widthPixels + ", densityDpi = " + appDisplayMetrics.densityDpi
                + ", density = " + appDisplayMetrics.density + ", scaledDensity = " + appDisplayMetrics.scaledDensity);
        float targetDensity = appDisplayMetrics.widthPixels / baseDp;
        int targetDensityDpi = (int) (160 * targetDensity);
        //防止字体变小
        float targetScaleDensity = targetDensity * (appDisplayMetrics.scaledDensity / appDisplayMetrics.density);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        Log.i(TAG, "setActivityDensity: after widthPixels = " + appDisplayMetrics.widthPixels + ", densityDpi = " + appDisplayMetrics.densityDpi
                + ", density = " + appDisplayMetrics.density + ", scaledDensity = " + appDisplayMetrics.scaledDensity);

        //系统原来的
        float density1 = Resources.getSystem().getDisplayMetrics().density;
        //更改之后的
        float density2 = activity.getResources().getDisplayMetrics().density;
        Log.i(TAG, "setActivityDensity: 原来的density = " + density1 + ", 更改之后的density = " + density2);
        Log.i(TAG, "setActivityDensity: AutoSizeUtils 100dp = " + AutoSizeUtils.dp2px(activity, 100));
        Log.i(TAG, "setActivityDensity: AutoSizeUtils 100sp = " + AutoSizeUtils.sp2px(activity, 100));
    }

    public static int dp2px(float value) {

        return (int) (TypedValue.applyDimension(COMPLEX_UNIT_DIP, value, mApplication.getResources().getDisplayMetrics()) + 0.5F);
    }
}
