package com.example.group.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.group.util.Utils;

public class PagerIndicator2 extends LinearLayout {
    private static final String TAG = "PagerIndicator";
    private Context mContext; //声明一个上下文对象
    private int mCount = 5; //指示器的个数
    private int mPad; //两个圆点之间的间隔
    private int mPointSize = 20; //两个圆点之间的间隔
    private int mSeq = 0; //当前指示器的序号
    private float mRatio = 0.0f; //已经移动的距离百分比
    private Paint mPaint; //声明一个画笔对象

    public PagerIndicator2(Context context) {
        this(context, null);
    }

    public PagerIndicator2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        //创建一个新的画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int left = (getMeasuredWidth() - mCount * mPointSize - (mCount - 1) * mPad) / 2;
        //先绘制作为背景的几个灰色圆点
        for (int i = 0; i < mCount; i++) {
            RectF rectF = new RectF(left + i * (mPointSize + mPad), 10, left + i * (mPointSize + mPad) + mPointSize, 10 + mPointSize);
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(rectF, 0, 360, true, mPaint);
            //canvas.drawCircle(left + i * (mPointSize + mPad) + 10, 20, 10, mPaint);
        }
        //再绘制作为前景的高亮红点,该红点随着翻页滑动而左右滚动
        RectF rectF = new RectF(left + (mSeq + mRatio) * (mPointSize + mPad), 10, left + (mSeq + mRatio) * (mPointSize + mPad) + mPointSize, 10 + mPointSize);
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF, 0, 360, true, mPaint);
        //canvas.drawCircle(left + (mSeq + mRatio) * (mPointSize + mPad) + 10, 20, 10, mPaint);
    }

    //设置指示器的个数,以及指示器之间的距离
    public void setCount(int count, int pad) {
        mCount = count;
        mPad = Utils.dip2px(mContext, pad);
        invalidate(); //立刻刷新视图
    }

    //设置指示器当前移动到的位置,及其位移比率
    public void setCurrent(int seq, float ratio) {
        mSeq = seq;
        mRatio = ratio;
        invalidate(); //立刻刷新视图
    }

}
