package com.example.util;

import android.os.CountDownTimer;
import android.util.Log;
/**
 * 提供 Count Down Timer 相关工具方法的工具类。
 */

public class CountDownTimerUtil extends CountDownTimer {
    private static final String TAG = "CountDownTimerUtil";
    private int mCount = 10;

    //总时长和间隔
    public CountDownTimerUtil(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.i(TAG, "onTick: millisUntilFinished = " + mCount--);
    }

    @Override
    public void onFinish() {
        Log.i(TAG, "onFinish: ");
        cancel();
    }
}
