package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.custom.R;

/**
 * Created by test
 */
@SuppressLint("DrawAllocation")
public class ScanningView2 extends View {
    private final Context mContext;
    private Paint mPaintLine;//画圆
    private Paint mPaintIcon;//画中间的图
    private Paint mPaintScan;//画扫描
    private Shader mScanShader;//扫描渲染shader
    private final Matrix mMatrix = new Matrix();//旋转需要的矩阵
    private int mScanAngle = 0;//扫描旋转的角度

    private Bitmap mCenterBitmap;

    private final float mLineWidth = 5f;
    private final float[] mCircleProportion = {1 / 8f, 2 / 8f, 3 / 8f, 4 / 8f};

    private int mWidth, mHeight;

    public ScanningView2(Context context) {
        this(context, null);
    }

    public ScanningView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanningView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void startAnim() {
        post(run);
    }

    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            mScanAngle = (mScanAngle + 1) % 360;
            mMatrix.postRotate(1, mWidth / 2, mHeight / 2);
            invalidate();
            postDelayed(run, 10);
        }
    };

    private void init() {
        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(ContextCompat.getColor(mContext, R.color.black));
        mPaintLine.setStrokeWidth(mLineWidth);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintIcon = new Paint();
        mPaintIcon.setAntiAlias(true);
        mPaintIcon.setColor(ContextCompat.getColor(mContext, R.color.black));

        mPaintScan = new Paint();
        mPaintScan.setAntiAlias(true);
        mPaintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.device_searching);
        mScanShader = new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.TRANSPARENT, ContextCompat.getColor(mContext, R.color.red)}, null);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawIcon(canvas);
        drawCross(canvas);
        drawScan1(canvas);
        drawScan2(canvas);
    }

    private void drawCircle(Canvas canvas) {
        mPaintLine.setStrokeWidth(mLineWidth / 2);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[0] - mLineWidth / 4, mPaintLine);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[1] - mLineWidth / 4, mPaintLine);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[2] - mLineWidth / 4, mPaintLine);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[3] - mLineWidth / 4, mPaintLine);
    }

    private void drawIcon(Canvas canvas) {
        canvas.drawBitmap(mCenterBitmap, null, new Rect((int) (mWidth / 2 - mWidth * mCircleProportion[0]),
                        (int) (mHeight / 2 - mHeight * mCircleProportion[0]),
                        (int) (mWidth / 2 + mWidth * mCircleProportion[0]),
                        (int) (mHeight / 2 + mHeight * mCircleProportion[0])),
                mPaintIcon);
    }

    private void drawCross(Canvas canvas) {
        mPaintLine.setStrokeWidth(mLineWidth / 2);
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaintLine);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaintLine);
    }

    private void drawScan1(Canvas canvas) {
        int count = canvas.save();
        mPaintScan.setShader(mScanShader);
        canvas.concat(mMatrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[3], mPaintScan);
        canvas.restoreToCount(count);
    }

    private void drawScan2(Canvas canvas) {
        int count = canvas.save();
        mPaintScan.setShader(mScanShader);
        canvas.rotate(mScanAngle, mWidth / 2, mHeight / 2);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * mCircleProportion[3], mPaintScan);
        canvas.restoreToCount(count);
    }
}
