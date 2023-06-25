package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.group.R;

import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class TimberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timber);
        findViewById(R.id.btn1).setOnClickListener(this::onClick);
        findViewById(R.id.btn2).setOnClickListener(this::onClick);
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
