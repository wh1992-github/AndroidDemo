package com.example.group;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CoordinatorActivity extends AppCompatActivity implements View.OnClickListener {
    private CoordinatorLayout cl_main; //声明一个协调布局对象
    private FloatingActionButton mBtnFab;//声明一个悬浮按钮对象
    private Button mBtnSnackBar;
    private Button mBtnFloating;
    private boolean floating_show = true; //是否显示悬浮按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        //从布局文件中获取名叫cl_main的协调布局
        cl_main = findViewById(R.id.cl_main);
        //从布局文件中获取名叫fab_btn的悬浮按钮
        mBtnSnackBar = findViewById(R.id.btn_snackbar);
        mBtnSnackBar.setOnClickListener(this);
        mBtnFloating = findViewById(R.id.btn_floating);
        mBtnFloating.setOnClickListener(this);
        mBtnFab = findViewById(R.id.fab_btn);
        mBtnFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_snackbar) {
            //在屏幕底部弹出一行提示条,注意悬浮按钮也会跟着上浮
            Snackbar.make(cl_main, "这是个提示条", Snackbar.LENGTH_LONG).show();
        } else if (v.getId() == R.id.btn_floating) {
            if (floating_show) { //已显示悬浮按钮
                mBtnFab.hide(); //隐藏悬浮按钮
                mBtnFloating.setText("显示悬浮按钮");
            } else { //未显示悬浮按钮
                mBtnFab.show(); //显示悬浮按钮
                mBtnFloating.setText("隐藏悬浮按钮");
            }
            floating_show = !floating_show;
        } else if (v.getId() == R.id.fab_btn) {
            Toast.makeText(this, "点击了FloatingActionButton", Toast.LENGTH_SHORT).show();
        }
    }
}
