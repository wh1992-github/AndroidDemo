
package com.example.util;

import android.app.Instrumentation;
import android.content.Context;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.concurrent.TimeUnit;

public class KeyEventUtils {
    public static void sendKeyEvent(final int keyCode, int delayMills) {
        ThreadManager.getInstance().executeScheduled(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(keyCode);
            }
        }, delayMills, TimeUnit.MILLISECONDS);
    }

    public static void clickCenterOfScreen(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final int x = displayMetrics.widthPixels / 2;
        final int y = displayMetrics.heightPixels / 2;
        ThreadManager.getInstance().executeScheduled(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, x, y, 0));
                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, x, y, 0));
            }
        }, 1, TimeUnit.SECONDS);
    }
}
