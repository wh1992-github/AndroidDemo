package com.example.animation.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.animation.R;

/**
 * Created by test on 2017/11/27.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ObjectXmlActivity extends AppCompatActivity implements OnClickListener {
    private ImageView iv_object_iv; //声明一个图像视图对象
    private boolean isPaused = false;
    private AnimatorSet mAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_xml);
        //从布局文件中获取名叫iv_object_group的图像视图
        iv_object_iv = findViewById(R.id.iv_object_iv);
        iv_object_iv.setOnClickListener(this);
        initAnimator(); //初始化属性动画
    }

    //初始化属性动画
    private void initAnimator() {
        //将xml中的动画加载
        mAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_object);
        mAnimatorSet.setTarget(iv_object_iv);
        mAnimatorSet.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_object_iv) {
            if (mAnimatorSet.isStarted()) { //属性动画已经播放过了
                if (mAnimatorSet.isRunning()) { //属性动画正在播放
                    if (!isPaused) {
                        mAnimatorSet.pause(); //暂停播放属性动画
                    } else {
                        mAnimatorSet.resume(); //恢复播放属性动画
                    }
                    isPaused = !isPaused;
                } else { //属性动画不在播放
                    mAnimatorSet.start(); //开始播放属性动画
                }
            } else { //属性动画尚未播放
                mAnimatorSet.start(); //开始播放属性动画
            }
        }
    }

}
