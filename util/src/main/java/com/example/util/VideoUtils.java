package com.example.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.HashMap;

public class VideoUtils {

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图,然后再利用ThumbnailUtils来生成指定大小的缩略图。 如果想要的缩略图的宽和高都小于MICRO_KIND,则类型要使用MICRO_KIND作为kind的值,这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。 其中,MINI_KIND: 512 x 384,MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        //获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        return createVideoThumbnail(url, width, height, -1);
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height, long timeUs) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            retriever.setDataSource(url, new HashMap<>());
            if (-1 == timeUs) {
                bitmap = retriever.getFrameAtTime();
            } else {
                bitmap = retriever.getFrameAtTime(timeUs);
            }
        } catch (IllegalArgumentException ex) {
            //Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            //Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException | IOException ex) {
                //Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }
}
