package com.example.group;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint({"LongLogTag", "SetTextI18n"})
public class ProgressBarActivity extends AppCompatActivity {
    private static final String TAG = "ProgressBarActivity";
    private TextView mTv1, mTv2;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mProgressBar = findViewById(R.id.progressbar);
        mSeekBar = findViewById(R.id.seekbar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > seekBar.getMax() - 10) {
                    seekBar.setSecondaryProgress(100);
                } else {
                    seekBar.setSecondaryProgress(seekBar.getProgress() + 10);
                }
                mTv1.setText("SeekBar: " + progress + "/" + mSeekBar.getMax() + "\nSecondaryProgress: " + mSeekBar.getSecondaryProgress());
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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressBarActivity.this, SeekBarActivity.class);
                startActivity(intent);
            }
        });
    }
}
