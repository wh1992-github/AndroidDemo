package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.custom.R;
import com.example.custom.util.Utils;

import java.io.InputStream;

/**
 * Created by test on 17-8-25.
 */
//围绕圆形图片旋转
public class CircleRotateView extends View {
    //圆心坐标
    private float mXIndex;
    private float mYIndex;
    //半径
    private int mRadius;
    //偏移量，为了让图片居中
    private float mXOff;
    private float mYOff;
    private Paint mPaint;
    private Bitmap mBitmap;
    //与x轴的角度
    private double mAngle = 90;
    //图片的坐标，需要计算
    private float mXPosition;
    private float mYPosition;

    public CircleRotateView(Context context) {
        this(context, null);
    }

    public CircleRotateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRotateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mXIndex = Utils.dip2px(context, 120);
        mYIndex = Utils.dip2px(context, 120);
        mRadius = Utils.dip2px(context, 100);

        @SuppressLint("ResourceType")
        InputStream inputStream = getResources().openRawResource(R.drawable.ic_launcher);
        mBitmap = BitmapFactory.decodeStream(inputStream);
        mXOff = mBitmap.getWidth() / 2;
        mYOff = mBitmap.getHeight() / 2;
    }

    //计算图片的位置坐标
    public void updatePosition() {
        double angleValue = (mAngle / 180.0) * Math.PI;
        mXPosition = (float) (mXIndex + Math.cos(angleValue) * mRadius - mXOff);
        mYPosition = (float) (mYIndex - Math.sin(angleValue) * mRadius - mYOff);
        mAngle -= 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mXIndex, mYIndex, mRadius, mPaint);
        canvas.drawBitmap(mBitmap, mXPosition, mYPosition, mPaint);
        updatePosition();
    }
}
