package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.group.R;
import com.example.group.adapter.RecyclerCollapseAdapter;

public class AppbarRecyclerActivity extends AppCompatActivity {
    private static final String[] yearArray = {"鼠年", "牛年", "虎年", "兔年", "龙年", "蛇年",
            "马年", "羊年", "猴年", "鸡年", "狗年", "猪年"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appbar_recycler);
        //从布局文件中获取名叫tl_title的工具栏
        Toolbar tl_title = findViewById(R.id.tl_title);
        //使用tl_title替换系统自带的ActionBar
        setSupportActionBar(tl_title);
        //从布局文件中获取名叫rv_main的循环视图
        RecyclerView rv_main = findViewById(R.id.rv_main);
        //创建一个垂直方向的线性布局管理器
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        //设置循环视图的布局管理器
        rv_main.setLayoutManager(llm);
        //构建一个十二生肖的线性适配器
        RecyclerCollapseAdapter adapter = new RecyclerCollapseAdapter(this, yearArray);
        //给rv_main设置十二生肖线性适配器
        rv_main.setAdapter(adapter);
    }

}

