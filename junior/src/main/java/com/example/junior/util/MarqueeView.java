package com.example.junior.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MarqueeView extends View implements Runnable {
    private static final String TAG = "MarqueeView";
    public static final int REPEAT_ONCETIME = 0;//一次结束
    public static final int REPEAT_INTERVAL = 1;//一次结束以后,再继续第二次
    public static final int REPEAT_CONTINUE = 2;//紧接着,滚动第二次

    private int repeatType = REPEAT_CONTINUE;//滚动模式
    private String paintContent;//最终绘制的文本
    private float speed = 1;//移动速度
    private int textColor = Color.BLACK;//文字颜色,默认黑色
    private float textSize = 16;//文字颜色,默认黑色
    private int textDistance;//item间距,dp单位
    private int defaultTextDistance = 10;//item间距,dp单位
    private String black_count = "";//间距转化成空格距离

    private float startLocationDistance = 1.0f;//开始的位置选取,百分比来的,距离左边,0~1,0代表不间距,1的话代表,从右面,1/2代表中间。

    private boolean isClickStop = true;//点击是否暂停
    private boolean isResetLocation = true;//默认为true
    private float xLocation = 0;//文本的x坐标
    private int contentWidth;//内容的宽度

    private boolean isRoll = false;//是否继续滚动
    private float oneBlack_width;//空格的宽度

    private TextPaint paint;//画笔
    private Rect rect;

    private int repeatCount = 0;
    private boolean resetInit = true;

    private Thread thread;
    private String content = "";

    private float textHeight;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initClick();
    }

    private void initPaint() {
        rect = new Rect();
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    private void initClick() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickStop) {
                    if (isRoll) {
                        stopRoll();
                    } else {
                        continueRoll();
                    }
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resetInit) {
            setTextDistance(defaultTextDistance);
            if (startLocationDistance < 0) {
                startLocationDistance = 0;
            } else if (startLocationDistance > 1) {
                startLocationDistance = 1;
            }
            xLocation = getWidth() * startLocationDistance;
            resetInit = false;
        }

        //需要判断滚动模式的
        switch (repeatType) {
            //也就是说文字已经到头了,此时停止线程就可以了
            case REPEAT_INTERVAL:
                if (contentWidth <= (-xLocation)) {
                    xLocation = getWidth();
                }
                break;
            case REPEAT_CONTINUE:
                if (xLocation < 0) {
                    int beAppend = (int) ((-xLocation) / contentWidth);
                    Log.i(TAG, "onDraw: contentWidth = " + contentWidth + ", xLocation = " + (-xLocation) + ", beAppend = " + beAppend);
                    if (beAppend >= repeatCount) {
                        repeatCount++;
                        //也就是说文字已经到头了
                        paintContent = paintContent + content;
                    }
                }
                //此处需要判断的xLocation需要加上相应的宽度
                break;
            default:
                //默认一次到头好了
                if (contentWidth < (-xLocation)) {
                    //也就是说文字已经到头了,此时停止线程就可以了
                    stopRoll();
                }
                break;
        }

        //把文字画出来
        if (paintContent != null) {
            canvas.drawText(paintContent, xLocation, getHeight() / 2 + textHeight / 2, paint);
        }
    }

    @Override
    public void run() {
        while (isRoll && !TextUtils.isEmpty(content)) {
            try {
                Thread.sleep(10);
                xLocation = xLocation - speed;
                postInvalidate();//每隔10毫秒重绘视图
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 继续滚动
     */
    public void continueRoll() {
        if (!isRoll) {
            if (thread != null) {
                thread.interrupt();
                thread = null;
            }
            isRoll = true;
            thread = new Thread(this);
            thread.start();//开启死循环线程让文字动起来
        }
    }

    /**
     * 停止滚动
     */
    public void stopRoll() {
        isRoll = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    //设置文字间距,不过如果内容是List形式的,该方法不适用 ,list的数据源,必须在设置setContent之前调用此方法。
    public void setTextDistance(int textDistance) {
        //设置之后就需要初始化了
        String black = " ";
        oneBlack_width = getBlackWidth();//空格的宽度
        int count = (int) (textDistance / oneBlack_width);//空格个数,有点粗略,有兴趣的朋友可以精细
        if (count == 0) {
            count = 1;
        }
        this.textDistance = (int) (oneBlack_width * count);
        black_count = "";
        for (int i = 0; i <= count; i++) {
            black_count = black_count + black;//间隔字符串
        }
        setContent(content);//设置间距以后要重新刷新内容距离
    }

    //计算出一个空格的宽度
    private float getBlackWidth() {
        String text1 = "e n";
        String text2 = "en";
        return getContentWidth(text1) - getContentWidth(text2);
    }

    private float getContentWidth(String black) {
        if (TextUtils.isEmpty(black)) {
            return 0;
        }
        if (rect == null) {
            rect = new Rect();
        }
        paint.getTextBounds(black, 0, black.length(), rect);
        textHeight = getContentHeight();
        return rect.width();
    }

    private float getContentHeight() {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs((fontMetrics.bottom - fontMetrics.top)) / 2;
    }

    //设置文字颜色
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(getResources().getColor(textColor));//文字颜色值,可以不设定
    }

    //设置文字大小
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        paint.setTextSize(textSize);//文字颜色值,可以不设定
        contentWidth = (int) (getContentWidth(content) + textDistance);//大小改变,需要重新计算宽高
    }

    //设置滚动速度
    public void setTextSpeed(float speed) {
        this.speed = speed;
    }

    //设置滚动的条目内容  字符串形式的
    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (isResetLocation) {//控制重新设置文本内容的时候,是否初始化xLocation。
            xLocation = getWidth() * startLocationDistance;
        }
        if (!content.endsWith(black_count)) {
            content = content + black_count;//避免没有后缀
        }
        this.content = content;
        //这里需要计算宽度啦,当然要根据模式来搞
        if (repeatType == REPEAT_CONTINUE) {
            //如果说是循环的话,则需要计算文本的宽度,然后再根据屏幕宽度,看能一个屏幕能盛得下几个文本
            contentWidth = (int) (getContentWidth(this.content) + textDistance);//可以理解为一个单元内容的长度
            //从0 开始计算重复次数了, 否则到最后 会跨不过这个坎而消失。
            repeatCount = 0;
            int contentCount = (getWidth() / contentWidth) + 2;
            this.paintContent = "";
            for (int i = 0; i <= contentCount; i++) {
                this.paintContent = this.paintContent + this.content;//根据重复次数去叠加。
            }
        } else {
            if (xLocation < 0 && repeatType == REPEAT_ONCETIME) {
                if (-xLocation > contentWidth) {
                    xLocation = getWidth() * startLocationDistance;
                }
            }
            contentWidth = (int) getContentWidth(this.content);
            this.paintContent = content;
        }
        if (!isRoll) {//如果没有在滚动的话,重新开启线程滚动
            continueRoll();
        }
    }
}
