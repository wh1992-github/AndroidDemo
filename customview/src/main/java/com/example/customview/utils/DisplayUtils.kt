package com.example.customview.utils

import android.content.Context

/**
 * 与屏幕信息有关的类，包括屏幕的长宽、分辨率、长度换算
 */
object DisplayUtils {
    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getDisplayWidth(context: Context?): Int =
        context?.resources?.displayMetrics?.widthPixels ?: 720

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getDisplayHeight(context: Context?): Int =
        context?.resources?.displayMetrics?.heightPixels ?: 1280

    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        return (dipValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dip(context: Context, pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()
    }
}
