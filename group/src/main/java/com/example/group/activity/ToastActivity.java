package com.example.group.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customtoast.TastyToast;
import com.example.group.R;
/**
 * 用于展示 Toast 功能的 Activity。
 */

public class ToastActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
    }

    public void showSuccessToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Download Successful !", TastyToast.SUCCESS).show();
    }

    public void showWarningToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Are you sure ?", TastyToast.WARNING).show();
    }

    public void showErrorToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Downloading failed ! Try again later ", TastyToast.ERROR).show();
    }

    public void showInfoToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Searching for username : uName ", TastyToast.INFO).show();
    }

    public void showDefaultToast(View view) {
        TastyToast.makeText(getApplicationContext(), "This is Default Toast ", TastyToast.DEFAULT).show();
    }


    public void showConfusingToast(View view) {
        TastyToast.makeText(getApplicationContext(), "I don't Know !", TastyToast.CONFUSING).show();
    }
}
