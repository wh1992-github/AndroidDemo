package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.example.group.R;

public class AppbarNestedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appbar_nested);
        //从布局文件中获取名叫tl_head的工具栏
        Toolbar tl_title = findViewById(R.id.tl_title);
        //使用tl_head替换系统自带的ActionBar
        setSupportActionBar(tl_title);
    }

}