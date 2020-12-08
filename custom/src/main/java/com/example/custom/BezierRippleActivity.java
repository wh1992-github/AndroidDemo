package com.example.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.custom.widget.BezierRippleView;

public class BezierRippleActivity extends AppCompatActivity {
    private BezierRippleView waveRippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_ripple);

        waveRippleView = findViewById(R.id.waverippleview);
        waveRippleView.startAnim();
    }
}
