package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("DrawAllocation")
public class BezierCustomView1 extends View {
    private static final String TAG = "BezierCustomView1";
    private Paint mPaint;
    private final List<PointF> mList = new ArrayList<>();

    public BezierCustomView1(Context context) {
        this(context, null);
    }

    public BezierCustomView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Log.i(TAG, "onMeasure: width = " + width + ", height = " + height);
    }

    /**
     * 曲线穿过相邻点的中点:
     * 先根据相邻点（P1,P2,P3）计算出相邻点的中点(P12,P23),
     * 最后将（P1,P2,P3）作为控制点画出贝塞尔曲线.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
        //清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OVER);

        //随机生成数据
        mList.clear();
        for (int i = 1; i < 60; i++) {
            mList.add(new PointF(i * 20, new Random().nextInt(300) + 20));
        }

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        for (PointF pointF : mList) {
            canvas.drawCircle(pointF.x, pointF.y, 4, mPaint);
        }

        //绘制连线
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < mList.size() - 1; i++) {
            //canvas.drawLine(mList.get(i).x, mList.get(i).y, mList.get(i + 1).x, mList.get(i + 1).y, mPaint);
        }

        //画高阶贝塞尔曲线
        mPaint.setColor(Color.GREEN);
        //设置渐变色
        //int[] colors = new int[]{Color.RED, Color.BLACK};
        //float[] positions = new float[]{0, 1};
        //Shader shader = new LinearGradient(0, 200, 0, 540, colors, positions, Shader.TileMode.MIRROR);
        //mPaint.setShader(shader);
        Path path = new Path();
        path.moveTo(mList.get(0).x, mList.get(0).y);
        path.quadTo(mList.get(0).x, mList.get(0).y, (mList.get(0).x + mList.get(1).x) / 2, (mList.get(0).y + mList.get(1).y) / 2);
        for (int i = 0; i < mList.size() - 2; i++) {
            path.cubicTo((mList.get(i).x + mList.get(i + 1).x) / 2, (mList.get(i).y + mList.get(i + 1).y) / 2, mList.get(i + 1).x, mList.get(i + 1).y, (mList.get(i + 1).x + mList.get(i + 2).x) / 2, (mList.get(i + 1).y + mList.get(i + 2).y) / 2);
        }
        path.quadTo((mList.get(mList.size() - 2).x + mList.get(mList.size() - 1).x) / 2, (mList.get(mList.size() - 2).y + mList.get(mList.size() - 1).y) / 2, mList.get(mList.size() - 1).x, mList.get(mList.size() - 1).y);
        //设置下方背景
        //mPaint.setStyle(Paint.Style.FILL);
        //path.lineTo(mList.get(mList.size()-1).x, getHeight());
        //path.lineTo(mList.get(0).x, getHeight());
        //path.close();
        canvas.drawPath(path, mPaint);
    }
}
