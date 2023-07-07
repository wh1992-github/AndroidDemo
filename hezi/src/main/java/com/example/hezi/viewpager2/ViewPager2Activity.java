package com.example.hezi.viewpager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hezi.R;
import com.example.hezi.viewpager2.horizontal.HorizontalActivity;
import com.example.hezi.viewpager2.radiogroup.RgActivity;
import com.example.hezi.viewpager2.vertical.VerticalActivity;
import com.example.hezi.viewpager2.withTab.TabActivity;

public class ViewPager2Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        findViewById(R.id.btn_horizontal).setOnClickListener(this);
        findViewById(R.id.btn_vertical).setOnClickListener(this);
        findViewById(R.id.btn_rg).setOnClickListener(this);
        findViewById(R.id.btn_tab).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_horizontal:
                startActivity(new Intent(this, HorizontalActivity.class));
                break;
            case R.id.btn_vertical:
                startActivity(new Intent(this, VerticalActivity.class));
                break;
            case R.id.btn_rg:
                startActivity(new Intent(this, RgActivity.class));
                break;
            case R.id.btn_tab:
                startActivity(new Intent(this, TabActivity.class));
                break;
        }

    }
}
