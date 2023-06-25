package com.example.hezi;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

/**
 * LottieAnimationView.CacheStrategy.None 没有缓存
 * LottieAnimationView.CacheStrategy.Weak 弱引用缓存
 * LottieAnimationView.CacheStrategy.Strong 强引用缓存
 */
public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView mLottieAnimationView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        mLottieAnimationView = findViewById(R.id.lottie_view);
        mLottieAnimationView.setAnimation("data.json");
        mLottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        mLottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        mLottieAnimationView.playAnimation();
    }
}