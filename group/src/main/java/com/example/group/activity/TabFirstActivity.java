package com.example.group.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.databinding.FragmentTabFirstBinding;

import java.util.Locale;

/**
 * Created by test on 2017/10/21.
 */
public class TabFirstActivity extends AppCompatActivity {
    private FragmentTabFirstBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTabFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //根据标签栏传来的参数拼接文本字符串
        Bundle extras = getIntent().getExtras();
        String tag = extras != null ? extras.getString("tag") : "未知";
        String desc = String.format(Locale.getDefault(), "我是%s页面,来自%s",
                "首页", tag);
        TextView tv_first = binding.tvFirst;
        Log.d("TabFirstActivity", "getId=" + tv_first.getId());
        tv_first.setText(desc);
    }

}
