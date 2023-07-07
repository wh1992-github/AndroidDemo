package com.example.hezi.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.hezi.R;
import com.example.hezi.view.SpeedTextView;

/**
 * LottieAnimationView.CacheStrategy.None 没有缓存
 * LottieAnimationView.CacheStrategy.Weak 弱引用缓存
 * LottieAnimationView.CacheStrategy.Strong 强引用缓存
 */
public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView mLottieAnimationView;
    private SpeedTextView mSpeedTextView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        mLottieAnimationView = findViewById(R.id.lottie_view);
        mSpeedTextView = findViewById(R.id.speed_view);

        mLottieAnimationView.setAnimation("data.json");
        mLottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        mLottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        mLottieAnimationView.playAnimation();

        mSpeedTextView.startAnim();
    }
}