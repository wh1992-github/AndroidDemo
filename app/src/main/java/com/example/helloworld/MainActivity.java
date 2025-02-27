package com.example.helloworld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.helloworld.databinding.ActivityMainBinding;

@SuppressLint({"NonConstantResourceId", "SdCardPath"})
@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private ActivityMainBinding mViewBinding;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mViewBinding.getRoot());
        //获取名叫tv_hello的TextView控件
        TextView tv_hello = findViewById(R.id.tv_hello);
        //设置TextView控件的文字内容
        tv_hello.setText("今天天气真热啊");
        //设置TextView控件的文字颜色
        tv_hello.setTextColor(Color.RED);
        //设置TextView控件的文字大小
        tv_hello.setTextSize(20);
        mViewBinding.btn1.setOnClickListener(this::onClick);
        mViewBinding.btn2.setOnClickListener(this::onClick);
        mViewBinding.btn3.setOnClickListener(this::onClick);

        requestPermissions(PERMISSIONS, 10002);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            //如果没有权限，获取权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: READ_EXTERNAL_STORAGE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE));
        Log.i(TAG, "onRequestPermissionsResult: WRITE_EXTERNAL_STORAGE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        Log.i(TAG, "onRequestPermissionsResult: CALL_PHONE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE));
        Log.i(TAG, "onRequestPermissionsResult: SEND_SMS = " + ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS));
        for (int i = 0; i < permissions.length; i++) {
            Log.i(TAG, "onRequestPermissionsResult: permission = " + permissions[i] + ", " + grantResults[i]);
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                break;
            default:
                break;
        }
    }
}