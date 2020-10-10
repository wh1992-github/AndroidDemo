package com.example.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static final int SUCCESS_CODE = 200;
    private static volatile OkHttpUtil sInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private OkHttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
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

    public void getAsyncHttp(String url, ResultCallback resultCallback) {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> resultCallback.onError(call.request().toString() + ", error = " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                mHandler.post(() -> resultCallback.onSuccess(bitmap));
            }
        });
    }

    //put方式 上传文件
    public void putData(String url, File file) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), file);
        //header参数
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("key", "value");
        Headers headers = headerBuilder.build();
        Request request = new Request.Builder().put(requestBody).headers(headers).url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        int code = response.code();
        if (code == SUCCESS_CODE) {
            String string = response.body().string();
            LogUtil.i(TAG, "response1 = " + string);
        } else {
            LogUtil.i(TAG, "failed: code1 = " + code);
        }
    }

    //post Map
    public void postMap(String url, Map<String, Object> map) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            RequestBody responseBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            //header参数
            Headers.Builder headerBuilder = new Headers.Builder();
            headerBuilder.add("key", "value");
            Headers headers = headerBuilder.build();
            Request request = new Request.Builder().post(responseBody).headers(headers).url(url).build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            int code = response.code();
            if (code == SUCCESS_CODE) {
                String string = response.body().string();
                LogUtil.i(TAG, "response2 = " + string);
            } else {
                LogUtil.i(TAG, "failed: code2 = " + code);
            }
        } catch (IOException | JSONException e) {
            LogUtil.e(TAG, "Exception = " + e.getMessage());
        }
    }

    //post方式 表单上传文件
    public void postFile(String url, JSONObject jsonObject) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Iterator<String> iterator = jsonObject.keys();
        try {
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                if ("key1".equals(key)) {
                    LogUtil.i(TAG, "key1 = " + key + ", value1 = " + value);
                    if (!TextUtils.isEmpty(value)) {
                        File file = new File(value);
                        if (file.exists()) {
                            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), file);
                            builder.addFormDataPart(key, file.getName(), fileBody);
                        }
                    }
                }
                if ("key2".equals(key)) {
                    LogUtil.i(TAG, "key2 = " + key + ", value2 = " + value);
                    builder.addFormDataPart(key, value);
                }
                if ("key3".equals(key)) {
                    LogUtil.i(TAG, "key3 = " + key + ", value3 = " + value);
                    builder.addFormDataPart(key, value);
                }
                if ("key4".equals(key)) {
                    LogUtil.i(TAG, "key4 = " + key + ", value4 = " + value);
                    builder.addFormDataPart(key, value);
                }
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            int code = response.code();
            if (code == SUCCESS_CODE) {
                String string = response.body().string();
                LogUtil.i(TAG, "response3 = " + string);
            } else {
                LogUtil.i(TAG, "failed: code3 = " + code);
            }
        } catch (JSONException | IOException e) {
            LogUtil.e(TAG, "Exception = " + e.getMessage());
        }
    }

    public interface ResultCallback {
        void onSuccess(Bitmap bitmap);

        void onError(String error);
    }
}
