
package com.example.custom.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    //将bitmap保存到本地
    public static boolean saveBitmapToWEBP(Bitmap bmp, String path) {
        if (null == bmp || TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            File dir = new File(f.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.WEBP, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //将bitmap保存到本地
    public static boolean saveBitmapToPNG(Bitmap bmp, String path) {
        if (null == bmp || TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //读取Bitmap图片
    public static Bitmap readBitMap(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            try {
                InputStream is = new FileInputStream(path);
                return BitmapFactory.decodeStream(is, null, getBitmapOption());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //读取Bitmap图片
    public static Bitmap readBitMap(Context context, int resId) {
        try {
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, getBitmapOption());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //调整图片大小
    public static Bitmap resizeBitmap(Context context, int resId, int targetWidth, int targetHeight) {
        if (null == context || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return resizeBitmap(readBitMap(context, resId), targetWidth, targetHeight);
    }

    //调整图片大小
    public static Bitmap resizeBitmap(Context context, String filePath, int targetWidth, int targetHeight) {
        if (TextUtils.isEmpty(filePath) || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return resizeBitmap(readBitMap(context, filePath), targetWidth, targetHeight);
    }

    //调整图片大小
    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (null == bitmap || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }

    //生成圆形图片
    public static Bitmap createCircleView(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) / 2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(width >> 1, height >> 1, radius, paint);
        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有：
        //PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return target;
    }

    //生成圆角图片
    public static Bitmap createRoundRectView(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, 20, 20, paint);
        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有：
        //PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return target;
    }

    //二次采样,内存优化
    @SuppressWarnings("deprecation")
    private static BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }
}
