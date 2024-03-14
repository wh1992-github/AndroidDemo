package com.example.group.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MusicView extends View {
    private static final String TAG = "MusicView";

    private int mHeight;
    private int mWidht;
    private Paint mPaint;
    private int mRandomValue;

    public MusicView(Context context) {
        this(context, null);
    }

    public MusicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.BLUE);
        startAnim();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidht = getMeasuredWidth();
        Log.i(TAG, "onMeasure: h = " + mHeight + ", w = " + mWidht);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = mRandomValue;
        Log.i(TAG, "onDraw: top = " + top);
        canvas.drawRoundRect(mWidht / 2 - 60, top, mWidht / 2 - 50, mHeight - top, 10, 10, mPaint);
        canvas.drawRoundRect(mWidht / 2 - 5, (300 - top), mWidht / 2 + 5, mHeight - (300 - top), 10, 10, mPaint);
        canvas.drawRoundRect(mWidht / 2 + 50, top, mWidht / 2 + 60, mHeight - top, 10, 10, mPaint);
    }

    private void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(100, 180);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRandomValue = (int) animation.getAnimatedValue();
                Log.i(TAG, "startAnim: mRandomValue = " + mRandomValue);
                postInvalidate();
            }
        });

        valueAnimator.start();
    }
}
