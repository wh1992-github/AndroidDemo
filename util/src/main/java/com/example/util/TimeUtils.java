
package com.example.util;

import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static String formatDateTime1(long milliseconds) {
        StringBuilder sb = new StringBuilder();
        long mss = milliseconds / 1000;
        long second = mss % 60;
        long minute = mss / 60 % 60;
        long hour = mss / 60 / 60;
        DecimalFormat format = new DecimalFormat("00");
        if (hour > 0) {
            sb.append(format.format(hour)).append(":").append(format.format(minute)).append(":").append(format.format(second));
        } else {
            sb.append(format.format(minute)).append(":").append(format.format(second));
        }
        return sb.toString();
    }

    //毫秒转化为标准时间格式
    public static String formatDateTime2(long milliseconds) {
        long mss = milliseconds / 1000;
        long second = mss % 60;
        long minute = mss / 60 % 60;
        long hour = mss / 60 / 60;
        if (hour > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
        }
    }

    //判断系统是否自动获取时区
    public static boolean isTimeZoneAuto(Context mContext) {
        try {
            return Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME_ZONE) > 0;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "isTimeZoneAuto: e = " + e.getMessage());
        }
        return false;
    }

    //设置自动获取时区
    public static void setAutoTimeZone(Context mContext, int checked) {
        Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, checked);
    }

    //获取系统默认时区
    public static String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    //设置系统默认时区
    //需要添加权限<uses-permission android:name="android.permission.SET_TIME_ZONE" />
    public static void setChinaTimeZone(Context context) {
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("Asia/Shanghai");// Asia/Taipei//GMT+08:00
    }

    //判断系统是否是24h/12h格式
    public static String timeFormat(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String strTimeFormat = Settings.System.getString(contentResolver, Settings.System.TIME_12_24);
        if (strTimeFormat.equals("24")) {
            return "24h";
        } else if (strTimeFormat.equals("12")) {
            return "12h";
        }
        return "unKnow";
    }
}
