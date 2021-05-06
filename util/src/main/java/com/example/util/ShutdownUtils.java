
package com.example.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

import java.lang.reflect.Method;

public class ShutdownUtils {
    private static final String TAG = "ShutdownUtils";

    public static final String REBOOT_RECOVERY = "recovery";
    public static final String REBOOT_RECOVERY_UPDATE = "recovery-update";
    public static final String REBOOT_REQUESTED_BY_DEVICE_OWNER = "deviceowner";
    public static final String REBOOT_SAFE_MODE = "safemode";
    public static final String REBOOT_QUIESCENT = "quiescent";
    public static final String SHUTDOWN_USER_REQUESTED = "userrequested";

    public static final int SHUTDOWN_REASON_UNKNOWN = 0;
    public static final int SHUTDOWN_REASON_SHUTDOWN = 1;
    public static final int SHUTDOWN_REASON_REBOOT = 2;
    public static final int SHUTDOWN_REASON_USER_REQUESTED = 3;
    public static final int SHUTDOWN_REASON_THERMAL_SHUTDOWN = 4;

    public static final int GO_TO_SLEEP_REASON_APPLICATION = 0;
    public static final int GO_TO_SLEEP_REASON_DEVICE_ADMIN = 1;
    public static final int GO_TO_SLEEP_REASON_TIMEOUT = 2;
    public static final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
    public static final int GO_TO_SLEEP_REASON_POWER_BUTTON = 4;
    public static final int GO_TO_SLEEP_REASON_HDMI = 5;
    public static final int GO_TO_SLEEP_REASON_SLEEP_BUTTON = 6;
    public static final int GO_TO_SLEEP_FLAG_NO_DOZE = 1 << 0;

    public interface ShutdownListener {
        void onFailed();
    }

    // @RequiresPermission("android.permission.REBOOT")
    public static void reboot(Context context) {
        reboot(context, "");
    }

    // @RequiresPermission("android.permission.REBOOT")
    public static void reboot(Context context, String reason) {
        LogUtil.d(TAG, "reboot() Start...");
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pManager.reboot(reason);
        LogUtil.d(TAG, "reboot() Success");
    }

    @SuppressLint("PrivateApi")
    public static void shutdown(Context context, ShutdownListener listener) {
        LogUtil.d(TAG, "shutdown() Start...");
        try {
            //获得ServiceManager类
            Class ServiceManager = Class.forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            try {
                //获得shutdown()方法
                Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
                //调用shutdown()方法
                shutdown.invoke(oIPowerManager, false, true);
                LogUtil.d(TAG, "shutdown() Success");
            } catch (Exception e) {
                //获得shutdown()方法
                Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, String.class, boolean.class);
                //调用shutdown()方法
                shutdown.invoke(oIPowerManager, false, SHUTDOWN_REASON_SHUTDOWN, true);
                LogUtil.d(TAG, "shutdown() Success");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "shutdown() Exception: " + e.getMessage());
            if (null != listener) {
                listener.onFailed();
            }
        }
    }

    @SuppressLint("PrivateApi")
    public static void goToSleep(Context context, ShutdownListener listener) {
        LogUtil.d(TAG, "goToSleep() Start...");
        try {
            //获得ServiceManager类
            Class ServiceManager = Class.forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method goToSleep = oIPowerManager.getClass().getMethod("goToSleep", long.class, int.class, int.class);
            //调用shutdown()方法
            goToSleep.invoke(oIPowerManager, SystemClock.uptimeMillis(), GO_TO_SLEEP_REASON_POWER_BUTTON, GO_TO_SLEEP_FLAG_NO_DOZE);
            LogUtil.d(TAG, "goToSleep() Success");
        } catch (Exception e) {
            LogUtil.e(TAG, "goToSleep() Exception: " + e.getMessage());
            if (null != listener) {
                listener.onFailed();
            }
        }
    }

}
