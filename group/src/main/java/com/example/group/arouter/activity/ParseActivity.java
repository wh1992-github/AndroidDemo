package com.example.group.arouter.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.group.R;
import com.example.group.arouter.data.ARouterConstants;
import com.example.group.arouter.data.Person;
import com.example.group.arouter.data.TestObj;

@SuppressLint("SetTextI18n")
@Route(path = ARouterConstants.COM_PARSE_ACTIVITY)
public class ParseActivity extends AppCompatActivity {
    private static final String TAG = "ParseActivity";
    @Autowired
    String name;
    @Autowired
    int age;
    @Autowired()
    Person person;
    //注意字段的名称必须与 withObject 的 key 一致
    @Autowired
    TestObj testObj;
    @Autowired
    TestObj mTestObj;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        ARouter.getInstance().inject(this);

        tv = findViewById(R.id.tv);

        Log.i(TAG, "onCreate: name = " + name + ", age = " + age + ", person = " + person + ", testObj = " + testObj + ", mTestObj = " + mTestObj);
        tv.setText(name + "\n" + age + "\n" + person);

        Intent intent = getIntent();
        if (intent != null) {
            Log.i(TAG, "onCreate: intent = " + intent.getExtras());
        }
    }
}
