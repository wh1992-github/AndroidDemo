package com.example.customview.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.customview.R;

public class RevealAnimationActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button startBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_animation);

        imageView = findViewById(R.id.img_iv);
        startBtn = findViewById(R.id.start_btn);

        final int centerX = 0;
        final int centerY = 0;
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float radius = (float) Math.hypot(imageView.getWidth(), imageView.getHeight());
                if (imageView.getVisibility() == View.VISIBLE) {
                    Animator animator = ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, radius, 0);
                    animator.setDuration(3000);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            imageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                } else {
                    Animator animator = ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, 0, radius);
                    animator.setDuration(3000);
                    imageView.setVisibility(View.VISIBLE);
                    animator.start();
                }
            }
        });
    }
}
