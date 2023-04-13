package com.example.customview.loadingview.mac;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.customview.R;


/**
 * Created by test on 16/7/9.
 */

public class LVComputerIpad extends LVComputer {

    int colorHome = Color.rgb(125, 130, 135);

    public LVComputerIpad(Context context) {
        this(context, null);
    }

    public LVComputerIpad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LVComputerIpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void drawScreen(Canvas canvas) {
        rectBg.top = 1;
        rectBg.left = 1;
        rectBg.right = mWidth - 1;
        rectBg.bottom = mHigh - 1;
        rectScreen.top = rectBg.top;
        rectScreen.left = rectBg.left;
        rectScreen.right = rectBg.right;
        rectScreen.bottom = rectBg.bottom;
        mPaint.setColor(Color.rgb(165, 165, 165));
        canvas.drawRoundRect(rectScreen, rectScreen.width() / 12f, rectScreen.width() / 12f, mPaint);
    }

    private void drawScreenWithin(Canvas canvas) {
        rectScreenWithin.top = rectScreen.top + 1;
        rectScreenWithin.left = rectScreen.left + 1;
        rectScreenWithin.right = rectScreen.right - 1;
        rectScreenWithin.bottom = rectScreen.bottom - 1;
        mPaint.setColor(colorScreenWithin);
        canvas.drawRoundRect(rectScreenWithin, rectScreen.width() / 12f - 1, rectScreen.width() / 12f - 1, mPaint);
    }

    private void drawContent(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        ios = setBitmapSize(R.drawable.apple, (int) (rectScreen.width() / 5));
        canvas.drawBitmap(ios, rectScreenShow.centerX() - ios.getWidth() - 5,
                rectScreenShow.centerY() - ios.getHeight() / 2, mPaint);
        android = setBitmapSize(R.drawable.android, (int) (rectScreenShow.width() / 5));
        canvas.drawBitmap(android, rectScreenShow.centerX() + 5,
                rectScreenShow.centerY() - android.getHeight() / 2, mPaint);
    }

    private void drawCamera(Canvas canvas) {
        mPaint.setColor(colorCamera);
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2, 2f, mPaint);
        mPaint.setColor(colorCameraCenter);
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2, 1f, mPaint);
        mPaint.setColor(colorHome);
        canvas.drawCircle(rectScreen.centerX(),
                rectScreenShow.bottom + (rectScreen.width() / 12f - 1) * 1.5f / 2f
                , (rectScreen.width() / 12f - 1) / 2.5f, mPaint);
        mPaint.setColor(colorScreenWithin);
        canvas.drawCircle(rectScreen.centerX(),
                rectScreenShow.bottom + (rectScreen.width() / 12f - 1) * 1.5f / 2f
                , (rectScreen.width() / 12f - 1) / 2.5f - 0.6f, mPaint);
    }

    private void drawScreenShow(Canvas canvas) {
        rectScreenShow.top = rectScreenWithin.top + (rectScreen.width() / 12f - 1) * 1.5f;
        rectScreenShow.bottom = rectScreenWithin.bottom - (rectScreen.width() / 12f - 1) * 1.5f;
        rectScreenShow.right = rectScreenWithin.right - (rectScreen.width() / 12f - 1) * 1f;
        rectScreenShow.left = rectScreenWithin.left + (rectScreen.width() / 12f - 1) * 1f;

        mPaint.setColor(colorScreenShow);
        canvas.drawRect(rectScreenShow, mPaint);
    }

    private void drawScreenReflective(Canvas canvas) {
        pathScreenReflective.reset();
        pathScreenReflective.moveTo(rectScreen.left + rectScreen.width() / 10f * 5f, rectScreen.top);
        pathScreenReflective.lineTo(rectScreen.right - rectScreen.width() / 10f * 2.5f, rectScreen.bottom);
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.bottom);
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.top);
        pathScreenReflective.close();
        mPaint.setColor(colorScreenReflective);
        canvas.drawPath(pathScreenReflective, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (mAnimatedValue >= 0) {
            drawScreen(canvas);
        }
        if (mAnimatedValue >= 1.0f / 6 * 1) {
            drawScreenWithin(canvas);
        }
        if (mAnimatedValue >= 1.0f / 6 * 2) {
            drawScreenShow(canvas);
        }
        if (mAnimatedValue >= 1.0f / 6 * 3) {
            drawCamera(canvas);
        }
        if (mAnimatedValue >= 1.0f / 6 * 4) {
            drawScreenReflective(canvas);
        }
        if (mAnimatedValue >= 1.0f / 6 * 5 && mAnimatedValue <= 1.0f) {
            drawContent(canvas);
        }
        canvas.restore();
    }

    @Override
    protected void initPaint() {
        super.initPaint();
    }
}