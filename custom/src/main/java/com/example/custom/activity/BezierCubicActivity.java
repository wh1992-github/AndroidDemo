package com.example.custom.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.example.custom.R;
import com.example.custom.widget.BezierCubicView;

//三阶贝塞尔曲线
/**
 * 用于展示 Bezier Cubic 功能的 Activity。
 */
public class BezierCubicActivity extends AppCompatActivity {
    private BezierCubicView cubicView;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_cubic);
        cubicView = findViewById(R.id.cubicview);
        radioGroup = findViewById(R.id.rg);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == group.getChildAt(0).getId()) {
                cubicView.moveLeft();
            } else if (checkedId == group.getChildAt(1).getId()) {
                cubicView.moveRight();
            }
        });
    }
}
