package com.example.group.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

@SuppressLint("DrawAllocation")
public class LoadingView1 extends View {
    private static final String TAG = "LoadingView";
    private static final float DEFAULT_STROKE_WIDTH = 8f;
    private static final float DEFAULT_LINE_WIDTH = 40f;
    private static final long DEFAULT_DURATION = 1333;

    private Paint mPaint;
    private Paint mLinePaint;
    private int mGroupRotation;
    private ValueAnimator mRenderAnimator;

    public LoadingView1(Context context) {
        this(context, null);
    }

    public LoadingView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int[] mMinColors = new int[]{Color.parseColor("#00000000"), Color.RED};
        //先创建一个渲染器
        SweepGradient mSweepGradient = new SweepGradient(width / 2, height / 2, mMinColors, null);
        //把渐变设置到笔刷
        mPaint.setShader(mSweepGradient);
        RectF rectF = new RectF(10, 10, height - 10, height - 10);
        int saveCount = canvas.save();
        canvas.rotate(mGroupRotation, rectF.centerX(), rectF.centerY());
        canvas.drawArc(rectF, 90, 360, false, mPaint);
        canvas.restoreToCount(saveCount);

        int hafHeight = height / 2;
        float dW = (width - DEFAULT_LINE_WIDTH) / 2;
        canvas.drawLine(dW, hafHeight - 15, width - dW, hafHeight - 15, mLinePaint);
        canvas.drawLine(dW, hafHeight, width - dW, hafHeight, mLinePaint);
        canvas.drawLine(dW, hafHeight + 15, width - dW, hafHeight + 15, mLinePaint);
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
        Log.i(TAG, "onVisibilityChanged: ");
        super.onVisibilityChanged(changedView, visibility);
        final boolean visible = (visibility == VISIBLE && getVisibility() == VISIBLE);
        if (visible) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        Log.i(TAG, "startAnimation: ");
        if (mRenderAnimator == null) {
            mRenderAnimator = ValueAnimator.ofInt(0, 360);
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