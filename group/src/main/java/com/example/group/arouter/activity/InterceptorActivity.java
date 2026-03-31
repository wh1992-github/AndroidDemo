package com.example.group.arouter.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.group.R;
import com.example.group.arouter.data.ARouterConstants;
/**
 * 用于展示 Interceptor 功能的 Activity。
 */

@Route(path = ARouterConstants.COM_ACTIVITY_INTERCEPTOR)
public class InterceptorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);
    }
}
