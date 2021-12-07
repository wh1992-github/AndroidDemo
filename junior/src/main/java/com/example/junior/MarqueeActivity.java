package com.example.junior;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.junior.util.MarqueeView;

/**
 * Created by test on 2017/9/14.
 */
public class MarqueeActivity extends AppCompatActivity {
    private TextView mTvMarquee;
    //跑马灯文字是否暂停滚动
    private boolean isPaused = false;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;
    private MarqueeView mMarqueeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
        initView();
        initClickListener();
    }

    private void initView() {
        mTvMarquee = findViewById(R.id.tv_marquee);
        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);
        mBtn5 = findViewById(R.id.btn5);
        mBtn6 = findViewById(R.id.btn6);
        mBtn7 = findViewById(R.id.btn7);
        mMarqueeView = findViewById(R.id.marquee_view);
    }

    private void initClickListener() {
        mTvMarquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;
                if (isPaused) {
                    mTvMarquee.setFocusable(false); //不允许获得焦点
                    mTvMarquee.setFocusableInTouchMode(false); //不允许在触摸时获得焦点
                } else {
                    mTvMarquee.setFocusable(true); //允许获得焦点
                    mTvMarquee.setFocusableInTouchMode(true); //允许在触摸时获得焦点
                    mTvMarquee.requestFocus(); //强制获得焦点,让跑马灯滚起来
                }
            }
        });
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarqueeView.setContent("风起日落,天行有常.");
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarqueeView.setTextColor(R.color.colorAccent);
            }
        });
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarqueeView.setTextSize(24);
            }
        });
        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarqueeView.setTextSpeed(3);
            }
        });
        mBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarqueeView.continueRoll();
            }
        });
        mBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarqueeView.stopRoll();
            }
        });
        mBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarqueeView.setTextDistance(100);
            }
        });
    }
}
