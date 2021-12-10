package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.customview.R;
import com.example.customview.widget.FadeInTextView;
import com.example.customview.widget.LoadingButton;

public class AnimationViewActivity extends AppCompatActivity {

    private FadeInTextView fadeInTextView;
    private LoadingButton loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_view);

        fadeInTextView = findViewById(R.id.fade_in_tv);
        loadingButton = findViewById(R.id.loading_btn);

        fadeInTextView
                .setTextString("自定义view实现字符串逐字显示，后边的文字是为了测试换行是否正常显示！")
                .setTextAnimationListener(new FadeInTextView.TextAnimationListener() {
                    @Override
                    public void animationFinish() {
                        loadingButton.stopLoading();
                    }
                });

        loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingButton.startLoading();
                fadeInTextView.startFadeInAnimation();
            }
        });
    }
}
