package com.example.performance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.performance.R;

/**
 * Created by test on 2017/12/27.
 */
public class PowerSavingActivity extends AppCompatActivity {
    private TextView tv_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_saving);
        tv_screen = findViewById(R.id.tv_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tv_screen.setText(MainApplication.getInstance().getChangeDesc());
    }

}