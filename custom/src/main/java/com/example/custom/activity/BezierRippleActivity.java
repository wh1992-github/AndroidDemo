package com.example.custom.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;
import com.example.custom.widget.BezierRippleView;

//水波纹贝塞尔曲线
/**
 * 用于展示 Bezier Ripple 功能的 Activity。
 */
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
