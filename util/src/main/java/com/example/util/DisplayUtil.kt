package com.example.util

import android.app.Activity
import android.app.Application
import android.util.Log

/**
 * @author test
 */
object DisplayUtil {
    private const val TAG = "DisplayUtil"

    /**
     * 屏幕适配：修改设备密度
     */
    @JvmStatic
    fun setCustomDensity(activity: Activity, application: Application, baseDp: Float) {
        val appDisplayMetrics = application.resources.displayMetrics
        val activityDisplayMetrics = activity.resources.displayMetrics
        Log.i(
            TAG,
            "onCreate: widthPixels = " + appDisplayMetrics.widthPixels + ", densityDpi = " + appDisplayMetrics.densityDpi + ", density = " + appDisplayMetrics.density + ", scaledDensity = " + appDisplayMetrics.scaledDensity
        )
        val targetDensity = appDisplayMetrics.widthPixels / baseDp
        val targetDensityDpi = (160 * targetDensity).toInt()
        val targetScaleDensity =
            targetDensity * (appDisplayMetrics.scaledDensity / appDisplayMetrics.density)
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaleDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }
}