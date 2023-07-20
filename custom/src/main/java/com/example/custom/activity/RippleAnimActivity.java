package com.example.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.custom.R;
import com.example.custom.widget.ScanningView1;
import com.example.custom.widget.ScanningView2;

public class RippleAnimActivity extends AppCompatActivity {

    private ScanningView1 mScanningView1;
    private ScanningView2 mScanningView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_anim);
        mScanningView1 = findViewById(R.id.scanView1);
        mScanningView2 = findViewById(R.id.scanView2);
        mScanningView1.startAnim();
        mScanningView2.startAnim();
    }
}
