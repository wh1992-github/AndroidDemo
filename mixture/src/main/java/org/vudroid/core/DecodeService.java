package org.vudroid.core;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.view.View;
/**
 * 用于处理 Decode 相关后台工作的 Service。
 */

public interface DecodeService {
    void setContentResolver(ContentResolver contentResolver);

    void setContainerView(View containerView);

    void open(Uri fileUri);

    void decodePage(Object decodeKey, int pageNum, DecodeCallback decodeCallback, float zoom, RectF pageSliceBounds);

    void stopDecoding(Object decodeKey);

    int getEffectivePagesWidth();

    int getEffectivePagesHeight();

    int getPageCount();

    int getPageWidth(int pageIndex);

    int getPageHeight(int pageIndex);

    void recycle();

    interface DecodeCallback {
        void decodeComplete(Bitmap bitmap);
    }
}
