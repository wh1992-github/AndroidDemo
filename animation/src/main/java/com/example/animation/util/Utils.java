package com.example.animation.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by test on 2017/9/11.
 */
public class Utils {
    private static final String TAG = "Utils";

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        //获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f); //四舍五入取整
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        //获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); //四舍五入取整
    }

    //获得屏幕的宽度
    public static int getScreenWidth(Context ctx) {
        //从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; //返回屏幕的宽度数值
    }

    //获得屏幕的高度
    public static int getScreenHeight(Context ctx) {
        //从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels; //返回屏幕的高度数值
    }

    //获得屏幕的像素密度
    public static float getScreenDensity(Context ctx) {
        //从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density; //返回屏幕的像素密度数值
    }

    public static void getScreenSize1(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Log.i(TAG, "getScreenSize: 111---mScreenWidth = " + displayMetrics.widthPixels + ", mScreenHeight = " + displayMetrics.heightPixels);
    }

    public static void getScreenSize2(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Log.i(TAG, "getScreenSize: 222---mScreenWidth = " + displayMetrics.widthPixels + ", mScreenHeight = " + displayMetrics.heightPixels);
    }

    //获得真实的屏幕的像素密度
    public static void getScreenSize3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        Log.i(TAG, "getScreenSize: 333---mScreenWidth = " + displayMetrics.widthPixels + ", mScreenHeight = " + displayMetrics.heightPixels);
    }

    public static void getScreenSize4(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Log.i(TAG, "getScreenSize: 444---mScreenWidth = " + point.x + ", mScreenHeight = " + point.y);
    }

    //获得真实的屏幕的像素密度
    public static void getScreenSize5(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        Log.i(TAG, "getScreenSize: 555---mScreenWidth = " + point.x + ", mScreenHeight = " + point.y);
    }
}

