package com.example.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class StringBitmap {

    //String文本转化为Bitmap
    public static Bitmap drawString() {
        int w = 300, h = 100;
        String title = "Android的新体验";
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasTemp = new Canvas(bitmap);
        canvasTemp.drawColor(Color.WHITE);
        Paint paint = new Paint();
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setTypeface(font);
        paint.setTextSize(26);

        Paint.FontMetrics fm = paint.getFontMetrics();
        float ascent = fm.ascent;
        float descent = fm.descent;
        float height = descent - ascent;
        float width = paint.measureText(title);

        canvasTemp.drawText(title, (w - width) / 2, (h + height) / 2 - descent, paint);

        return bitmap;
    }
}