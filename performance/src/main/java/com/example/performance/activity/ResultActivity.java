package com.example.performance.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.performance.R;
/**
 * 用于展示 Result 功能的 Activity。
 */

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.i(TAG, "onCreate: ");
    }
}
