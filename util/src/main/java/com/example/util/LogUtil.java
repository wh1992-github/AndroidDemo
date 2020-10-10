package com.example.util;

import android.util.Log;

/**
 * Log.v 的输出为黑色,输出大于或等于VERBOSE日志级别的信息
 * Log.d 的输出是蓝色,输出大于或等于DEBUG日志级别的信息
 * Log.i 的输出为绿色,输出大于或等于INFO日志级别的信息
 * Log.w 的输出为橙色,输出大于或等于WARN日志级别的信息
 * Log.e 的输出为红色,仅输出ERROR日志级别的信息
 */
public final class LogUtil {
    private static final String APP_TAG = "AndroidUtil";
    private static final boolean LOGV_ON = true;
    private static final boolean LOGD_ON = true;
    private static final boolean LOGI_ON = true;
    //开启输出全部调用栈
    private static final boolean ENABLE_FULL_STACK = false;
    //日志栈尾深度
    private static final int STACK_TRACE_TAIL_DEPTH = 6;
    //分隔线
    private static final String line = "----------------------------------\n";

    public static void v(String tag, String msg) {
        if (LOGV_ON) {
            Log.v(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOGD_ON) {
            Log.d(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOGI_ON) {
            Log.d(APP_TAG, getPrefix(tag, msg));
        }
    }

    public static void w(String tag, String msg) {
        if (LOGI_ON) {
            Log.e(APP_TAG, getPrefix(tag, msg));
        }
    }

    public static void e(String tag, String msg) {
        if (LOGI_ON) {
            Log.e(APP_TAG, getPrefix(tag, msg));
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (LOGI_ON) {
            Log.e(APP_TAG, getPrefix(tag, "") + getThrowInfo(throwable));
        }
    }

    //生成前缀信息
    private static String getPrefix(String tag, String msg) {
        String prefix;
        if (ENABLE_FULL_STACK) {
            prefix = tag + "\n" + line + getAllInvokerInfo() + line;
        } else {
            prefix = tag + " --- " + msg + " |---> " + getInvokerInfoAt(STACK_TRACE_TAIL_DEPTH);
        }
        return prefix;
    }

    //获得调用栈中指定索引的调用者信息
    private static String getInvokerInfoAt(int index) {
        StackTraceElement invoker = getInvoker(index);
        return getClassName(invoker) + "." + getMethodName(invoker) + "(" + getFileName(invoker) + ":" + getLineNumber(invoker) + ")";
    }

    //获得全部调用栈信息
    private static String getAllInvokerInfo() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder info = new StringBuilder();
        for (int i = trace.length; i > STACK_TRACE_TAIL_DEPTH; i--) {
            stringBuilder.insert(0, " ");
            info.append(stringBuilder).append(getInvokerInfoAt(i)).append("\n");
        }
        return info.toString();
    }

    //获得调用栈中指定索引的调用实例
    private static StackTraceElement getInvoker(int index) {
        return Thread.currentThread().getStackTrace()[index];
    }

    private static String getClassName(StackTraceElement invoker) {
        return invoker.getClassName();
    }

    private static String getMethodName(StackTraceElement invoker) {
        return invoker.getMethodName();
    }

    private static String getFileName(StackTraceElement invoker) {
        return invoker.getFileName();
    }

    private static String getLineNumber(StackTraceElement invoker) {
        return String.valueOf(invoker.getLineNumber());
    }

    //根据异常生成异常信息
    private static String getThrowInfo(Throwable t) {
        return "\n" + Log.getStackTraceString(t);
    }
}