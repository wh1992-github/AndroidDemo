package com.example.group.arouter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.group.R;
import com.example.group.arouter.data.ARouterConstants;

@Route(path = ARouterConstants.TEST_ACTIVITY)
public class TestActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_test);
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
        mTv = findViewById(R.id.tv);
        mTvPara = findViewById(R.id.tv_para);
    }
}
