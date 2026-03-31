package com.example.customview.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.widget.AnimationButton;
/**
 * 用于展示 Animation Btn 功能的 Activity。
 */

public class AnimationBtnActivity extends AppCompatActivity {

    private AnimationButton animationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_btn);
        animationButton = findViewById(R.id.animation_btn);
        animationButton.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {
                animationButton.start();
            }

            @Override
            public void animationFinish() {
                Toast.makeText(AnimationBtnActivity.this, "动画执行完毕", Toast.LENGTH_SHORT).show();
                animationButton.reset();
            }
        });
    }
}
