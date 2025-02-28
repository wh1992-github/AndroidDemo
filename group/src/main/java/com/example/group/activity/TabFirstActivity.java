package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.group.R;

import java.util.Locale;

/**
 * Created by test on 2017/10/21.
 */
public class TabFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_first);
        //根据标签栏传来的参数拼接文本字符串
        String desc = String.format(Locale.getDefault(), "我是%s页面,来自%s",
                "首页", getIntent().getExtras().getString("tag"));
        TextView tv_first = findViewById(R.id.tv_first);
        Log.d("TabFirstActivity", "getId=" + tv_first.getId());
        tv_first.setText(desc);
    }

}
