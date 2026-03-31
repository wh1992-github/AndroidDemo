package com.example.group.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.example.group.R;
import com.example.group.livedata.LifecycleHelper;
/**
 * 用于展示 Live Data 功能的 Activity。
 */

public class LiveDataActivity extends AppCompatActivity {
    private static final String TAG = "LiveDataActivity";
    private Button mBtnLifecycle;
    private Button mBtnLivedata;
    private Button mBtnLivedataTransformations;

    private LifecycleHelper mLifecycleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        mBtnLifecycle = findViewById(R.id.btn_lifecycle);
        mBtnLifecycle.setOnClickListener(v -> startActivity(new Intent(this, LifecycleActivity.class)));

        mBtnLivedata = findViewById(R.id.btn_livedata);
        mBtnLivedata.setOnClickListener(v -> startActivity(new Intent(this, LiveDataSampleActivity.class)));

        mBtnLivedataTransformations = findViewById(R.id.btn_livedata_transformations);
        // LiveDataTransformationsActivity temporarily disabled due to Kotlin Function1 compatibility issue
        // mBtnLivedataTransformations.setOnClickListener(v -> startActivity(new Intent(this, LiveDataTransformationsActivity.class)));
    }
}
