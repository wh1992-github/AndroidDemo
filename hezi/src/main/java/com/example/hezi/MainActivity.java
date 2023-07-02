package com.example.hezi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_lottie).setOnClickListener(this::onClick);
        findViewById(R.id.btn_picker).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_lottie) {
            Intent intent = new Intent(this, LottieActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_picker) {
            Intent intent = new Intent(this, AddressPickerActivity.class);
            startActivity(intent);
        }
    }
}