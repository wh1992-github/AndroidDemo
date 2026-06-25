package com.example.group.activity;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.group.databinding.ActivityAppbarNestedBinding;
/**
 * 用于展示 Appbar Nested 功能的 Activity。
 */

public class AppbarNestedActivity extends AppCompatActivity {
    private ActivityAppbarNestedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityAppbarNestedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //从布局文件中获取名叫tl_head的工具栏
        Toolbar tl_title = binding.tlTitle;
        //使用tl_head替换系统自带的ActionBar
        setSupportActionBar(tl_title);
    }

}
