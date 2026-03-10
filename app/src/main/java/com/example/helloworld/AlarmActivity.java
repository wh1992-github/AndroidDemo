package com.example.helloworld;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.helloworld.databinding.ActivityAlarmBinding;

@SuppressLint({"NonConstantResourceId", "SdCardPath"})
@RequiresApi(api = Build.VERSION_CODES.R)
public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity---";
    private ActivityAlarmBinding mViewBinding;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        mViewBinding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeChangeReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 60 * 1000, pendingIntent);

        mViewBinding.btnBestBuySell.setOnClickListener(v -> {
            new Thread(() -> {
                JDK jdk = new JDK();
                jdk.printBestBuySell(AlarmActivity.this, "长白山.txt");
            }).start();
        });

        mViewBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    JDK jdk = new JDK();
                    jdk.start(AlarmActivity.this);
                }).start();
            }
        });
        new Thread(() -> {
            JDK jdk = new JDK();
            jdk.start(AlarmActivity.this);
        }).start();
    }
}