package com.example.group.arouter.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.group.R;
import com.example.group.arouter.data.ARouterConstants;
/**
 * 封装 Activity One 相关逻辑的类。
 */

@Route(path = ARouterConstants.COM_ACTIVITY_ONE)
public class ActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }
}
