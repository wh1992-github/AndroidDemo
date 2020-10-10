package com.example.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;

public class SystemPathUtil {
    private static final String TAG = "SystemPathUtil";

    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    public static String getSDCardPath() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    @SuppressLint("UsableSpace")
    public static void getSystemSpace(Context context) {
        if (isSDCardMounted()) {
            //获得路径
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            //获得sdcard上 block的总数
            long blockCount = statFs.getBlockCount();
            //获得sdcard上每个block 的大小
            long blockSize = statFs.getBlockSize();
            //获取可供程序使用的Block数量
            long availCount = statFs.getAvailableBlocks();
            long totalSize = blockCount * blockSize;
            long availableSize = availCount * blockSize;

            Log.d(TAG, "onCreate: total = " + Formatter.formatFileSize(context, totalSize));
            Log.d(TAG, "onCreate: available = " + Formatter.formatFileSize(context, availableSize));
            Log.d(TAG, "onCreate: used = " + Formatter.formatFileSize(context, (totalSize - availableSize)));

            //获取可供程序使用的Block数量
            totalSize = file.getTotalSpace();
            availableSize = file.getUsableSpace();

            Log.d(TAG, "onCreate: total = " + Formatter.formatFileSize(context, totalSize));
            Log.d(TAG, "onCreate: available = " + Formatter.formatFileSize(context, availableSize));
            Log.d(TAG, "onCreate: used = " + Formatter.formatFileSize(context, (totalSize - availableSize)));
        }
    }

    private static void systemPath(Context context) {
        //获得SD卡目录/mnt/sdcard（获取的是手机外置sd卡的路径）
        Log.d(TAG, "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory().getPath());
        //获得系统目录/system
        Log.d(TAG, "Environment.getRootDirectory(): " + Environment.getRootDirectory().getPath());
        //获得根目录/data内部存储路径
        Log.d(TAG, "Environment.getDataDirectory(): " + Environment.getDataDirectory().getPath());
        //获得缓存目录/cache
        Log.d(TAG, "Environment.getDownloadCacheDirectory(): " + Environment.getDownloadCacheDirectory().getPath());
        //用于获取APP的在SD卡中的cache目录 /sdcard/Android/data/<application>/cache
        Log.d(TAG, "getExternalCacheDir(): " + context.getExternalCacheDir().getPath());
        //用于获取APP的cache目录 /data/data/<application>/cache
        Log.d(TAG, "getCacheDir(): " + context.getCacheDir().getPath());
        //用于获取APP的files目录 /data/data/<application>/files
        Log.d(TAG, "getFilesDir(): " + context.getFilesDir().getPath());
    }
}
