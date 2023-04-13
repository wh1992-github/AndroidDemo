package com.example.customview.loadingview.mac;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.example.customview.R;


/**
 * Created by test on 16/7/8.
 */

public class LVComputerDesktop extends LVComputer {
    RectF rectHost = new RectF();
    RectF rectSupport = new RectF();
    RectF rectBottom = new RectF();
    Path pathSupport = new Path();

    public LVComputerDesktop(Context context) {
        this(context, null);
    }

    public LVComputerDesktop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LVComputerDesktop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void drawScreen(Canvas canvas) {
        rectBg.top = mPadding;
        rectBg.left = mPadding;
        rectBg.right = mWidth - mPadding;
        rectBg.bottom = mHigh - mHigh / 5;
        rectScreen.top = rectBg.top;
        rectScreen.left = rectBg.left + rectBg.width() / 6f;
        rectScreen.right = rectBg.right - rectBg.width() / 6f;
        rectScreen.bottom = rectBg.bottom;
        mPaint.setColor(colorScreenWithin);
        canvas.drawRoundRect(rectScreen, rectBg.width() / 6f / 6f, rectBg.width() / 6f / 6f, mPaint);
    }

    private void drawHost(Canvas canvas) {
        rectHost.top = rectScreen.height() / 6 * 4 + 2;
        rectHost.bottom = rectScreen.bottom + 2;
        rectHost.left = rectScreen.left;
        rectHost.right = rectScreen.right;

        mShader = new LinearGradient(rectHost.left, rectHost.bottom, rectHost.right, rectHost.bottom, new int[]{Color.rgb(176, 177, 177), Color.rgb(226, 227, 229), Color.rgb(226, 227, 229)}, new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP);
        mPaint.setColor(Color.rgb(176, 177, 177));
        mPaint.setShader(mShader);
        canvas.drawRoundRect(rectHost, rectBg.width() / 6 / 6f, rectBg.width() / 6 / 6f, mPaint);
        mPaint.setShader(null);

        rectScreenWithin.top = rectHost.top;
        rectScreenWithin.left = rectHost.left;
        rectScreenWithin.right = rectHost.right;
        rectScreenWithin.bottom = rectHost.top + rectHost.height() / 2f;
        mPaint.setColor(Color.rgb(0, 0, 0));

        canvas.drawRect(rectScreenWithin, mPaint);
    }

    private void drawLogo(Canvas canvas) {
        ios = setBitmapSize(R.drawable.apple_dark, (int) (rectHost.height() / 4f));
        canvas.drawBitmap(ios, rectScreenWithin.centerX() - ios.getWidth() / 2f, rectScreenWithin.bottom + rectHost.height() / 4f - ios.getHeight() / 2, mPaint);
    }

    private void drawScreenShow(Canvas canvas) {
        rectScreenShow.top = rectScreen.top + rectBg.width() / 6 / 6f;
        rectScreenShow.left = rectScreen.left + rectBg.width() / 6 / 6f;
        rectScreenShow.right = rectScreen.right - rectBg.width() / 6 / 6f;
        rectScreenShow.bottom = rectScreenWithin.bottom - rectBg.width() / 6 / 6f;
        mPaint.setColor(colorScreenShow);
        canvas.drawRect(rectScreenShow, mPaint);
    }

    private void drawCamera(Canvas canvas) {
        mPaint.setColor(colorCamera);
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 4, mPaint);
        mPaint.setColor(colorCameraCenter);
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 2, mPaint);
    }

    private void drawScreenReflective(Canvas canvas) {
        pathScreenReflective.reset();
        pathScreenReflective.moveTo(rectScreen.left + rectScreen.width() / 10f * 6f, rectScreen.top);
        pathScreenReflective.lineTo(rectScreen.right - rectScreen.width() / 10f, rectScreen.bottom);
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.bottom);
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.top);
        pathScreenReflective.close();

        mPaint.setColor(colorScreenReflective);
        canvas.drawPath(pathScreenReflective, mPaint);
    }

    private void drawSupport(Canvas canvas) {
        pathSupport.reset();
        rectSupport.top = rectScreen.bottom;
        rectSupport.left = rectScreen.centerX() - rectScreen.width() / 9f;
        rectSupport.right = rectScreen.centerX() + rectScreen.width() / 9f;
        rectSupport.bottom = rectScreen.bottom + mHigh / 5 * 0.8f;
        pathSupport.moveTo(rectSupport.left + 10, rectSupport.top);
        pathSupport.cubicTo(

                rectSupport.left + rectSupport.width() / 18f, rectSupport.top + rectSupport.height() * 2.5f / 3f,

                rectSupport.left + rectSupport.width() / 16f, rectSupport.top + rectSupport.height() * 2f / 3f,

                rectSupport.left - 30, rectSupport.bottom - 20);
        pathSupport.lineTo(rectSupport.right + 30, rectSupport.bottom - 20);
        pathSupport.cubicTo(

                rectSupport.right - rectSupport.width() / 16f, rectSupport.top + rectSupport.height() * 2f / 3f,

                rectSupport.right - rectSupport.width() / 18f, rectSupport.top + rectSupport.height() * 2.5f / 3f,

                rectSupport.right - 10, rectSupport.top);


        rectBottom.top = rectSupport.bottom - 20;
        rectBottom.bottom = rectSupport.bottom - 20 + mHigh / 5 * 0.8f / 15f;
        rectBottom.left = rectSupport.left - 30;
        rectBottom.right = rectSupport.right + 30;
        pathSupport.close();
        mShader = new LinearGradient(rectSupport.centerX(), rectSupport.top, rectSupport.centerX(), rectSupport.bottom, new int[]{Color.rgb(190, 190, 190), Color.rgb(245, 245, 245), Color.rgb(245, 245, 245), Color.rgb(190, 190, 190), Color.rgb(245, 245, 245)}, new float[]{0f, 0.5f, 0.55f, 0.65f, 1f}, Shader.TileMode.CLAMP);

        mPaint.setColor(Color.rgb(249, 249, 249));
        mPaint.setShader(mShader);
        canvas.drawPath(pathSupport, mPaint);
        mPaint.setShader(null);
    }

    private void drawSupportBottom(Canvas canvas) {
        pathSupport.reset();
        pathSupport.moveTo(rectBottom.left, rectBottom.top - 1);
        pathSupport.quadTo(rectBottom.left, rectBottom.bottom, rectBottom.left + 10, rectBottom.bottom);
        pathSupport.lineTo(rectBottom.right - 10, rectBottom.bottom);
        pathSupport.quadTo(rectBottom.right, rectBottom.bottom, rectBottom.right, rectBottom.top - 1);
        mShader = new LinearGradient(rectBottom.centerX(), rectBottom.top, rectBottom.centerX(), rectBottom.bottom, new int[]{Color.rgb(230, 230, 230), Color.rgb(220, 220, 220), Color.rgb(209, 210, 211)}, new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP);
        mPaint.setColor(Color.rgb(230, 230, 230));
        mPaint.setShader(mShader);
        canvas.drawPath(pathSupport, mPaint);
        mPaint.setShader(null);
    }

    private void drawComputerShadow(Canvas canvas) {
        pathComputerShadow.reset();
        pathComputerShadow.moveTo(rectBottom.left + 10, rectBottom.bottom);

        pathComputerShadow.quadTo(rectBottom.left + 30, rectBottom.bottom,

                rectBottom.left + 30, mHigh

        );
        pathComputerShadow.lineTo(rectBottom.right - 30, mHigh);
        pathComputerShadow.quadTo(rectBottom.right - 30, rectBottom.bottom,

                rectBottom.right - 10, rectBottom.bottom

        );

        mShader = new LinearGradient(rectBottom.centerX(), rectBottom.bottom, rectBottom.centerX(), mHigh, new int[]{Color.rgb(235, 235, 235), Color.rgb(255, 255, 255)}, new float[]{0f, 1f}, Shader.TileMode.CLAMP);

        mPaint.setColor(Color.rgb(245, 245, 245));
        mPaint.setShader(mShader);

        canvas.drawPath(pathComputerShadow, mPaint);
        mPaint.setShader(null);
    }

    private void drawContent(Canvas canvas) {
        ios = setBitmapSize(R.drawable.apple, (int) (rectScreen.width() / 10));
        canvas.drawBitmap(ios, rectScreenShow.centerX() - ios.getWidth() - 5, rectScreenShow.centerY() - ios.getHeight() / 2, mPaint);
        android = setBitmapSize(R.drawable.android, (int) (rectScreenShow.width() / 11));
        canvas.drawBitmap(android, rectScreenShow.centerX() + 5, rectScreenShow.centerY() - android.getHeight() / 2, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (mAnimatedValue >= 0) {
            drawScreen(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10) {
            drawHost(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 2) {
            drawLogo(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 3) {
            drawScreenShow(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 4) {
            drawCamera(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 5) {
            drawScreenReflective(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 6) {
            drawSupport(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 7) {
            drawSupportBottom(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 8) {
            drawComputerShadow(canvas);
        }
        if (mAnimatedValue >= 1.0f / 10 * 9 && mAnimatedValue <= 1.0f) {
            drawContent(canvas);
        }
        canvas.restore();
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        mPadding = dip2px(2f);
    }
}
