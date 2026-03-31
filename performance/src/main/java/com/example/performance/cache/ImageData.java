package com.example.performance.cache;

import android.graphics.Bitmap;
/**
 * 用于描述 Image 数据的实体类。
 */

public class ImageData {
    public String url; //图片地址
    public Bitmap bitmap; //位图数据

    public ImageData(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

}
