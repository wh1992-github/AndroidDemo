package com.example.group.retrofit;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group.R;
import com.example.group.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的简单使用
 */
@SuppressLint("LogNotTimber")
public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";
    private TextView mTextView;
    private ImageView imageView;
    private RetrofitAPI mRetrofitAPI;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mTextView = findViewById(R.id.tv);
        imageView = findViewById(R.id.iv);

        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(chain -> {
                    HttpUrl httpUrl = chain.request().url().newBuilder().build();
                    Log.i(TAG, "url = " + httpUrl.toString());
                    Request request = chain.request().newBuilder().url(httpUrl)
                            .header("Connection", "close")
                            .header("Accept-Encoding", "identity")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        //步骤4:构建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl("https://api.uomg.com/")
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                //使用rxjava的adapter
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        //步骤5:创建网络请求接口对象实例
        mRetrofitAPI = retrofit.create(RetrofitAPI.class);

        findViewById(R.id.btn1).setOnClickListener(v -> getJsonData());
        findViewById(R.id.btn2).setOnClickListener(v -> postJsonData());
        findViewById(R.id.btn3).setOnClickListener(v -> loadBitmap());
    }

    //RxJava加载图片
    private void loadBitmap() {
        Observable<ResponseBody> observable = mRetrofitAPI.getPic();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        LogUtil.i(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NotNull ResponseBody responseBody) {
                        LogUtil.i(TAG, "onNext: " + Thread.currentThread().getName());
                        imageView.setImageBitmap(BitmapFactory.decodeStream(responseBody.byteStream()));
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        LogUtil.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i(TAG, "onComplete: ");
                    }
                });
    }


    /**
     * 示例，get加载Json数据
     */
    private void getJsonData() {
        //步骤6：对发送请求进行封装，传入接口参数
        Call<RetrofitData<RetrofitInfo>> jsonDataCall = mRetrofitAPI.getJsonData("新歌榜", "json");

        Log.i(TAG, "getJsonData: url = " + jsonDataCall.request().url());
        //步骤7:发送网络请求
        //同步执行
        //Response<RetrofitData<RetrofitInfo>> execute = jsonDataCall.execute();
        //异步执行
        jsonDataCall.enqueue(new Callback<RetrofitData<RetrofitInfo>>() {
            @Override
            public void onResponse(Call<RetrofitData<RetrofitInfo>> call, Response<RetrofitData<RetrofitInfo>> response) {
                //步骤8：请求处理,输出结果
                Toast.makeText(RetrofitActivity.this, "get回调成功:异步执行", Toast.LENGTH_SHORT).show();
                RetrofitData<RetrofitInfo> body = response.body();
                if (body == null) {
                    return;
                }
                RetrofitInfo retrofitInfo = body.getData();
                if (retrofitInfo == null) {
                    return;
                }
                mTextView.setText("返回的数据：" + "\n" + retrofitInfo.getName() + "\n" + retrofitInfo.getPicUrl());
            }

            @Override
            public void onFailure(Call<RetrofitData<RetrofitInfo>> call, Throwable t) {
                Log.i(TAG, "get回调失败：" + t.getMessage());
                Toast.makeText(RetrofitActivity.this, "get回调失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 示例，Post加载Json数据
     */
    private void postJsonData() {
        //步骤6：对发送请求进行封装:传入参数
        Call<Object> call = mRetrofitAPI.postDataCall("JSON");

        //请求地址
        Log.i(TAG, "postJsonData: url = " + call.request().url());

        //请求参数
        StringBuilder sb = new StringBuilder();
        if (call.request().body() instanceof FormBody) {
            FormBody body = (FormBody) call.request().body();
            for (int i = 0; i < body.size(); i++) {
                sb.append(body.encodedName(i))
                        .append(" = ")
                        .append(body.encodedValue(i))
                        .append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            Log.i(TAG, "RequestParams:{" + sb.toString() + "}");
        }

        //步骤7:发送网络请求(异步)
        call.enqueue(new Callback<Object>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //步骤8：请求处理,输出结果
                Object body = response.body();
                if (body == null) {
                    return;
                }
                mTextView.setText("返回的数据：" + "\n" + response.body().toString());
                Toast.makeText(RetrofitActivity.this, "post回调成功:异步执行", Toast.LENGTH_SHORT).show();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "post回调失败：" + t.getMessage());
                Toast.makeText(RetrofitActivity.this, "post回调失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 多个文件上传
     */
    private void partMapDataCall(Retrofit retrofit) {
        File file1 = new File("文件路径");
        File file2 = new File("文件路径");
        if (!file1.exists()) {
            file1.mkdir();
        }
        if (!file2.exists()) {
            file2.mkdir();
        }

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/png"), file1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/png"), file2);
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);

        Map<String, MultipartBody.Part> mapPart = new HashMap<>();
        mapPart.put("file1", filePart1);
        mapPart.put("file2", filePart2);

        Call<ResponseBody> partMapDataCall = retrofit.create(RetrofitAPI.class).getPartMapData(mapPart);
    }

    /**
     * 图文上传
     */
    private void partDataCall(Retrofit retrofit) {
        //声明类型,这里是文字类型
        MediaType textType = MediaType.parse("text/plain");
        //根据声明的类型创建RequestBody,就是转化为RequestBody对象
        RequestBody name = RequestBody.create(textType, "这里是你需要写入的文本：刘亦菲");

        //创建文件，这里演示图片上传
        File file = new File("文件路径");
        if (!file.exists()) {
            file.mkdir();
        }

        //将文件转化为RequestBody对象
        //需要在表单中进行文件上传时，就需要使用该格式：multipart/form-data
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/png"), file);
        //将文件转化为MultipartBody.Part
        //第一个参数：上传文件的key；第二个参数：文件名；第三个参数：RequestBody对象
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), imgBody);

        Call<ResponseBody> partDataCall = retrofit.create(RetrofitAPI.class).getPartData(name, filePart);
    }

    /**
     * get请求
     */
    private void getData3Call(Retrofit retrofit) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 10006);
        map.put("name", "刘亦菲");
        Call<ResponseBody> getData3call = retrofit.create(RetrofitAPI.class).getData3(map);
    }

    /**
     * post请求
     */
    private void getPostData3(Retrofit retrofit) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "刘亦菲");
        map.put("sex", "女");
        Call<ResponseBody> postData3 = retrofit.create(RetrofitAPI.class).getPostData3(map);
    }
}
