package com.example.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.custom.R;
import com.example.custom.service.NormalService;
import com.example.custom.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by test on 2017/10/14.
 */
public class ServiceNormalActivity extends AppCompatActivity implements OnClickListener {
    private static WeakReference<TextView> tv_normal_ref;
    private TextView tv_normal;
    private Intent mIntent; //声明一个意图对象
    private static String mDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_normal);
        tv_normal = findViewById(R.id.tv_normal);
        tv_normal_ref = new WeakReference<>(tv_normal);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        //创建一个通往普通服务的意图
        mIntent = new Intent(this, NormalService.class);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) { //点击了启动服务按钮
            startService(mIntent); //启动指定意图的服务
        } else if (v.getId() == R.id.btn_stop) { //点击了停止服务按钮
            stopService(mIntent); //停止指定意图的服务
        }
    }

    public static void showText(String desc) {
        TextView tv = tv_normal_ref != null ? tv_normal_ref.get() : null;
        if (tv != null) {
            mDesc = String.format(Locale.getDefault(), "%s%s %s\n", mDesc, DateUtil.getNowDateTime("HH:mm:ss"), desc);
            tv.setText(mDesc);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理静态引用
        if (tv_normal_ref != null) {
            tv_normal_ref.clear();
            tv_normal_ref = null;
        }
        mDesc = "";
    }

}
