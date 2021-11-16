package com.example.group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.group.loop.CircleIndicator;
import com.example.group.loop.LoopViewPager;
import com.example.group.loop.LoopAdapter;

/**
 * Created by test on 2017/10/21.
 */
@SuppressLint("DefaultLocale")
public class BannerPagerActivity extends AppCompatActivity {
    private static final String TAG = "BannerPagerActivity";
    private LoopViewPager mLooperViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_pager);
        mLooperViewPager = findViewById(R.id.viewpager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        mLooperViewPager.setAdapter(new LoopAdapter(BannerPagerActivity.this));
        mLooperViewPager.setOnDispatchTouchEventListener(mDispatchOnTouchListener);
        mLooperViewPager.setLooperPic(true);
        indicator.setViewPager(mLooperViewPager);
    }

    private LoopViewPager.OnDispatchTouchEventListener mDispatchOnTouchListener = new LoopViewPager.OnDispatchTouchEventListener() {
        @Override
        public void onDispatchKeyEvent(MotionEvent event) {
            Log.i(TAG, "mDispatchOnTouchListener.onDispatchKeyEvent(" + event + ")");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLooperViewPager.setLooperPic(false);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mLooperViewPager.setLooperPic(true);
            }
        }
    };

}
