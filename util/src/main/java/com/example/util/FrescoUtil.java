package com.example.util;

import android.graphics.drawable.Animatable;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

public class FrescoUtil {
    private static final String TAG = "FrescoUtil";
    private static final String BASE_URL = "http://pic0.iqiyipic.com/image/20190805/97/2f/a_100193141_m_601_m10_720_405.jpg";
    private static final String BACKUP_URL = "http://pic0.iqiyipic.com/image/20190805/97/2f/a_100193141_m_601_m10_320_180.jpg";

    public static void loadImage(SimpleDraweeView simpleDraweeView) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(BASE_URL)
                .setTapToRetryEnabled(true)//设置点击重试,如果允许重试的话,会拦截ImageView的点击事件,可以重试4次.
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        Log.i(TAG, "onSubmit: ");
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        Log.i(TAG, "onFinalImageSet: ");
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        Log.i(TAG, "onIntermediateImageSet: ");
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        Log.i(TAG, "onIntermediateImageFailed: ");
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        Log.i(TAG, "onFailure: ");
                        //失败后加载backup图片
                        simpleDraweeView.setImageURI(BACKUP_URL);
                    }

                    @Override
                    public void onRelease(String id) {
                        Log.i(TAG, "onRelease: ");
                    }
                })
                .build();
        simpleDraweeView.setController(controller);
    }
}
