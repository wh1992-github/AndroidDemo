package com.example.custom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class BezierRippleView extends View {
    private Paint mPaint;
    private Path mPath;
    private int mWaveStartX;

    public BezierRippleView(Context context) {
        this(context, null);
    }

    public BezierRippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.parseColor("#009FCC"));
    }

    //重写onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        drawPoint(canvas);
        drawHorizontal(canvas);
        //drawVertical(canvas);
    }

    //贝塞尔水平波纹
    private void drawHorizontal(Canvas canvas) {
        if (mWaveStartX >= 0) {
            mWaveStartX = -400;
        }
        mPath = new Path();
        mPath.moveTo(mWaveStartX, 600);
        for (int i = 0; i < getWidth() / 400 + 2; i++) {
            mPath.rQuadTo(100, -100, 200, 0);
            mPath.rQuadTo(100, 100, 200, 0);
        }
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#009FCC"));//设置画笔的颜色为海蓝色
        canvas.drawPath(mPath, mPaint);
    }

    //贝塞尔垂直波纹
    private void drawVertical(Canvas canvas) {
        if (mWaveStartX >= 0) {
            mWaveStartX = -400;
        }
        mPath = new Path();
        mPath.moveTo(0, -mWaveStartX);
        for (int i = 0; i < getWidth() / 400 + 2; i++) {
            mPath.rQuadTo(100, -100, 200, 0);
            mPath.rQuadTo(100, 100, 200, 0);
        }
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#009FCC"));//设置画笔的颜色为海蓝色
        canvas.drawPath(mPath, mPaint);
    }

    //贝塞尔曲线的基准点
    private void drawPoint(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(12);
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < getWidth() / 100 + 2; i++) {
            canvas.drawPoint(i * 100, 300 + getValue(i), mPaint);
        }

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        mPath = new Path();
        mPath.moveTo(0, 300);
        for (int i = 0; i < getWidth() / 400 + 2; i++) {
            mPath.rQuadTo(100, -100, 200, 0);
            mPath.rQuadTo(100, 100, 200, 0);
        }
        canvas.drawPath(mPath, mPaint);
    }

    //获取纵坐标
    private int getValue(int i) {
        if (i % 4 == 1) {
            return -60;
        } else if (i % 4 == 3) {
            return 60;
        }
        return 0;
    }

    //这里是动画效果，在Activity调用就行了
    public void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(-400, 0);
        valueAnimator.setDuration(4000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mWaveStartX = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }
}