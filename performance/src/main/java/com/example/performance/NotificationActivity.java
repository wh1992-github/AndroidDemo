package com.example.performance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

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
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
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
            case R.id.btn4:
                openNormalNotify();
                break;
            case R.id.btn5:
                openFoldNotify();
                break;
            case R.id.btn6:
                openFlyNotify();
                break;
            //Notification 还有一个显示等级,使用很简单,详见Android进阶之光22页.
            default:
                break;
        }
    }

    //通知发出时，播放一段音频
    //.setSound(Uri.fromFile(new File("音频地址")))
    //通知发出时，震动
    //.setVibrate(new long[]{0,1000,1000,1000})
    //设置led灯闪烁，灯的颜色，灯亮的时长，灯暗去的时长
    //.setLights(Color.GREEN,1000,1000)
    //设置默认铃声和震动
    //.setDefaults(Notification.DEFAULT_ALL)
    //设置富文本通知，比如设置一段长文字或者图片
    //.setStyle(new Notification.BigTextStyle().bigText("很长很长的话"))
    //.setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.bg_02)))
    //设置通知的重要性
    //.setPriority(Notification.PRIORITY_MAX)
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
        //实例化Notification.Builder并设置相关属性
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setChannelId(channelID)
                .setContentTitle("最简单的Notification")
                .setContentText("只有标题和内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
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
        //实例化Notification.Builder并设置相关属性
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setChannelId(channelID)
                .setContentTitle("最简单的Notification")
                .setContentText("更新的通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
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

    //普通的Notification
    private void openNormalNotify() {
        Intent intent = new Intent(this, ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Title");
        builder.setContentText("普通的Notification");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Android8.0 版本必须加上channelId参数。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "1";
            String channelName = "channel_name";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    //折叠式Notification
    private void openFoldNotify() {
        Intent intent = new Intent(this, ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Title");
        builder.setContentText("折叠式Notification");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Android8.0 版本必须加上channelId参数。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "2";
            String channelName = "channel_name";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        Notification notification = builder.build();
        //折叠式的设置RemoteViews方法
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_item);
        notification.bigContentView = remoteViews;
        notificationManager.notify(2, notification);
    }

    //悬挂式Notification
    private void openFlyNotify() {
        Intent intent = new Intent(this, ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Title");
        builder.setContentText("悬挂式Notification");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        //悬挂式的设置Intent方法，与普通的对比只多了 setFullScreenIntent 方法。
        builder.setFullScreenIntent(pendingIntent, true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Android8.0 版本必须加上channelId参数。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "3";
            String channelName = "channel_name";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        Notification notification = builder.build();
        notificationManager.notify(3, notification);
    }
}
