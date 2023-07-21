package com.example.group.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

@SuppressLint("DrawAllocation")
public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";
    private static final float DEFAULT_STROKE_WIDTH = 8f;
    private static final long DEFAULT_DURATION = 2000;
    private Paint mPaint;
    private ValueAnimator mRenderAnimator;
    private int mCurrentIndex;
    private int mPageCount = 5;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void setPageCount(int count) {
        mPageCount = count;
        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int index = mCurrentIndex % mPageCount;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int top = height / 2 - 10;
        int bottom = height / 2 + 10;
        int singleWidth = width / mPageCount;
        int gap = 10;

        for (int i = 0; i < mPageCount; i++) {
            if (index == i) {
                mPaint.setColor(Color.parseColor("#FF0000"));
            } else {
                mPaint.setColor(Color.parseColor("#80FF0000"));
            }
            canvas.drawRoundRect(new RectF(gap + singleWidth * i, top, singleWidth * (i + 1) - gap, bottom), 10, 10, mPaint);
        }
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
            mRenderAnimator = ValueAnimator.ofInt(0, mPageCount);
            mRenderAnimator.setDuration(DEFAULT_DURATION);
            mRenderAnimator.setInterpolator(new LinearInterpolator());
            mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);
            mRenderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentIndex = (int) animation.getAnimatedValue();
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
