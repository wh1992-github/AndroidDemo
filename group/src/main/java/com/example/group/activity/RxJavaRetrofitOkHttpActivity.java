package com.example.group.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.group.R;
import com.example.group.retrofit.OkHttpActivity;
import com.example.group.retrofit.RetrofitActivity;
import com.example.group.rxjava.RxJavaActivity01;
import com.example.group.rxjava.RxJavaActivity02;
import com.example.group.rxjava.RxJavaActivity03;
import com.example.group.rxjava.RxJavaActivity04;

@SuppressLint("NonConstantResourceId")
public class RxJavaRetrofitOkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        findViewById(R.id.btn_rxjava2_01).setOnClickListener(this);
        findViewById(R.id.btn_rxjava2_02).setOnClickListener(this);
        findViewById(R.id.btn_rxjava2_03).setOnClickListener(this);
        findViewById(R.id.btn_rxjava2_04).setOnClickListener(this);
        findViewById(R.id.btn_retrofit).setOnClickListener(this);
        findViewById(R.id.btn_okhttp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rxjava2_01://RxJava2的简单使用(一)
                startActivity(new Intent(this, RxJavaActivity01.class));
                break;
            case R.id.btn_rxjava2_02://RxJava2的简单使用(二)
                startActivity(new Intent(this, RxJavaActivity02.class));
                break;
            case R.id.btn_rxjava2_03://RxJava2的简单使用(三)
                startActivity(new Intent(this, RxJavaActivity03.class));
                break;
            case R.id.btn_rxjava2_04://RxJava2的简单使用(四)
                startActivity(new Intent(this, RxJavaActivity04.class));
                break;
            case R.id.btn_retrofit:
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case R.id.btn_okhttp:
                startActivity(new Intent(this, OkHttpActivity.class));
                break;
            default:
                break;
        }
    }
}
