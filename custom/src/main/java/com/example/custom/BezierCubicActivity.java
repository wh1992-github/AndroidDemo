package com.example.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.example.custom.widget.BezierCubicView;

public class BezierCubicActivity extends AppCompatActivity {
    private BezierCubicView cubicView;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_cubic);
        cubicView = findViewById(R.id.cubicview);
        radioGroup = findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == group.getChildAt(0).getId()) {
                    cubicView.moveLeft();
                } else if (checkedId == group.getChildAt(1).getId()) {
                    cubicView.moveRight();
                }
            }
        });
    }
}
