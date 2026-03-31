package com.example.device.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.device.R;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Locale;
/**
 * 用于展示 UI 功能的 Activity。
 */

public class UIActivity extends AppCompatActivity {
    private static final String TAG = "UIActivity";
    private RelativeLayout mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ui);
        mRootView = findViewById(R.id.rl);
    }
}