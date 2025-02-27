package com.example.helloworld;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@SuppressLint("SimpleDateFormat")
public class TimeChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmActivity---";
    private boolean b0 = true;
    private boolean b1 = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SimpleDateFormat formatter111 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter222 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter111.format(System.currentTimeMillis());
            long timeStart1 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 32700000;
            long timeStart2 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 33000000;
            long timeStart3 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 33300000;
            long timeEnd1 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 74000000;
            long timeEnd2 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 74300000;
            long timeEnd3 = Objects.requireNonNull(formatter111.parse(dateString)).getTime() + 74600000;
            Log.i(TAG, "onReceive: Time changed = " + formatter222.format(System.currentTimeMillis()));
            Log.i(TAG, "onReceive: Time timeStart1 = " + formatter222.format(timeStart1) + ", timeStart2 = " + formatter222.format(timeStart2) + ", timeStart3 = " + formatter222.format(timeStart3));
            Log.i(TAG, "onReceive: Time timeEnd1 = " + formatter222.format(timeEnd1) + ", timeEnd2 = " + formatter222.format(timeEnd2) + ", timeEnd3 = " + formatter222.format(timeEnd3));

            if (b0 && ((System.currentTimeMillis() > timeStart1 && System.currentTimeMillis() < timeStart2) || (System.currentTimeMillis() > timeEnd1 && System.currentTimeMillis() < timeEnd2))) {
                b0 = false;
                b1 = true;
                Intent activity = new Intent();
                ComponentName componentName = new ComponentName("com.ss.android.lark", "com.ss.android.lark.main.app.MainActivity");
                activity.setComponent(componentName);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activity);
            } else if (b1 && ((System.currentTimeMillis() > timeStart2 && System.currentTimeMillis() < timeStart3) || (System.currentTimeMillis() > timeEnd2 && System.currentTimeMillis() < timeEnd3))) {
                b0 = true;
                b1 = false;
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
            }
        } catch (Exception e) {
            Log.e(TAG, "onReceive: e = " + e.getMessage());
        }
    }
}