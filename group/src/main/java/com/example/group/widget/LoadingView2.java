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

public class LoadingView2 extends View {
    private static final String TAG = "LoadingView2";
    private static final float DEFAULT_STROKE_WIDTH = 8f;
    private static final long DEFAULT_DURATION = 2000;
    private Paint mPaint;
    private ValueAnimator mRenderAnimator;
    private int mGroupRotation;

    public LoadingView2(Context context) {
        this(context, null);
    }

    public LoadingView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int index = mGroupRotation % 5;
        canvas.drawCircle(40, 50, index == 0 ? 20 : 12, mPaint);
        canvas.drawCircle(110, 50, index == 1 ? 20 : 12, mPaint);
        canvas.drawCircle(180, 50, index == 2 ? 20 : 12, mPaint);
        canvas.drawCircle(250, 50, index == 3 ? 20 : 12, mPaint);
        canvas.drawCircle(320, 50, index == 4 ? 20 : 12, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        boolean visible = (visibility == VISIBLE && getVisibility() == VISIBLE);
        Log.i(TAG, "onVisibilityChanged: visible = " + visible);
        if (visible) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        Log.i(TAG, "startAnimation: ");
        if (mRenderAnimator == null) {
            mRenderAnimator = ValueAnimator.ofInt(0, 5);
            mRenderAnimator.setDuration(DEFAULT_DURATION);
            mRenderAnimator.setInterpolator(new LinearInterpolator());
            mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);
            mRenderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mGroupRotation = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        }
        if (!mRenderAnimator.isStarted()) {
            mRenderAnimator.start();
        } else if (mRenderAnimator.isPaused()) {
            mRenderAnimator.resume();
        }
    }

    private void stopAnimation() {
        Log.i(TAG, "stopAnimation: ");
        if (mRenderAnimator != null) {
            mRenderAnimator.pause();
        }
    }
}
