package com.example.customview.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 与屏幕信息有关的类，包括屏幕的长宽、分辨率、长度换算
 */
public class DisplayUtils {

    /**
     * 获取屏幕宽度
     */
    public static int getDisplayWidth(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            return widthPixels;
        }
        return 720;
    }

    /**
     * 获取屏幕高度
     */
    public static int getDisplayHeight(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int heightPixels = dm.heightPixels;
            return heightPixels;
        }
        return 1280;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
