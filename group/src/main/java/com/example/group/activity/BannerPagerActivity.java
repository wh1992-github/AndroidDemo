package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.ActivityBannerPagerBinding;
import com.example.group.loop.BannerPagerAdapter;
import com.example.group.loop.CircleIndicator;
import com.example.group.loop.LoopViewPager;

/**
 * Created by test on 2017/10/21.
 */
@SuppressLint("LogNotTimber")
public class BannerPagerActivity extends AppCompatActivity {
    private ActivityBannerPagerBinding binding;
    private static final String TAG = "BannerPagerActivity";
    private LoopViewPager mLoopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBannerPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLoopViewPager = binding.viewpager;
        CircleIndicator indicator = binding.indicator;
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
