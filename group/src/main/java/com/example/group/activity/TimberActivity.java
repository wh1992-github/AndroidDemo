package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
import com.example.group.databinding.ActivityTimberBinding;

import timber.log.Timber;
/**
 * 用于展示 Timber 功能的 Activity。
 */

@SuppressLint("NonConstantResourceId")
public class TimberActivity extends AppCompatActivity {
    private ActivityTimberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btn1.setOnClickListener(this::onClick);
        binding.btn2.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Timber.i("onClick: btn1");
                break;
            case R.id.btn2:
                Timber.i("onClick: btn2");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.i("onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy: ");
    }
}
