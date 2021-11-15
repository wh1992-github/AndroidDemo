package com.example.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * drawText文字绝对居中
 */
public class CustomTextView extends View {
    private static final String TAG = "CustomTextView";
    private Paint mPaint;
    private int mSize;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSize = dp2px(300);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String content = "123国 国321";
        mPaint.setStrokeWidth(1f);
        mPaint.setTextSize(sp2px(40));
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float ascent = fm.ascent;
        float descent = fm.descent;
        float top = fm.top;
        float bottom = fm.bottom;
        float leading = fm.leading;
        float height = descent - ascent;
        float width = mPaint.measureText(content);
        Log.i(TAG, "onDraw: top = " + top + ", ascent = " + ascent + ", leading = " + leading + ", descent = " + descent + ", bottom = " + bottom);
        Log.i(TAG, "onDraw: width = " + width + ", height = " + height);

        mPaint.setColor(Color.RED);
        canvas.drawLine(0, (mSize + height) / 2 - descent + top, mSize, (mSize + height) / 2 - descent + top, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, (mSize + height) / 2 - descent + ascent, mSize, (mSize + height) / 2 - descent + ascent, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(0, (mSize + height) / 2 - descent + leading, mSize, (mSize + height) / 2 - descent + leading, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, (mSize + height) / 2 - descent + descent, mSize, (mSize + height) / 2 - descent + descent, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, (mSize + height) / 2 - descent + bottom, mSize, (mSize + height) / 2 - descent + bottom, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawText(content, (mSize - width) / 2, (mSize + height) / 2 - descent, mPaint);

        mPaint.setStrokeWidth(10f);
        mPaint.setColor(Color.BLACK);
        canvas.drawPoint(mSize / 2, mSize / 2, mPaint);
    }

    //将 dp 转换为 px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
