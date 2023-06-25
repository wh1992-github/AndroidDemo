package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.group.R;
import com.example.group.loop.BannerPagerAdapter;
import com.example.group.loop.CircleIndicator;
import com.example.group.loop.LoopViewPager;

/**
 * Created by test on 2017/10/21.
 */

public class BannerPagerActivity extends AppCompatActivity {
    private static final String TAG = "BannerPagerActivity";
    private LoopViewPager mLoopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_pager);
        mLoopViewPager = findViewById(R.id.viewpager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        mLoopViewPager.setAdapter(new BannerPagerAdapter(BannerPagerActivity.this));
        mLoopViewPager.setOnDispatchTouchEventListener(mDispatchOnTouchListener);
        mLoopViewPager.setLooperPic(true);
        indicator.setViewPager(mLoopViewPager);
    }

    private LoopViewPager.OnDispatchTouchEventListener mDispatchOnTouchListener = new LoopViewPager.OnDispatchTouchEventListener() {
        @Override
        public void onDispatchKeyEvent(MotionEvent event) {
            Log.i(TAG, "mDispatchOnTouchListener.onDispatchKeyEvent(" + event + ")");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLoopViewPager.setLooperPic(false);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mLoopViewPager.setLooperPic(true);
            }
        }
    };
}