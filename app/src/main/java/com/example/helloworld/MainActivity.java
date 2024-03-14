package com.example.helloworld;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.helloworld.databinding.ActivityMainBinding;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mViewBinding.getRoot());
        //获取名叫tv_hello的TextView控件
        TextView tv_hello = findViewById(R.id.tv_hello);
        //设置TextView控件的文字内容
        tv_hello.setText("今天天气真热啊");
        //设置TextView控件的文字颜色
        tv_hello.setTextColor(Color.RED);
        //设置TextView控件的文字大小
        tv_hello.setTextSize(20);
        mViewBinding.btn1.setOnClickListener(this::onClick);
        mViewBinding.btn2.setOnClickListener(this::onClick);
        mViewBinding.btn3.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Log.i(TAG, "onClick: " + reverseWords("abcd efg hijk"));
                break;
            case R.id.btn2:
                StringBuilder stringBuilder = new StringBuilder("asdfghjkl");
                Log.i(TAG, "onClick: " + stringBuilder.append("---zxcvbnm"));

                String string = "qwertyuio";
                Log.i(TAG, "onClick: " + string.concat("---zxcvbnm"));
                break;
            case R.id.btn3:
                Log.i(TAG, "onClick: size = " + call(arrays1));
                Log.i(TAG, "onClick: size = " + call(arrays2));
                Log.i(TAG, "onClick: size = " + call(arrays3));
                break;
            default:
                break;
        }
    }

    public String reverseWords(String s) {
        String[] ss = s.trim().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = ss.length - 1; i >= 0; i--) {
            if (ss[i].equals("")) {
                continue;
            }
            sb.append(ss[i] + " ");
        }
        return sb.toString().trim();
    }

    private static final int[] arrays1 = {2, 5, 1, 2, 3, 4, 7, 7, 6};
    private static final int[] arrays2 = {2, 5, 1, 3, 1, 2, 1, 7, 7, 6};
    private static final int[] arrays3 = {3, 2, 3, 4, 5, 6};

    //数组注水问题
    public int call(int[] array) {
        int r = 0;
        int max_index = -1;
        int max = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                max_index = i;
            }
        }

        max = array[0];
        for (int i = 0; i < max_index; i++) {
            if (array[i] < max) {
                r += (max - array[i]);
            } else {
                max = array[i];
            }
        }

        max = array[array.length - 1];
        for (int i = array.length - 1; i > max_index; i--) {
            if (array[i] < max) {
                r += (max - array[i]);
            } else {
                max = array[i];
            }
        }

        return r;
    }
}

