package com.example.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideUtil {
    private static final String TAG = "GlideUtil";
    private static final String BASE_URL = "http://b-ssl.duitang.com/uploads/item/201809/26/20180926162125_vjbwi.jpg";

    public static void loadImage(Context context, ImageView imageView) {
        //RequestOptions options = RequestOptions.noAnimation()
        RequestOptions options = new RequestOptions()
                .transforms(new CenterCrop(), new RoundedCorners(40))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                //指定图片的尺寸
                //.override(400, 400)
                //指定图片的缩放类型为fitCenter (等比例缩放图片,宽或者高等于ImageView的宽或者高)
                //.fitCenter()
                //指定图片的缩放类型为centerInside (等比例缩放图片,直到图片的宽高都小于等于ImageView的宽高)
                //.centerInside()
                //指定图片的缩放类型为centerCrop (等比例缩放图片,直到图片的宽高都大于等于ImageView的宽高,然后截取中间的显示)
                //.centerCrop()
                //相当于指定图片的缩放类型为centerCrop,并且以圆形显示
                //.circleCrop()
                //是否跳过内存缓存
                .skipMemoryCache(false)
                //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //跳过磁盘缓存
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //只缓存原来分辨率的图片
                //.diskCacheStrategy(DiskCacheStrategy.DATA)
                //只缓存最终的图片
                //.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                ;

        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                LogUtil.i(TAG, "onLoadFailed: ");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                LogUtil.i(TAG, "onResourceReady: ");
                return false;
            }
        };

        Glide.with(context)
                .load(BASE_URL)
                .addListener(requestListener)
                .apply(options)
                .into(imageView);
    }
}
