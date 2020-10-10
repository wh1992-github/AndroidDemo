package com.example.group.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Locale;

public class NumberProgressBar extends ProgressBar {
    private TextPaint mTextPaint;
    private Paint mTabPaint;
    private float mTextHeight;
    private float mTextWidth;

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTabPaint.setColor(Color.RED);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 10);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mTextHeight = metrics.bottom - metrics.top;
        mTextWidth = mTextPaint.measureText("88%");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resetHeight = MeasureSpec.makeMeasureSpec((int) (6 * mTextHeight), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, resetHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (100 == getProgress()) {
            mTextPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 8);
        } else {
            mTextPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 10);
        }
        float radius = mTextWidth / 2 + 2;                                                                        //水滴圆半径
        float textCenterX = (getMeasuredWidth() - radius) * getProgress() / getMax();                             //文本中心X
        float textLeft = (getMeasuredWidth() - radius) * getProgress() / getMax() - mTextWidth / 2;               //文本X
        float baselineY = getMeasuredHeight() / 2 + 2 * mTextHeight + mTextPaint.getFontMetrics().top;                //文本基线Y
        float circleY = baselineY + (mTextPaint.getFontMetrics().bottom + mTextPaint.getFontMetrics().top) / 2;           //圆心Y
        //画水滴文本圆
        canvas.drawCircle(textCenterX, circleY + mTextWidth / 2, radius, mTabPaint);
        //画水滴圆切边三角形
        Path path = new Path();
        double tangle = Math.acos(radius / mTextWidth);
        float rightX = (float) (textCenterX + Math.sin(tangle) * radius);
        float rightY = (float) (circleY + mTextWidth / 2 - Math.cos(tangle) * radius);
        float leftX = (float) (textCenterX - Math.sin(tangle) * radius);
        float leftY = (float) (circleY + mTextWidth / 2 - Math.cos(tangle) * radius);
        path.moveTo(textCenterX, circleY - mTextWidth / 2);
        path.lineTo(rightX, rightY);
        path.lineTo(leftX, leftY);
        path.close();
        canvas.drawPath(path, mTabPaint);
        //画文本
        String text = String.format(Locale.getDefault(), "%02d%%", getProgress());
        canvas.drawText(text, textLeft, baselineY + mTextWidth / 2, mTextPaint);
    }
}
