package com.example.junior;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by test on 2017/9/15.
 */
public class ShapeActivity extends AppCompatActivity implements View.OnClickListener {
    private View v_content; //声明一个视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);
        //从布局文件中获取名叫v_content的视图
        v_content = findViewById(R.id.v_content);
        findViewById(R.id.btn_rect).setOnClickListener(this);
        findViewById(R.id.btn_oval).setOnClickListener(this);
        findViewById(R.id.btn_ring).setOnClickListener(this);
        findViewById(R.id.btn_dash).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_rect) { //点击了按钮btn_rect
            //把矩形形状设置为v_content的背景
            v_content.setBackgroundResource(R.drawable.shape_rect);
        } else if (v.getId() == R.id.btn_oval) { //点击了按钮btn_oval
            //把椭圆形状设置为v_content的背景
            v_content.setBackgroundResource(R.drawable.shape_oval);
        } else if (v.getId() == R.id.btn_ring) { //点击了按钮btn_oval
            //把椭圆形状设置为v_content的背景
            v_content.setBackgroundResource(R.drawable.shape_ring);
        } else if (v.getId() == R.id.btn_dash) { //点击了按钮btn_oval
            //把椭圆形状设置为v_content的背景
            v_content.setBackgroundResource(R.drawable.shape_dash);
        }
    }

}
