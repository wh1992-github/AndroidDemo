package com.example.group.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.FragmentTabSecondBinding;

import java.util.Locale;

/**
 * Created by test on 2017/10/21.
 */
public class TabSecondActivity extends AppCompatActivity {
    private FragmentTabSecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTabSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //根据标签栏传来的参数拼接文本字符串
        Bundle extras = getIntent().getExtras();
        String tag = extras != null ? extras.getString("tag") : "未知";
        String desc = String.format(Locale.getDefault(), "我是%s页面,来自%s",
                "分类", tag);
        TextView tv_second = binding.tvSecond;
        tv_second.setText(desc);
    }
}
