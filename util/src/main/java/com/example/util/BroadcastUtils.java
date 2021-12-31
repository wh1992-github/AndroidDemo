package com.example.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.util.List;

public class BroadcastUtils {

    /**
     * 判断是否有应用注册指定的广播（仅支持静态注册）
     *
     * @param context 上下文
     * @param intent  需要查询的intent,需要设置action
     * @return true或false
     */
    public static boolean isRegisterBroadcast(Context context, Intent intent) {
        List<ResolveInfo> list = getRegisterBroadcastList(context, intent);
        return list != null && !list.isEmpty();
    }

    /**
     * 获取注册指定intent的接受者列表,仅支持静态注册。
     *
     * @param context 上下文
     * @param intent  需要查询的intent
     * @return null或list
     */
    public static List<ResolveInfo> getRegisterBroadcastList(Context context, Intent intent) {
        if (null == intent || TextUtils.isEmpty(intent.getAction())) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> results = pm.queryBroadcastReceivers(intent, 0);
        return results;
    }

}
