package com.example.group.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
import com.example.group.databinding.ActivityLevelListBinding;
/**
 * 用于展示 Level List 功能的 Activity。
 */

public class LevelListActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLevelListBinding binding;
    private static final String TAG = "LevelListActivity";
    private Button mBtnLevel1;
    private Button mBtnLevel2;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnLevel1 = binding.btnLevel01;
        mBtnLevel2 = binding.btnLevel02;
        mBtnLevel1.setOnClickListener(this);
        mBtnLevel2.setOnClickListener(this);
        mIv = binding.ivDrawable;
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
