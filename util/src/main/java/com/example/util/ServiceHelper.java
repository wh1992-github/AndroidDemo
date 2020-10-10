
package com.example.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ServiceHelper {
    private static final String TAG = "ServiceHelper";

    public static ComponentName startService(Context context, Intent intent) {
        ComponentName componentName = null;
        if (VERSION.SDK_INT >= 26) {
            try {
                componentName = context.startService(intent);
                LogUtil.d(TAG, "startService() result: " + componentName);
            } catch (IllegalStateException e) {
                try {
                    componentName = context.startForegroundService(intent);
                    LogUtil.d(TAG, "startForegroundService() result: " + componentName);
                } catch (SecurityException e1) {
                    LogUtil.e(TAG, "startForegroundService() SecurityException: " + e1.getMessage());
                }
            } catch (SecurityException e) {
                LogUtil.e(TAG, "startService() SecurityException: " + e.getMessage());
            }
        } else {
            try {
                componentName = context.startService(intent);
                LogUtil.d(TAG, "startService() result: " + componentName);
            } catch (IllegalStateException e) {
                LogUtil.e(TAG, "startService() IllegalStateException: " + e.getMessage());
            } catch (SecurityException e) {
                LogUtil.e(TAG, "startService() SecurityException: " + e.getMessage());
            }
        }

        return componentName;
    }

    public static void setForegroundService(Service service, int iconResId,
                                            String channelName, String channelId, int id) {
        setForegroundService(service, id, channelName, channelId, iconResId, iconResId,
                "default", "default");
    }

    public static void setForegroundService(Service service, int id,
                                            String name, String channelId,
                                            int smallIconId, int largeIconId,
                                            String title, String content) {
        NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(service, channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(smallIconId)
                    .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), largeIconId))
                    .build();
        } else {
            //notification = new Notification();
            //notification.flags = Notification.FLAG_ONGOING_EVENT;
            //notification.flags |= Notification.FLAG_NO_CLEAR;
            //notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(service, channelId);
            mBuilder.setSmallIcon(smallIconId);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(service.getResources(), largeIconId));
            if (VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(content);
            }

            notification = mBuilder.build();
        }
        service.startForeground(id, notification);
    }
}
