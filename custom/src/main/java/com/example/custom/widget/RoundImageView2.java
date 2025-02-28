package com.example.custom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

@SuppressLint("DrawAllocation")
public class RoundImageView2 extends AppCompatImageView {
    private static final int radius = 20;
    private Paint mPaint;
    private Xfermode mXfermode;

    public RoundImageView2(Context context) {
        this(context, null);
    }

    public RoundImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //画源图像，为一个圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //画目标图像
        canvas.drawBitmap(drawableToBitmap(exChangeSize(getDrawable())), 0, 0, mPaint);
        //还原混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    //图片拉升
    private Drawable exChangeSize(Drawable drawable) {
        float scale = 1.0f;
        scale = Math.max(getWidth() * 1.0f / drawable.getIntrinsicWidth(), getHeight()
                * 1.0f / drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, (int) (scale * drawable.getIntrinsicWidth()),
                (int) (scale * drawable.getIntrinsicHeight()));
        return drawable;
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        //当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
