package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.customview.R;
import com.example.customview.widget.RadarWaveView;

public class RadarActivity extends AppCompatActivity {

    private RadarWaveView radarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        radarView = (RadarWaveView) findViewById(R.id.radar_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        radarView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        radarView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radarView.stop();
    }
}
