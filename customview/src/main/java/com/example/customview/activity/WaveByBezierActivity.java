package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.customview.R;
import com.example.customview.widget.WaveViewByBezier;

public class WaveByBezierActivity extends AppCompatActivity {

    private WaveViewByBezier waveViewByBezier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_by_bezier);

        waveViewByBezier = findViewById(R.id.wave_bezier);

        waveViewByBezier.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        waveViewByBezier.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveViewByBezier.resumeAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveViewByBezier.stopAnimation();
    }
}
