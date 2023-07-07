package com.example.hezi.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hezi.databinding.ActivityConstraintBinding;

@SuppressLint("LongLogTag")
public class ConstraintLayoutActivity extends AppCompatActivity {
    private static final String TAG = "ConstraintLayoutActivity";
    private ActivityConstraintBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityConstraintBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.startAnim.setOnClickListener(view -> startAnim());

        mBinding.btn.setOnClickListener(view -> {
            Log.i(TAG, "onCreate: 点击事件");
            Log.i(TAG, "onCreate: l = " + mBinding.btn.getLeft() + ", r = " + mBinding.btn.getRight()
                    + ", t = " + mBinding.btn.getTop() + ", b = " + mBinding.btn.getBottom());
            Log.i(TAG, "onCreate: x = " + mBinding.btn.getTranslationX() + ", y = " + mBinding.btn.getTranslationY()
                    + ", z = " + mBinding.btn.getTranslationZ());
        });
    }

    private void startAnim() {
        //构造一个在横轴上平移的属性动画
        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(mBinding.btn, "translationX", 0f, 200f);
        translateAnim.setDuration(1000);
        translateAnim.start();
    }
}
