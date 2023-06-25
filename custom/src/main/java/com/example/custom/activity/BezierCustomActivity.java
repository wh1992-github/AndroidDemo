package com.example.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.custom.R;
import com.example.custom.widget.BezierCustomView1;
import com.example.custom.widget.BezierCustomView2;

//高阶贝塞尔曲线
public class BezierCustomActivity extends AppCompatActivity {

    private Button mButton;
    private BezierCustomView2 mBezierCustomView2;
    private BezierCustomView1 mBezierCustomView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_custom);
        mButton = findViewById(R.id.reset_btn);
        mBezierCustomView1 = findViewById(R.id.customview1);
        mBezierCustomView2 = findViewById(R.id.customview2);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBezierCustomView1.invalidate();
                mBezierCustomView2.invalidate();
            }
        });
    }
}
