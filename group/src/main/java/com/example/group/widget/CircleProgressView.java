package com.example.group.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircleProgressView extends View {
    private static final String TAG = "CircleProgressView";
    //画笔对象的引用
    private Paint paint;
    //圆环的颜色
    private int roundColor = Color.GRAY;
    //圆环进度的颜色
    private int roundProgressColor = Color.RED;
    //圆环的宽度
    private float roundWidth = 7;
    //最大进度
    private int max = 100;
    //当前进度
    private int progress;
    //中间进度百分比的字符串的字体
    private float textSize = 60;
    //点的半径
    private float pointRadius = 6;
    //空心点的宽度
    private float pointWidth = 8;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //画最外层的大圆环
        paint.setColor(Color.GRAY); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出圆环

        //画圆心
        paint.setStrokeWidth(roundWidth);
        paint.setColor(Color.GRAY);
        canvas.drawPoint(centerX, centerY, paint);

        //画圆弧 ，画圆环的进度
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);  //用于定义的圆弧的形状和大小的界限

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧

        //画圆上的两个点
        paint.setStrokeWidth(pointWidth);
        PointF startPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 0, 270);
        canvas.drawCircle(startPoint.x, startPoint.y, pointRadius, paint);
        paint.setStrokeWidth(pointWidth + 6);
        PointF progressPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progress / max, 270);
        canvas.drawCircle(progressPoint.x, progressPoint.y, pointRadius, paint);

        //画文字
        paint.setStrokeWidth(2);
        paint.setColor(roundProgressColor);
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setTextSize(textSize);
        //paint.setTypeface(Typeface.DEFAULT); //设置字体
        String textTime = getTimeText(progress);
        float textWidth = paint.measureText(textTime);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        canvas.drawText(textTime, centerX - textWidth / 2, centerY + textSize / 3, paint);
    }

    private boolean downOnArc = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchArc(x, y)) {
                    downOnArc = true;
                    updateArc(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnArc) {
                    updateArc(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downOnArc = false;
                invalidate();
                if (changeListener != null) {
                    changeListener.onProgressChangeEnd(max, progress);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int centerX, centerY;
    private int radius;
    private int paddingOuterThumb = 20;

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged: w = " + width + ", h = " + height);
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);
        radius = minCenter - 50; //圆环的半径
        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb * 1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb * 1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }

    //根据点的位置，更新进度
    private void updateArc(int x, int y) {
        int cx = x - centerX;
        int cy = y - centerY;
        //计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
        double angle = Math.atan2(cy, cx) / Math.PI;
        //将角度转换成（0->2）之间的值，然后加上90°的偏移量
        angle = ((2 + angle) % 2 + (90 / 180f)) % 2;
        //用（0->2）之间的角度值乘以总进度，等于当前进度
        progress = (int) (angle * max / 2);
        if (changeListener != null) {
            changeListener.onProgressChange(max, progress);
        }
        invalidate();
    }

    private int minValidateTouchArcRadius; //最小有效点击半径
    private int maxValidateTouchArcRadius; //最大有效点击半径

    //判断是否按在圆边上
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {
            return true;
        }
        return false;
    }

    //计算某点到圆点的距离
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    private String getTimeText(int progress) {
        int minute = progress / 60;
        int second = progress % 60;
        String result = (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
        return result;
    }

    public synchronized int getMax() {
        return max;
    }

    //设置进度的最大值
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    //获取进度.需要同步
    public synchronized int getProgress() {
        return progress;
    }

    //设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
    //刷新界面调用postInvalidate()能在非UI线程刷新
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCircleColor() {
        return roundColor;
    }

    public void setCircleColor(int circleColor) {
        this.roundColor = circleColor;
    }

    public int getCircleProgressColor() {
        return roundProgressColor;
    }

    public void setCircleProgressColor(int circleProgressColor) {
        this.roundProgressColor = circleProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public static class ChartUtil {
        //依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle) {
            float posX = 0.0f;
            float posY = 0.0f;
            //将角度转换为弧度
            float arcAngle = (float) (Math.PI * cirAngle / 180.0);
            if (cirAngle < 90) {
                posX = cirX + (float) (Math.cos(arcAngle)) * radius;
                posY = cirY + (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 90) {
                posX = cirX;
                posY = cirY + radius;
            } else if (cirAngle > 90 && cirAngle < 180) {
                arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
                posX = cirX - (float) (Math.cos(arcAngle)) * radius;
                posY = cirY + (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 180) {
                posX = cirX - radius;
                posY = cirY;
            } else if (cirAngle > 180 && cirAngle < 270) {
                arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
                posX = cirX - (float) (Math.cos(arcAngle)) * radius;
                posY = cirY - (float) (Math.sin(arcAngle)) * radius;
            } else if (cirAngle == 270) {
                posX = cirX;
                posY = cirY - radius;
            } else {
                arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
                posX = cirX + (float) (Math.cos(arcAngle)) * radius;
                posY = cirY - (float) (Math.sin(arcAngle)) * radius;
            }
            return new PointF(posX, posY);
        }

        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle) {
            cirAngle = (orginAngle + cirAngle) % 360;
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
        }
    }

    private OnProgressChangeListener changeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);

        void onProgressChangeEnd(int duration, int progress);
    }
}