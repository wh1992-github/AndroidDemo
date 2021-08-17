package com.example.group;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.group.widget.NumberProgressBar;
import com.example.group.widget.NumberSeekBar;

public class SeekBarActivity extends AppCompatActivity {
    private NumberSeekBar mSeekBar;
    private NumberProgressBar mProgressBar;

    private final Handler mHandler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mSeekBar.getProgress() >= 100) {
                mSeekBar.setProgress(0);
            }
            mSeekBar.setProgress(mSeekBar.getProgress() + 1);

            if (mProgressBar.getProgress() >= 100) {
                mProgressBar.setProgress(0);
            }
            mProgressBar.setProgress(mProgressBar.getProgress() + 1);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);
        mSeekBar = findViewById(R.id.numberSeekBar);
        mProgressBar = findViewById(R.id.numberProgressBar);
        mHandler.postDelayed(runnable, 100);
    }
}
