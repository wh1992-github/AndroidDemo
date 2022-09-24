package com.example.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.custom.widget.ScanningView;

public class RippleAnimActivity extends AppCompatActivity {

    private ScanningView mScanningView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_anim);
        mScanningView = findViewById(R.id.scanView);
        mScanningView.startAnim();
    }
}
