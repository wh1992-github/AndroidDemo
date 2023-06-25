package com.example.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.custom.R;
import com.example.custom.util.MeasureUtil;

import java.util.Locale;

/**
 * Created by test on 2017/10/14.
 */

public class MeasureLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_layout);
        LinearLayout ll_header = findViewById(R.id.ll_header);
        TextView tv_desc = findViewById(R.id.tv_desc);
        //计算获取线性布局的实际高度
        float height = MeasureUtil.getRealHeight(ll_header);
        String desc = String.format(Locale.getDefault(), "上面下拉刷新头部的高度是%f", height, Locale.getDefault());
        tv_desc.setText(desc);
    }
}
