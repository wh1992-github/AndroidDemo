package com.example.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CircleCornerView extends LinearLayout {
    private static final int radius = 28;
    private float width, height;
    private final Path mPath = new Path();

    public CircleCornerView(Context context) {
        this(context, null);
    }

    public CircleCornerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCornerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //四个角：右上，右下，左下，左上
        mPath.moveTo(radius, 0);
        mPath.lineTo(width - radius, 0);
        mPath.quadTo(width, 0, width, radius);

        mPath.lineTo(width, height - radius);
        mPath.quadTo(width, height, width - radius, height);

        mPath.lineTo(radius, height);
        mPath.quadTo(0, height, 0, height - radius);

        mPath.lineTo(0, radius);
        mPath.quadTo(0, 0, radius, 0);

        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}