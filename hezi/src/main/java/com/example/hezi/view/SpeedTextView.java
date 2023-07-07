package com.example.hezi.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class SpeedTextView extends AppCompatTextView {

    private static final String TAG = "SpeedTextView";

    public SpeedTextView(Context context) {
        this(context, null);
    }

    public SpeedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            Log.i(TAG, "onAnimationUpdate: " + valueAnimator1.getAnimatedValue());
            setText(String.valueOf(valueAnimator1.getAnimatedValue()));
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
