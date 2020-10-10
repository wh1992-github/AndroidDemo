package com.example.group.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.util.Locale;

@SuppressLint("AppCompatCustomView")
public class NumberSeekBar extends SeekBar {
    private static final String TAG = "NumberSeekBar_";
    private TextPaint mTextPaint;
    private Paint mTabPaint;
    private float mTextHeight;
    private float mTextWidth;
    private float mFontBottom, mFontTop;

    public NumberSeekBar(Context context) {
        this(context, null);
    }

    public NumberSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTabPaint.setColor(Color.BLUE);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 10);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mFontBottom = metrics.bottom;
        mFontTop = metrics.top;
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
        float radius = mTextWidth / 2 + 2;                                                   //水滴圆半径
        float progressX = (getMeasuredWidth() - 2 * radius) * getProgress() / getMax();             //进度条X
        float textCenterX = progressX + getPaddingLeft();                                           //文本中心X
        float textLeft = progressX - mTextWidth / 2 + getPaddingLeft();                             //文本开始X
        float baselineY = getMeasuredHeight() / 2 + 2 * mTextHeight + mFontTop;                     //文本基线Y
        float circleY = baselineY + (mFontBottom + mFontTop) / 2;                                   //圆心Y
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
