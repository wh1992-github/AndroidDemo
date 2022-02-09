package com.example.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.group.log4j.Log4jUtil;

public class Log4jActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Log4jActivity";
    private Button mBtn1;
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log4j);

        mBtn1 = findViewById(R.id.btn_01);
        mBtn2 = findViewById(R.id.btn_02);
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
