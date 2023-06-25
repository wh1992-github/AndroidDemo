package com.example.group.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.group.R;
import com.example.group.livedata.LifecycleHelper;
import com.example.group.livedata.MyLifecycleService;

public class LifecycleActivity extends AppCompatActivity {
    private static final String TAG = "LifecycleActivity";
    private Button mBtnOpen;
    private Button mBtnClose;

    private LifecycleHelper mLifecycleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);

        mBtnOpen = findViewById(R.id.btn_open);
        mBtnClose = findViewById(R.id.btn_close);

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