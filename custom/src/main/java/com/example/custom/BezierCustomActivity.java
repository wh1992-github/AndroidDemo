package com.example.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.custom.widget.BezierCubicView;
import com.example.custom.widget.BezierCustomView;

//高阶贝塞尔曲线
public class BezierCustomActivity extends AppCompatActivity {

    private Button mButton;
    private BezierCustomView mBezierCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_custom);
        mButton=findViewById(R.id.reset_btn);
        mBezierCustomView = findViewById(R.id.customview);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBezierCustomView.invalidate();
            }
        });
    }
}
