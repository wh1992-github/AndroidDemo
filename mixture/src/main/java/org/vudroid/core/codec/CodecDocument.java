package org.vudroid.core.codec;
/**
 * 用于约束 Codec Document 相关能力的接口。
 */

public interface CodecDocument {
    CodecPage getPage(int pageNumber);

    int getPageCount();

    void recycle();
}
