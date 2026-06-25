package com.example.group.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
/**
 * 封装 View Drag Activity 1 相关逻辑的类。
 */

public class ViewDragActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //底部菜单抽屉示例
        setContentView(R.layout.activity_view_drag1);
    }
}
