package org.vudroid.core.codec;

import android.graphics.Bitmap;
import android.graphics.RectF;
/**
 * 用于约束 Codec Page 相关能力的接口。
 */

public interface CodecPage {
    boolean isDecoding();

    void waitForDecode();

    int getWidth();

    int getHeight();

    Bitmap renderBitmap(int width, int height, RectF pageSliceBounds);

    void recycle();
}
