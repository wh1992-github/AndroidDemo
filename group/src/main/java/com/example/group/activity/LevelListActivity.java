package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.group.R;

public class LevelListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LevelListActivity";
    private Button mBtnLevel1;
    private Button mBtnLevel2;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        mBtnLevel1 = findViewById(R.id.btn_level_01);
        mBtnLevel2 = findViewById(R.id.btn_level_02);
        mBtnLevel1.setOnClickListener(this);
        mBtnLevel2.setOnClickListener(this);
        mIv = findViewById(R.id.iv_drawable);
        mIv.setImageLevel(8);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_level_01) {
            //设置的level值必须在12-20之间
            mIv.setImageLevel(18);
        } else if (v.getId() == R.id.btn_level_02) {
            //设置的level值必须在6-10之间
            mIv.setImageLevel(8);
        }
    }
}
