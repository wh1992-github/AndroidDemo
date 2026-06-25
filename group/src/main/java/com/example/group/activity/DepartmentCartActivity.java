package com.example.group.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.group.databinding.ActivityDepartmentCartBinding;

/**
 * Created by test on 2017/10/21.
 */
public class DepartmentCartActivity extends AppCompatActivity {
    private ActivityDepartmentCartBinding binding;
    private static final String TAG = "DepartmentCartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityDepartmentCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //从布局文件中获取名叫tl_head的工具栏
        Toolbar tl_head = binding.tlHead;
        //使用tl_head替换系统自带的ActionBar
        setSupportActionBar(tl_head);
        //给tl_head设置导航图标的点击监听器
        //setNavigationOnClickListener必须放到setSupportActionBar之后,不然不起作用
        tl_head.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
