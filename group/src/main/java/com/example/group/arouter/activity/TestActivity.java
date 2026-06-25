package com.example.group.arouter.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.group.arouter.data.ARouterConstants;
import com.example.group.databinding.ActivityTestBinding;
/**
 * 用于展示 Test 功能的 Activity。
 */

@Route(path = ARouterConstants.TEST_ACTIVITY)
public class TestActivity extends AppCompatActivity {
    private ActivityTestBinding binding;
    private TextView mTv;
    private TextView mTvPara;
    @Autowired
    String name;
    @Autowired
    int age;
    @Autowired
    boolean boy;
    @Autowired
    int high;
    @Autowired
    String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ARouter.getInstance().inject(this);
        initView();
        initData();
    }

    private void initData() {
        mTv.setText(this.getClass().getSimpleName());
        String params = "参数是： " + "name: " + name + "  age: " + age + " boy: " + boy;
        if (obj != null) {
            params = params + " obj: " + obj.toString();
        }
        mTvPara.setText(params);
    }

    private void initView() {
        mTv = binding.tv;
        mTvPara = binding.tvPara;
    }
}
