package com.example.group.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
import com.example.group.databinding.ActivityLog4jBinding;
import com.example.group.log4j.Log4jUtil;
/**
 * 用于展示 Log 4 j 功能的 Activity。
 */

public class Log4jActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLog4jBinding binding;
    private static final String TAG = "Log4jActivity";
    private Button mBtn1;
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLog4jBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtn1 = binding.btn01;
        mBtn2 = binding.btn02;
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_01) {
            Log4jUtil.configure();
        } else if (v.getId() == R.id.btn_02) {
            Log4jUtil.i(TAG, "start record log.");
            Log4jUtil.i(TAG, "finish record log.");
        }
    }
}
