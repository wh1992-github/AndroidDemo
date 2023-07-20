package com.example.custom.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.custom.R;

import java.util.Timer;
import java.util.TimerTask;

public class ScanningView1 extends FrameLayout {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public ScanningView1(Context context) {
        this(context, null);
    }

    public ScanningView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanningView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startAnim() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(() -> addMoveCircle());
            }
        }, 0, 1000);
    }

    private void addMoveCircle() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.circle_bg);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 6f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 6f);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(imageView, "alpha", 0.8f, 0.1f);
        scaleXAnim.setDuration(6000);
        scaleYAnim.setDuration(6000);
        alphaAnim.setDuration(6000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnim, scaleYAnim, alphaAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                addView(imageView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
}