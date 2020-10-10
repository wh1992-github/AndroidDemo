package com.example.helloworld;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_";
    private static final String BASE_URL = "http://192.168.20.189:8080/ftp/test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取名叫tv_hello的TextView控件
        TextView tv_hello = findViewById(R.id.tv_hello);
        //设置TextView控件的文字内容
        tv_hello.setText("今天天气真热啊");
        //设置TextView控件的文字颜色
        tv_hello.setTextColor(Color.RED);
        //设置TextView控件的文字大小
        tv_hello.setTextSize(20);

        findViewById(R.id.btn1).setOnClickListener(this::requestData);
        findViewById(R.id.btn2).setOnClickListener(this::requestData);
    }

    private void requestData(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                http();
                break;
            case R.id.btn2:
                okhttp();
                break;
        }
    }

    private void http() {
        Log.i(TAG, "run: code : start");
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("key", "http_key");
                InputStream inputStream = connection.getInputStream();
                int totalLength = connection.getContentLength();
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
                Log.i(TAG, "run: code = " + connection.getResponseCode());
            } catch (Exception e) {
                Log.i(TAG, "run: e = " + e.getMessage());
            }
        }).start();
    }

    private void okhttp() {
        Log.i(TAG, "run: code : start");
        OkHttpUtil.getInstance().requestImage(BASE_URL);
    }
}

