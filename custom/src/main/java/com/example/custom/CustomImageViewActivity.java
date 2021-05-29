package com.example.custom;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.custom.util.BitmapUtils;

public class CustomImageViewActivity extends AppCompatActivity {

    private ImageView mCircleView;
    private ImageView mRoundRectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_imageview);
        mCircleView = findViewById(R.id.circle_iv);
        mRoundRectView = findViewById(R.id.round_rect_iv);
        mCircleView.setImageBitmap(BitmapUtils.createCircleView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_default)));
        mRoundRectView.setImageBitmap(BitmapUtils.createRoundRectView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_default)));
    }
}