package com.example.group.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
/**
 * 封装 View Drag Activity 2 相关逻辑的类。
 */

public class ViewDragActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //可拖拽View
        setContentView(R.layout.activity_view_drag2);
    }
}
