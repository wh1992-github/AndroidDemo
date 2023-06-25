package com.example.group.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.group.R;
import com.example.group.util.LocalBroadcastManager;

public class LocalBroadcastActivity extends AppCompatActivity {
    private static final String TAG = "LocalBroadcastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_local_broadcast);
        findViewById(R.id.button).setOnClickListener(this::onClick);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.fuxi.test.custom");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void onClick(View view) {
        Intent intent = new Intent("com.fuxi.test.custom");
        intent.putExtra("data", "test-LocalBroadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //1.LocalBroadcast 是APP内部维护的一套广播机制，有很高的安全性和高效性。所以如果有APP内部发送、接收广播的需要应该使用 LocalBroadcast。
    //2.Receiver 只允许动态注册，不允许在 AndroidManifest 中注册。
    //3.LocalBroadcastManager 所发送的广播 action，只能与注册到 LocalBroadcastManager 中 BroadcastReceiver 产生互动。
    //4.LocalBroadcastManager 的核心实现实际还是 Handler，只是利用到了 IntentFilter 的 match 功能，至于 BroadcastReceiver 换成其他接口也无所谓，顺便利用了现成的类和概念而已。
    //自定义的类似 BroadcastReceiver 的接口
    private final LocalBroadcastManager.CustomBroadcastReceiver broadcastReceiver = (context, intent) -> {
        //本地广播
        Log.i(TAG, "onReceive: action = " + intent.getAction() + ", data = " + intent.getStringExtra("data"));
    };
}