package com.example.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * Created by test on 2017/10/21.
 */
public class DepartmentCartActivity extends AppCompatActivity {
    private static final String TAG = "DepartmentCartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_department_cart);
        //从布局文件中获取名叫tl_head的工具栏
        Toolbar tl_head = findViewById(R.id.tl_head);
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
