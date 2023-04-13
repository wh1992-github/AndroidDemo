package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.customview.R;
import com.example.customview.loadingview.mac.LVComputer;
import com.example.customview.loadingview.mac.LVComputerDesktop;
import com.example.customview.loadingview.mac.LVComputerIpad;

public class LoadingViewMacActivity extends AppCompatActivity {

    private LVComputerDesktop mLVComputerDesktop;
    private LVComputer mLVComputer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac);
        mLVComputerDesktop = findViewById(R.id.lv_computer_desktop);
        mLVComputer = findViewById(R.id.lv_computer);
    }

    public void startAnim(View v) {
        if (v.getId() == R.id.lv_computer_ipad) {
            ((LVComputerIpad) v).startAnim(4000);
            mLVComputerDesktop.startAnim(4000);
            mLVComputer.startAnim(4000);
        } else if (v.getId() == R.id.lv_computer_desktop) {
            ((LVComputerDesktop) v).startAnim(4000);
        } else if (v.getId() == R.id.lv_computer) {
            ((LVComputer) v).startAnim(6000);
        }
    }
}
