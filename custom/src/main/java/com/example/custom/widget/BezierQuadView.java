package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class BezierQuadView extends View {
    private int eventX, eventY;
    private int centerX, centerY;
    private int startX, startY;
    private int endX, endY;
    private Paint paint;

    public BezierQuadView(Context context) {
        this(context, null);
    }

    public BezierQuadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierQuadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    //测量大小完成以后回调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
        startX = centerX - 250;
        startY = centerY;
        endX = centerX + 250;
        endY = centerY;
        eventX = centerX;
        eventY = centerY - 250;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.GRAY);
        //画3个点
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, 10, paint);
        canvas.drawCircle(endX, endY, 10, paint);
        canvas.drawCircle(eventX, eventY, 10, paint);

        //绘制连线
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(startX, startY, eventX, eventY, paint);
        canvas.drawLine(eventX, eventY, endX, endY, paint);

        //画二阶贝塞尔曲线
        paint.setColor(Color.GREEN);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(eventX, eventY, endX, endY);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                invalidate();
                break;
        }
        return true;
    }
}
