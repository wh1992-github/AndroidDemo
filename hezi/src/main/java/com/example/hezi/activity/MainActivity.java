package com.example.hezi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hezi.R;
import com.example.hezi.viewpager.ViewpagerActivity;
import com.example.hezi.viewpager2.ViewPager2Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_lottie).setOnClickListener(this);
        findViewById(R.id.btn_picker).setOnClickListener(this);
        findViewById(R.id.btn_viewpager).setOnClickListener(this);
        findViewById(R.id.btn_viewpager2).setOnClickListener(this);
        findViewById(R.id.btn_constraint).setOnClickListener(this);
        findViewById(R.id.btn_step_view).setOnClickListener(this);
        findViewById(R.id.btn_video_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_lottie) {
            Intent intent = new Intent(this, LottieActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_picker) {
            Intent intent = new Intent(this, AddressPickerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_viewpager) {
            Intent intent = new Intent(this, ViewpagerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_viewpager2) {
            Intent intent = new Intent(this, ViewPager2Activity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_constraint) {
            Intent intent = new Intent(this, ConstraintLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_step_view) {
            Intent intent = new Intent(this, StepViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_video_view) {
            Intent intent = new Intent(this, VideoViewActivity.class);
            startActivity(intent);
        }
    }
}