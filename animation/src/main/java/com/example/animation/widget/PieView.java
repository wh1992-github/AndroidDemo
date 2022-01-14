package com.example.animation.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class PieView extends View {
    private static final String TAG = "PieAnimation";
    private static final int mEndAngle = 270; //结束角度
    private static final int mInterval = 70; //间隔时间,单位毫秒
    private static final int mIncrease = 10; //每次绘制增加的度数
    private int mStartAngle = 0; //当前绘制的角度
    private final Paint mPaint; //声明一个画笔对象
    private final Handler mHandler = new Handler(); //声明一个处理器对象
    private boolean isRunning = false; //是否正在播放

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(); //创建一个新的画笔
        mPaint.setAntiAlias(true); //设置画笔为无锯齿
        mPaint.setColor(Color.GREEN); //设置画笔的颜色
        mPaint.setStyle(Style.FILL); //设置画笔的类型。STROKE表示空心,FILL表示实心
    }

    //开始播放动画
    public void start() {
        mStartAngle = 0;
        isRunning = true;
        //立即启动绘图刷新任务
        mHandler.post(mRefresh);
    }

    //是否正在播放
    public boolean isRunning() {
        return isRunning;
    }

    //定义一个绘图刷新任务
    private final Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            mStartAngle += mIncrease;
            if (mStartAngle <= mEndAngle) { //未绘制完成
                postInvalidate(); //立即刷新视图
                //延迟若干时间后再次启动绘图刷新任务
                mHandler.postDelayed(this, mInterval);
            } else { //已绘制完成
                isRunning = false;
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRunning) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            //视图的宽高取较小的那个作为扇形的直径
            int diameter = Math.min(width, height);
            //创建扇形的矩形边界
            RectF rectf = new RectF((width - diameter) / 2, (height - diameter) / 2,
                    (width + diameter) / 2, (height + diameter) / 2);
            //在画布上绘制指定角度的扇形。第四个参数为true表示绘制扇形,为false表示绘制圆弧
            canvas.drawArc(rectf, 0, mStartAngle, true, mPaint);
        }
    }

}
