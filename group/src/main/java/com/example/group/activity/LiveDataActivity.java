package com.example.group.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.ActivityLiveDataBinding;
import com.example.group.livedata.LifecycleHelper;
/**
 * 用于展示 Live Data 功能的 Activity。
 */

public class LiveDataActivity extends AppCompatActivity {
    private ActivityLiveDataBinding binding;
    private static final String TAG = "LiveDataActivity";
    private Button mBtnLifecycle;
    private Button mBtnLivedata;
    private Button mBtnLivedataTransformations;

    private LifecycleHelper mLifecycleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnLifecycle = binding.btnLifecycle;
        mBtnLifecycle.setOnClickListener(v -> startActivity(new Intent(this, LifecycleActivity.class)));

        mBtnLivedata = binding.btnLivedata;
        mBtnLivedata.setOnClickListener(v -> startActivity(new Intent(this, LiveDataSampleActivity.class)));

        mBtnLivedataTransformations = binding.btnLivedataTransformations;
        // LiveDataTransformationsActivity temporarily disabled due to Kotlin Function1 compatibility issue
        // mBtnLivedataTransformations.setOnClickListener(v -> startActivity(new Intent(this, LiveDataTransformationsActivity.class)));
    }
}
