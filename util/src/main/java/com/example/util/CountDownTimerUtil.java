package com.example.util;

import android.os.CountDownTimer;
import android.util.Log;

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
    }
}
