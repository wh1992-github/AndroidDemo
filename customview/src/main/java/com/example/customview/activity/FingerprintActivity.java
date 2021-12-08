package com.example.customview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.utils.FingerprintUtil;

public class FingerprintActivity extends AppCompatActivity {
    private static final String TAG = "FingerprintActivity";
    private TextView mResultTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        mResultTv = (TextView) findViewById(R.id.result_tv);

        findViewById(R.id.open_finger_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFinger();
            }
        });

        findViewById(R.id.cancel_finger_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerprintUtil.cancel();
            }
        });
    }

    private void openFinger() {
        FingerprintUtil.callFingerPrint(this, new FingerprintUtil.OnCallBackListener() {
            @Override
            public void onSupportFailed() {
                Toast.makeText(FingerprintActivity.this, "当前设备不支持指纹", Toast.LENGTH_LONG).show();
                Log.i(TAG, "当前设备不支持指纹");
            }

            @Override
            public void onInsecurity() {
                Toast.makeText(FingerprintActivity.this, "当前设备未处于安全保护中", Toast.LENGTH_LONG).show();
                Log.i(TAG, "当前设备未处于安全保护中");
            }

            @Override
            public void onEnrollFailed() {
                Toast.makeText(FingerprintActivity.this, "请到设置中设置指纹", Toast.LENGTH_LONG).show();
                Log.i(TAG, "请到设置中设置指纹");
            }

            @Override
            public void onAuthenticationStart() {
                Toast.makeText(FingerprintActivity.this, "验证开始", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onAuthenticationStart: ");
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                Toast.makeText(FingerprintActivity.this, errString, Toast.LENGTH_LONG).show();
                Log.i(TAG, "onAuthenticationError: ");
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(FingerprintActivity.this, "解锁失败", Toast.LENGTH_LONG).show();
                Log.i(TAG, "解锁失败");
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                Toast.makeText(FingerprintActivity.this, helpString, Toast.LENGTH_LONG).show();
                Log.i(TAG, "onAuthenticationHelp: ");
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                Toast.makeText(FingerprintActivity.this, "解锁成功", Toast.LENGTH_LONG).show();
                Log.i(TAG, "解锁成功");
                mResultTv.setText(result.getClass().getName());
            }
        });
    }
}
