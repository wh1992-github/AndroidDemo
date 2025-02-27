package com.example.device.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.device.databinding.ActivityAutoSizeBinding;
import com.example.device.util.AdapterUtil;

import me.jessyan.autosize.utils.AutoSizeUtils;

@SuppressLint({"NonConstantResourceId", "SdCardPath"})
@RequiresApi(api = Build.VERSION_CODES.R)
public class AutoSizeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AutoSizeActivity---";
    private ActivityAutoSizeBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdapterUtil.setActivityDensity(this,1000);
        Log.i(TAG, "onCreate: ");
        mViewBinding = ActivityAutoSizeBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        mViewBinding.btn.setOnClickListener(this);
        //获取系统原来的
        float density1 = Resources.getSystem().getDisplayMetrics().density;
        //获取AutoSize后的
        float density2 = getResources().getDisplayMetrics().density;
        float widthPixels = getResources().getDisplayMetrics().widthPixels;
        Log.i(TAG, "onCreate: density1 = " + density1 + ", density2 = " + density2 + ", widthPixels = " + widthPixels);
        Log.i(TAG, "onCreate: " + AutoSizeUtils.dp2px(this, 10));
        Log.i(TAG, "onCreate: " + AutoSizeUtils.dp2px(this, 100));
    }


    @Override
    public void onClick(View v) {
        addView();
    }

    private void addView() {
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Button mButton = new Button(this);
        mButton.setText("WindowManager Button");
        mButton.setTextSize(AutoSizeUtils.dp2px(this, 100));
        mButton.setAllCaps(false);

        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 0; //偏移量
        mLayoutParams.y = 0; //偏移量
        //mLayoutParams.alpha = 0.6f; //透明度
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        mWindowManager.addView(mButton, mLayoutParams);
    }
}