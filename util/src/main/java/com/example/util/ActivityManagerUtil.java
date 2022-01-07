package com.example.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * 管理activity
 */
@SuppressWarnings("deprecation")
public class ActivityManagerUtil {
    private static final String TAG = "ActivityManagerUtil";
    private static Stack<Activity> mActivityStack;
    private static ActivityManagerUtil mActivityManagerUtil;

    private ActivityManagerUtil() {
    }

    //单一实例
    public static ActivityManagerUtil getInstance() {
        if (mActivityManagerUtil == null) {
            mActivityManagerUtil = new ActivityManagerUtil();
        }
        return mActivityManagerUtil;
    }

    //添加Activity到堆栈
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    //获取栈顶Activity
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    //结束栈顶Activity
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    //结束指定的Activity
    public void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    //结束指定类名的Activity
    public void killActivity(Class<?> clazz) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(clazz)) {
                killActivity(activity);
            }
        }
    }

    //结束所有Activity
    public void killAllActivity() {
        for (Activity activity : mActivityStack) {
            activity.finish();
        }
        mActivityStack.clear();
    }

    //退出应用程序
    public void AppExit(Context context) {
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            Log.e(TAG, "AppExit: e = " + e.getMessage());
        }
    }
}
