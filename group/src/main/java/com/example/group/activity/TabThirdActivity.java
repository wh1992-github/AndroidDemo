package com.example.group.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.group.R;

import java.util.Locale;

/**
 * Created by test on 2017/10/21.
 */
public class TabThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_third);
        //根据标签栏传来的参数拼接文本字符串
        Bundle extras = getIntent().getExtras();
        String tag = extras != null ? extras.getString("tag") : "未知";
        String desc = String.format(Locale.getDefault(), "我是%s页面,来自%s",
                "购物车", tag);
        TextView tv_third = findViewById(R.id.tv_third);
        tv_third.setText(desc);
    }

}
