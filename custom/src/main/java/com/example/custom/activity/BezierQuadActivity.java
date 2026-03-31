package com.example.custom.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;

//二阶贝塞尔曲线
/**
 * 用于展示 Bezier Quad 功能的 Activity。
 */
public class BezierQuadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_quad);
    }
}
