package com.example.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;

import java.util.Locale;

public class FrescoConfigUtil {
    private static final String TAG = "FrescoConfigUtil";
    //分配的可用内存
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    //使用的缓存数量
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    //小图极低磁盘空间缓存的最大值（特性:可将大量的小图放到另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 20 * ByteConstants.MB;
    //小图低磁盘空间缓存的最大值（特性:可将大量的小图放到另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 40 * ByteConstants.MB;
    //默认图极低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 20 * ByteConstants.MB;
    //默认图低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_LOW_SIZE = 40 * ByteConstants.MB;
    //磁盘缓存的最大值
    private static final int MAX_DISK_CACHE_SIZE = 80 * ByteConstants.MB;

    //小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "ImagePipelineCacheSmall";
    //默认图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "ImagePipelineCacheDefault";

    //ImagePipelineConfig 的完整配置
    @SuppressWarnings({"MalformedFormatString", "RedundantSuppression"})
    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {
        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE,//内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,//内存缓存中图片的最大数量。
                MAX_MEMORY_CACHE_SIZE,//内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,//内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);//内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量,空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = () -> bitmapCacheParams;

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getExternalCacheDir())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getExternalCacheDir())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true);

        //这段代码,用于清理缓存
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(trimType -> {
            double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
            String suggestedValue = getValue(suggestedTrimRatio);
            String closeToDalvikHeapLimit = getValue(MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio());
            String whileAppInBackground = getValue(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio());
            String whileAppInForeground = getValue(MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio());
            if (suggestedValue.equals(closeToDalvikHeapLimit) || suggestedValue.equals(whileAppInBackground) || suggestedValue.equals(whileAppInForeground)) {
                ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
            }
        });
        return configBuilder.build();
    }

    private static String getValue(double d) {
        LogUtil.i(TAG, "getValue: d = " + d);
        return String.format(Locale.getDefault(), "%.2f", d);
    }
}
