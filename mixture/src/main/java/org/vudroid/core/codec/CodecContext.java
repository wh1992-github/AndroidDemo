package org.vudroid.core.codec;

import android.content.ContentResolver;
/**
 * 用于约束 Codec Context 相关能力的接口。
 */

public interface CodecContext {
    CodecDocument openDocument(String fileName);

    void setContentResolver(ContentResolver contentResolver);

    void recycle();
}
