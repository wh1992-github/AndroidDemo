package com.example.senior.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.senior.R;
import com.example.senior.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Set;

/**
 * Created by test on 2017/10/7.
 */
public class BroadSystemActivity extends AppCompatActivity {
    private static WeakReference<TextView> tv_system_ref;
    private TextView tv_system;
    private static String desc = "开始侦听分钟广播,请稍等。注意要保持屏幕亮着,才能正常收到广播";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_system);
        tv_system = findViewById(R.id.tv_system);
        tv_system_ref = new WeakReference<>(tv_system);
        tv_system.setText(desc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //创建一个变更变更的广播接收器
        timeReceiver = new TimeReceiver();
        //创建一个意图过滤器,只处理系统分钟变化的广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        //注册广播接收器,注册之后才能正常接收广播
        registerReceiver(timeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销广播接收器,注销之后就不再接收广播
        unregisterReceiver(timeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理静态引用
        if (tv_system_ref != null) {
            tv_system_ref.clear();
            tv_system_ref = null;
        }
        desc = "开始侦听分钟广播,请稍等。注意要保持屏幕亮着,才能正常收到广播";
    }

    //声明一个分钟广播的接收器
    private TimeReceiver timeReceiver;

    //定义一个分钟广播的接收器
    public static class TimeReceiver extends BroadcastReceiver {

        //一旦接收到分钟变更的广播,马上触发接收器的onReceive方法
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                TextView tv = tv_system_ref != null ? tv_system_ref.get() : null;
                if (tv != null) {
                    Bundle bundle = intent.getExtras(); //获取广播携带的包裹
                    Set<String> key_set = bundle.keySet();
                    String content = "";
                    for (String key : key_set) {
                        content = String.format(Locale.getDefault(), "%s\n%s=%s", content, key, bundle.get(key));
                    }
                    desc = String.format(Locale.getDefault(), "%s\n%s 收到一个%s广播, 内容是%s", desc,
                            DateUtil.getNowTime(), intent.getAction(), content);
                    tv.setText(desc);
                }
            }
        }
    }

}
