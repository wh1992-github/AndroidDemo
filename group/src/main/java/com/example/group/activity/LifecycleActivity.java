package com.example.group.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.ActivityLifeCycleBinding;
import com.example.group.livedata.LifecycleHelper;
import com.example.group.livedata.MyLifecycleService;
/**
 * 用于展示 Lifecycle 功能的 Activity。
 */

public class LifecycleActivity extends AppCompatActivity {
    private ActivityLifeCycleBinding binding;
    private static final String TAG = "LifecycleActivity";
    private Button mBtnOpen;
    private Button mBtnClose;

    private LifecycleHelper mLifecycleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLifeCycleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnOpen = binding.btnOpen;
        mBtnClose = binding.btnClose;

        Intent intent = new Intent(LifecycleActivity.this, MyLifecycleService.class);
        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        mLifecycleHelper = new LifecycleHelper();
        getLifecycle().addObserver(mLifecycleHelper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mLifecycleHelper);
    }

}
