package com.example.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

//仿锤子时钟
public class CircleClockView extends View {
    private static final String TAG = "CircleClockView--";
    //表盘的背景颜色
    @ColorInt
    private static final int DIAL_BG = 0xFFF0F0F0;
    //表外圆环的颜色
    @ColorInt
    private static final int RING_BG = 0xFFF8F8F8;
    //字体颜色
    @ColorInt
    private static final int TEXT_COLOR = 0xFF141414;
    //时针和分针的颜色
    @ColorInt
    private static final int HOUR_MINUTE_COLOR = 0xFF5B5B5B;
    //秒钟的颜色
    @ColorInt
    private static final int SECOND_COLOR = 0xFFB55050;
    //表盘最小大小
    private static final int MIN_SIZE = 200;
    private static final int HOUR_MINUTE_WIDTH = 30;
    private static final int SECOND_WIDTH = 8;
    //每秒 秒针移动6°
    private static final int DEGREE = 6;
    private int hour, minute, second;
    //圆环的宽度
    private int ringPaintWidth = 10;
    //表盘的大小
    private int mSize;
    //表盘背景画笔
    private Paint outCirclePaint;
    //最外层圆环
    private Paint outRingPaint;
    //时间文本
    private Paint timeTextPaint;
    //时针,分针,秒针
    private Paint hourPaint, minutePaint, secondPaint;
    //中心圆
    private Paint inCirclePaint, inRedCirclePaint;
    //时间
    private Paint textPaint;
    //为了显示秒针晃动效果,需要重复绘制
    private boolean isFirst = true;

    /**
     * 居中
     * 日历类，用来获取当前时间
     */
    private Calendar calendar;

    public CircleClockView(Context context) {
        this(context, null);
    }

    public CircleClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //获取当前时间的实例
        calendar = Calendar.getInstance();

        outCirclePaint = new Paint();
        outCirclePaint.setColor(DIAL_BG);
        outCirclePaint.setAntiAlias(true);

        outRingPaint = new Paint();
        outRingPaint.setColor(RING_BG);
        outRingPaint.setStrokeWidth(dp2px(ringPaintWidth));
        outRingPaint.setStyle(Paint.Style.STROKE);
        outRingPaint.setAntiAlias(true);
        //添加阴影 0x80000000
        outRingPaint.setShadowLayer(4, 2, 2, 0x80000000);

        timeTextPaint = new Paint();
        timeTextPaint.setAntiAlias(true);
        timeTextPaint.setColor(TEXT_COLOR);

        hourPaint = new Paint();
        hourPaint.setAntiAlias(true);
        hourPaint.setColor(HOUR_MINUTE_COLOR);
        hourPaint.setStrokeWidth(HOUR_MINUTE_WIDTH);
        //设置为圆角
        hourPaint.setStrokeCap(Paint.Cap.ROUND);
        //添加阴影
        hourPaint.setShadowLayer(4, 0, 0, 0x80000000);

        minutePaint = new Paint();
        minutePaint.setAntiAlias(true);
        minutePaint.setColor(HOUR_MINUTE_COLOR);
        minutePaint.setStrokeWidth(HOUR_MINUTE_WIDTH);
        //设置为圆角
        minutePaint.setStrokeCap(Paint.Cap.ROUND);
        //添加阴影
        minutePaint.setShadowLayer(4, 0, 0, 0x80000000);

        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setColor(SECOND_COLOR);
        secondPaint.setStrokeWidth(SECOND_WIDTH);
        //设置为圆角
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
        //添加阴影
        secondPaint.setShadowLayer(4, 3, 0, 0x80000000);

        inCirclePaint = new Paint();
        inCirclePaint.setAntiAlias(true);
        inCirclePaint.setColor(HOUR_MINUTE_COLOR);
        //添加阴影
        inCirclePaint.setShadowLayer(5, 0, 0, 0x80000000);

        inRedCirclePaint = new Paint();
        inRedCirclePaint.setAntiAlias(true);
        inRedCirclePaint.setColor(SECOND_COLOR);
        //添加阴影
        inRedCirclePaint.setShadowLayer(5, 0, 0, 0x80000000);

        textPaint = new Paint();
        textPaint.setTextSize(80);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mSize = dp2px(MIN_SIZE);
        setMeasuredDimension(mSize, mSize + 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            calendar = Calendar.getInstance();
        }
        getTime();
        //将画布移到中央
        canvas.translate(mSize / 2, mSize / 2);
        drawOutCircle(canvas);
        drawOutRing(canvas);
        drawScale(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawInCircle(canvas);
        if (isFirst) {
            drawSecond(canvas, second * DEGREE + 1);
        } else {
            drawSecond(canvas, second * DEGREE);
        }
        drawInRedCircle(canvas);
        drawText(canvas);
        if (isFirst) {
            //每隔1秒重绘View,重绘会调用onDraw()方法
            postInvalidateDelayed(1000);
        }
    }

    //获取时分秒
    private String getTime() {
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        Log.i(TAG, "getTime: " + hour + ":" + minute + ":" + second);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    //画表盘背景
    private void drawOutCircle(Canvas canvas) {
        canvas.drawCircle(0, 0, mSize / 2 - 4, outCirclePaint);
        canvas.save();
    }

    //画表盘最外层圆环
    private void drawOutRing(Canvas canvas) {
        canvas.save();
        float radius = mSize / 2 - dp2px(ringPaintWidth + 6) / 2;
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        outRingPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, 0, 360, false, outRingPaint);
        canvas.restore();
    }

    //画时间标志
    private void drawScale(Canvas canvas) {
        int radius = mSize / 2 - 60;
        //刻度弧紧靠进度弧
        canvas.save();
        int textSize = 15;
        timeTextPaint.setTextSize(sp2px(textSize));
        Paint.FontMetrics fm = timeTextPaint.getFontMetrics();
        float ascent = fm.ascent;
        float descent = fm.descent;
        float top = fm.top;
        float bottom = fm.bottom;
        float height = descent - ascent;

        float width = timeTextPaint.measureText("12");
        canvas.drawText("12", -width / 2, -radius + height - 2 * descent, timeTextPaint);

        width = timeTextPaint.measureText("3");
        canvas.drawText("3", radius - width, height / 2 - descent, timeTextPaint);

        width = timeTextPaint.measureText("6");
        canvas.drawText("6", -width / 2, radius, timeTextPaint);

        width = timeTextPaint.measureText("9");
        canvas.drawText("9", -radius, height / 2 - descent, timeTextPaint);

        //参照坐标系
        canvas.drawLine(-mSize >> 1, 0, mSize >> 1, 0, timeTextPaint);
        canvas.drawLine(0, -mSize >> 1, 0, mSize >> 1, timeTextPaint);
        canvas.restore();
    }

    //画时针
    private void drawHour(Canvas canvas) {
        int length = mSize / 4;
        canvas.save();
        //这里没有算秒钟对时钟的影响
        float degree = hour * 5 * DEGREE + minute / 2f;
        canvas.rotate(degree, 0, 0);
        canvas.drawLine(0, 0, 0, -length, hourPaint);
        canvas.restore();
    }

    //画分针
    private void drawMinute(Canvas canvas) {
        int length = mSize / 3;
        canvas.save();
        float degree = minute * DEGREE + second / 10f;
        canvas.rotate(degree, 0, 0);
        canvas.drawLine(0, 0, 0, -length, minutePaint);
        canvas.restore();
    }

    //画中心黑圆
    private void drawInCircle(Canvas canvas) {
        int radius = mSize / 20;
        canvas.save();
        canvas.drawCircle(0, 0, radius, inCirclePaint);
        canvas.restore();
    }

    //红色中心圆
    private void drawInRedCircle(Canvas canvas) {
        int radius = mSize / 40;
        canvas.save();
        canvas.drawCircle(0, 0, radius, inRedCirclePaint);
        canvas.restore();
    }

    //画秒针
    private void drawSecond(Canvas canvas, float degrees) {
        int length = mSize / 2;
        canvas.save();
        canvas.rotate(degrees);
        canvas.drawLine(0, length / 5, 0, -length * 4 / 5, secondPaint);
        canvas.restore();
        isFirst = !isFirst;
        if (!isFirst) {
            invalidate();
        }
    }

    //画时间
    private void drawText(Canvas canvas) {
        canvas.save();
        canvas.drawText(getTime(), -160, 440, textPaint);
        canvas.save();
    }

    //将 dp 转换为 px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
