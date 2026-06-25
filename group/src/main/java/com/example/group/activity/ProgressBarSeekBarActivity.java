package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.ActivityProgressbarSeekbarBinding;
import com.example.group.widget.NumberProgressBar;
import com.example.group.widget.NumberSeekBar;
/**
 * 用于展示 Progress Bar Seek Bar 功能的 Activity。
 */

@SuppressLint({"LongLogTag", "SetTextI18n"})
public class ProgressBarSeekBarActivity extends AppCompatActivity {
    private ActivityProgressbarSeekbarBinding binding;
    private static final String TAG = "ProgressBarActivity";
    private TextView mTv1, mTv2;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;
    private NumberSeekBar mNumberSeekBar;
    private NumberProgressBar mNumberProgressBar;

    private final Handler mHandler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mNumberSeekBar.getProgress() >= 100) {
                mNumberSeekBar.setProgress(0);
            }
            mNumberSeekBar.setProgress(mNumberSeekBar.getProgress() + 1);

            if (mNumberProgressBar.getProgress() >= 100) {
                mNumberProgressBar.setProgress(0);
            }
            mNumberProgressBar.setProgress(mNumberProgressBar.getProgress() + 1);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressbarSeekbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mTv1 = binding.tv1;
        mTv2 = binding.tv2;
        mProgressBar = binding.progressbar;
        mSeekBar = binding.seekbar;
        mNumberSeekBar = binding.numberSeekBar;
        mNumberProgressBar = binding.numberProgressBar;
        mHandler.postDelayed(runnable, 100);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > seekBar.getMax() - 10) {
                    seekBar.setSecondaryProgress(100);
                } else {
                    seekBar.setSecondaryProgress(seekBar.getProgress() + 10);
                }
                mTv1.setText("SeekBar: " + progress + "/" + mSeekBar.getMax() + "\t\tSecondaryProgress: " + mSeekBar.getSecondaryProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStopTrackingTouch: ");
                mProgressBar.setProgress(mSeekBar.getProgress());
                mProgressBar.setSecondaryProgress(mSeekBar.getProgress() + 10);
                mTv2.setText("ProgressBar: " + mSeekBar.getProgress() + "/" + mProgressBar.getMax());
            }
        });
    }
}
