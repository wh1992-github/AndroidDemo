package com.example.custom.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.custom.R;
import com.example.custom.service.BindDelayService;
import com.example.custom.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by test on 2017/10/14.
 */
public class BindDelayActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "BindDelayActivity";
    private static WeakReference<TextView> tv_delay_ref;
    private TextView tv_delay;
    private Intent mIntent; //声明一个意图对象
    private static String mDesc = "";
    private boolean mIsBound = false; //标记服务是否已绑定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_delay);
        tv_delay = findViewById(R.id.tv_delay);
        tv_delay_ref = new WeakReference<>(tv_delay);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        //创建一个通往延迟绑定服务的意图
        mIntent = new Intent(this, BindDelayService.class);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) { //点击了开始服务按钮
            startService(mIntent); //启动服务
        } else if (v.getId() == R.id.btn_bind) { //点击了绑定服务按钮
            if (!mIsBound) {
                boolean bindFlag = bindService(mIntent, mFirstConn, Context.BIND_AUTO_CREATE); //绑定服务
                if (bindFlag) {
                    mIsBound = true; //标记为已绑定
                }
                Log.d(TAG, "bindFlag=" + bindFlag);
            }
        } else if (v.getId() == R.id.btn_unbind) { //点击了解绑服务按钮
            if (mIsBound) {
                try {
                    unbindService(mFirstConn); //解绑服务
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mIsBound = false; //标记为未绑定
                mBindService = null;
            }
        } else if (v.getId() == R.id.btn_stop) { //点击了停止服务按钮
            stopService(mIntent); //停止服务
        }
    }

    public static void showText(String desc) {
        TextView tv = tv_delay_ref != null ? tv_delay_ref.get() : null;
        if (tv != null) {
            mDesc = String.format(Locale.getDefault(), "%s%s %s\n", mDesc, DateUtil.getNowDateTime("HH:mm:ss"), desc);
            tv.setText(mDesc);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理静态引用
        if (tv_delay_ref != null) {
            tv_delay_ref.clear();
            tv_delay_ref = null;
        }
        mDesc = "";
        // 如果已绑定Service，需要解绑
        if (mIsBound) {
            try {
                unbindService(mFirstConn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mIsBound = false;
        }
    }

    private BindDelayService mBindService; //声明一个服务对象
    private ServiceConnection mFirstConn = new ServiceConnection() {

        //获取服务对象时的操作
        public void onServiceConnected(ComponentName name, IBinder service) {
            //如果服务运行于另外一个进程,则不能直接强制转换类型,
            //否则会报错“java.lang.ClassCastException: android.os.BinderProxy cannot be cast to...”
            mBindService = ((BindDelayService.LocalBinder) service).getService();
            Log.d(TAG, "onServiceConnected");
        }

        //无法获取到服务对象时的操作
        public void onServiceDisconnected(ComponentName name) {
            mBindService = null;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

}
