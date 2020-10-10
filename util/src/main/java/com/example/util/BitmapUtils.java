
package com.example.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    /**
     * 将bitmap保存到本地
     */
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将bitmap保存到本地
     */
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取Bitmap图片
     *
     * @return bitmap
     */
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

    /**
     * 读取Bitmap图片
     *
     * @return bitmap
     */
    public static Bitmap readBitMap(Context context, int resId) {
        try {
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, getBitmapOption());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private static BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return opt;
    }

    /**
     * 调整图片大小
     *
     * @param context      上下文
     * @param resId        源图片ID
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap resizeBitmap(Context context, int resId, int targetWidth, int targetHeight) {
        if (null == context || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return resizeBitmap(readBitMap(context, resId), targetWidth, targetHeight);
    }

    /**
     * 调整图片大小
     *
     * @param filePath     源图片路径
     * @param targetWidth  目标高度
     * @param targetHeight 目标宽度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap resizeBitmap(Context context, String filePath, int targetWidth,
                                      int targetHeight) {
        if (TextUtils.isEmpty(filePath) || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return resizeBitmap(readBitMap(context, filePath), targetWidth, targetHeight);
    }

    /**
     * 调整图片大小
     *
     * @param bitmap       源图片
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (null == bitmap || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }

    /**
     * 转换成圆角图片
     *
     * @param context 上下文
     * @param resId   资源ID
     * @param pixels  弧度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap toRoundCornerImage(Context context, int resId, int width, int height, int pixels) {
        Bitmap bmp = readBitMap(context, resId);
        if (null == bmp) {
            return null;
        }
        bmp = resizeBitmap(bmp, width, height);
        return toRoundCornerImage(bmp, pixels);
    }

    /**
     * 转换成圆角图片
     *
     * @param context 上下文
     * @param path    源图片路径
     * @param pixels  弧度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap toRoundCornerImage(Context context, String path, int width, int height, int pixels) {
        Bitmap bmp = readBitMap(context, path);
        if (null == bmp) {
            return null;
        }
        bmp = resizeBitmap(bmp, width, height);
        return toRoundCornerImage(bmp, pixels);
    }

    /**
     * 转换成圆角图片
     *
     * @param bitmap 需要转换的图片
     * @param width  转换后的宽度
     * @param height 转换后的高度
     * @param pixels 弧度
     * @return 转换后的图片
     */
    public static Bitmap toRoundCornerImage(Bitmap bitmap, int width, int height, int pixels) {
        if (null == bitmap) {
            return null;
        }
        bitmap = resizeBitmap(bitmap, width, height);
        return toRoundCornerImage(bitmap, pixels);
    }

    /**
     * 转换成圆角图片
     *
     * @param bitmap 需转换的图片
     * @param pixels 弧度
     * @return 新图片, 参数错误返回null
     */
    public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
        if (null == bitmap || pixels <= 0) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        //抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 生成圆形图片
     *
     * @param source
     * @return 圆形图片
     */
    private static Bitmap circleBitmap(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有：
        //PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;
    }
}
