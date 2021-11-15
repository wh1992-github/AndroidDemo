package com.example.performance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaiton);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.btn1:
                startNotify();
                break;
            case R.id.btn2:
                updateNotify();
                break;
            case R.id.btn3:
                cancelNotify();
                break;
        }
    }

    private void startNotify() {
        Intent mainIntent = new Intent(this, ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        String channelID = "channel_id";
        String channelName = "channel_name";
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        //实例化NotificationCompat.Builder并设置相关属性
        Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(channelID)
                .setContentTitle("最简单的Notification")
                .setContentText("只有小图标、标题、内容")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .build();
        int id = 1;
        notificationManager.notify(id, notification);
    }

    private void updateNotify() {
        Intent mainIntent = new Intent(this, ResultActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        String channelID = "channel_id";
        String channelName = "channel_name";
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        //实例化NotificationCompat.Builder并设置相关属性
        Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setChannelId(channelID)
                .setContentTitle("最简单的Notification")
                .setContentText("更新的通知")
                .setContentIntent(mPendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .build();
        int id = 1;
        notificationManager.notify(id, notification);
    }

    private void cancelNotify() {
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyManager == null) {
            return;
        }
        int id = 1;
        notifyManager.cancel(id);
        //取消全部
        //notifyManager.cancelAll();
    }
}
