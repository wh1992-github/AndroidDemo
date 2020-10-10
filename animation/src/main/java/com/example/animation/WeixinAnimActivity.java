package com.example.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

//仿微信摇一摇动画效果
public class WeixinAnimActivity extends AppCompatActivity {
    private ImageView mIvUp;
    private ImageView mIvDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_anim);

        mIvUp = findViewById(R.id.up);
        mIvDown = findViewById(R.id.down);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startAnim();
        return true;
    }

    private void startAnim() {
        AnimationSet animUp = new AnimationSet(true);
        TranslateAnimation translateUp0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, -0.5f);
        translateUp0.setDuration(600);
        TranslateAnimation translateUp1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, +0.5f);
        translateUp1.setDuration(600);
        translateUp1.setStartOffset(600);
        animUp.addAnimation(translateUp0);
        animUp.addAnimation(translateUp1);
        mIvUp.startAnimation(animUp);

        AnimationSet animDown = new AnimationSet(true);
        TranslateAnimation translateDown0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, +0.5f);
        translateDown0.setDuration(600);
        TranslateAnimation translateDown1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, -0.5f);
        translateDown1.setDuration(600);
        translateDown1.setStartOffset(600);
        animDown.addAnimation(translateDown0);
        animDown.addAnimation(translateDown1);
        mIvDown.startAnimation(animDown);
    }
}
