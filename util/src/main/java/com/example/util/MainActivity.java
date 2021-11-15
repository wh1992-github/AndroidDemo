package com.example.util;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private static final String BASE_URL = "http://b-ssl.duitang.com/uploads/item/201809/26/20180926162125_vjbwi.jpg";
    private ImageView mImageView;
    private ImageView mBitmap;
    private Button mButton;
    private CountDownTimerUtil mCountDownTimerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenUtils.printScreenParams(this);
        mImageView = findViewById(R.id.iv);
        mBitmap = findViewById(R.id.bitmap);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this::onClick);

        GlideUtil.loadImage(this, BASE_URL, mImageView);
        mBitmap.setImageBitmap(StringBitmap.drawString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {
        DefaultType defaultType = new DefaultType();
        defaultType.setSexName(DefaultType.Person.female);
        defaultType.setSexType(DefaultType.Sex.MAN);

        mCountDownTimerUtil = new CountDownTimerUtil(10 * 1000, 1000);
        mCountDownTimerUtil.start();

        ReflectUtil.reflect();
        AtomicWork.atomicWork();
        JavaTest.switchLight();
        JavaTest.fbnq();
        JavaTest.test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimerUtil != null) {
            mCountDownTimerUtil.cancel();
            mCountDownTimerUtil = null;
        }
    }
}
