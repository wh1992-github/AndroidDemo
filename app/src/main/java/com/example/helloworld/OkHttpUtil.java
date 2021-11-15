package com.example.helloworld;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static volatile OkHttpUtil sInstance;
    private OkHttpClient mOkHttpClient;

    private OkHttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public static OkHttpUtil getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpUtil.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpUtil();
                }
            }
        }
        return sInstance;
    }

    public void requestImage(String url) {
        Request request = new Request.Builder().get().url(url).header("key", "okhttp_key").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: e = " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    ResponseBody responseBody = response.body();
                    InputStream inputStream = responseBody.byteStream();
                    long totalLength = responseBody.contentLength();
                    int currentLength = 0;
                    int length;
                    byte[] buffer = new byte[1024 * 100];
                    FileOutputStream fileOutputStream = new FileOutputStream("sdcard/AAA/" + System.currentTimeMillis() + ".jpg");
                    while ((length = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                        currentLength += length;
                        Log.i(TAG, "requestData: current = " + currentLength + ", total = " + totalLength);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                    Log.i(TAG, "run: code = " + response.code());
                } catch (IOException e) {
                    Log.i(TAG, "onResponse: e = " + e.getMessage());
                }
            }
        });
    }
}
