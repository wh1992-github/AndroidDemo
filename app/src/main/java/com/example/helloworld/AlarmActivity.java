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
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 60 * 1000, pendingIntent);

        mViewBinding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    Log.i(TAG, "Constants--- -------------------------------- ALL START -------------------------------- ");
                    Constants.printTest();
                    Log.i(TAG, "Constants--- --------------------------------- ALL END --------------------------------- ");
                }).start();
            }
        });
    }
}