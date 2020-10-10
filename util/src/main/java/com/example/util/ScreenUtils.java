package com.example.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtils {
    private static final String TAG = "ScreenUtils";

    public static void printScreenParams(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        int densityDpi = displayMetrics.densityDpi;
        float density = displayMetrics.density;
        float scaledDensity = displayMetrics.scaledDensity;
        float xdpi = displayMetrics.xdpi;
        float ydpi = displayMetrics.ydpi;
        LogUtil.i(TAG, "printScreenParams: widthPixels = " + widthPixels + ", heightPixels = " + heightPixels + ", densityDpi = " + densityDpi
                + ", density = " + density + ", scaledDensity = " + scaledDensity + ", xdpi = " + xdpi + ", ydpi = " + ydpi);
    }

    //获得屏幕高度,像素单位。
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //获得屏幕宽度
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //获得状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //获取当前屏幕截图,包含状态栏
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    //获取当前屏幕截图,不包含状态栏
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
