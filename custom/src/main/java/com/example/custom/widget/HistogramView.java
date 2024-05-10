package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

//自定义View柱状图
public class HistogramView extends View {
    private Paint mPaint;
    private Path mPath;

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制坐标轴
        mPaint.reset();
        mPath.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(100, 100);
        mPath.rLineTo(0, 402);
        mPath.rLineTo(800, 0);
        canvas.drawPath(mPath, mPaint);

        //绘制文字
        mPaint.reset();
        mPaint.setTextSize(30);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("A", 190, 540, mPaint);
        canvas.drawText("B", 290, 540, mPaint);
        canvas.drawText("C", 390, 540, mPaint);
        canvas.drawText("D", 490, 540, mPaint);
        canvas.drawText("E", 590, 540, mPaint);
        canvas.drawText("F", 690, 540, mPaint);
        canvas.drawText("G", 790, 540, mPaint);

        //绘制直方图，柱形图是用较粗的直线来实现的
        mPaint.reset();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(60);
        float[] lines3 = {
                200, 500, 200, 395,
                300, 500, 300, 420,
                400, 500, 400, 470,
                500, 500, 500, 300,
                600, 500, 600, 200,
                700, 500, 700, 150,
                800, 500, 800, 350,
        };
        canvas.drawLines(lines3, mPaint);

        //绘制直方图，柱形图是用矩形来实现的
        mPaint.reset();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(60);
        canvas.drawRect(new RectF(170, 395, 230, 500), mPaint);
        canvas.drawRect(new RectF(270, 460, 330, 500), mPaint);
    }
}
