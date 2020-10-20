package com.example.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private static final String BASE_URL = "http://b-ssl.duitang.com/uploads/item/201809/26/20180926162125_vjbwi.jpg";
    private ImageView mImageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenUtils.printScreenParams(this);
        mImageView = findViewById(R.id.iv);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this::onClick);

        GlideUtil.loadImage(this, BASE_URL, mImageView);
    }

    public void onClick(View view) {

    }
}
