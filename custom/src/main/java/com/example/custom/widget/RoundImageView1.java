package com.example.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

public class RoundImageView1 extends AppCompatImageView {
    private static final String TAG = "CustomRoundImageView";
    private static final int radius = 60;
    private float width, height;

    public RoundImageView1(Context context) {
        this(context, null);
    }

    public RoundImageView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        Log.i(TAG, "onLayout: w = " + width + ", h = " + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        //四个角：右上，右下，左下，左上
        path.moveTo(radius, 0);
        path.lineTo(width - radius, 0);
        path.quadTo(width, 0, width, radius);

        path.lineTo(width, height - radius);
        path.quadTo(width, height, width - radius, height);

        path.lineTo(radius, height);
        path.quadTo(0, height, 0, height - radius);

        path.lineTo(0, radius);
        path.quadTo(0, 0, radius, 0);

        canvas.clipPath(path);
        Log.i(TAG, "onDraw: radius = " + radius);
        super.onDraw(canvas);
    }
}