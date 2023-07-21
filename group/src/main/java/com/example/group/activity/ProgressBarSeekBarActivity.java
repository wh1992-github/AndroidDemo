package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.group.R;
import com.example.group.widget.NumberProgressBar;
import com.example.group.widget.NumberSeekBar;

@SuppressLint({"LongLogTag", "SetTextI18n"})
public class ProgressBarSeekBarActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_progressbar_seekbar);
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mProgressBar = findViewById(R.id.progressbar);
        mSeekBar = findViewById(R.id.seekbar);
        mNumberSeekBar = findViewById(R.id.numberSeekBar);
        mNumberProgressBar = findViewById(R.id.numberProgressBar);
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
