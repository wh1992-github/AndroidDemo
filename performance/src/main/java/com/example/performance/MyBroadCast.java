package com.example.performance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class MyBroadCast extends BroadcastReceiver {

    private static final String TAG = "whtestdemo";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: MyBroadCast");
        Intent mIntent = new Intent(context, MyBroadCast.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 4000, mPendingIntent);
    }
}
