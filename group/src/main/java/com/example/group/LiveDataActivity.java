package com.example.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.group.livedata.LifecycleHelper;

public class LiveDataActivity extends AppCompatActivity {
    private static final String TAG = "LiveDataActivity";
    private Button mBtnLivedata;
    private Button mBtnLivedataTransformations;

    private LifecycleHelper mLifecycleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);
        initView();
        mLifecycleHelper = new LifecycleHelper(this);
        mLifecycleHelper.addObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleHelper.removeObserver();
    }

    private void initView() {
        mBtnLivedata = findViewById(R.id.btn_livedata);
        mBtnLivedata.setOnClickListener(v -> startActivity(new Intent(this, LiveDataSampleActivity.class)));

        mBtnLivedataTransformations = findViewById(R.id.btn_livedata_transformations);
        mBtnLivedataTransformations.setOnClickListener(v -> startActivity(new Intent(this, LiveDataTransformationsActivity.class)));
    }
}
