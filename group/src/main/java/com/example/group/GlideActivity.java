package com.example.group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.group.fragment.GlideFragment;

/**
 * Glide的核心思想:
 * 1.对象池:
 * Glide原理的核心是为bitmap维护一个对象池.对象池的主要目的是通过减少大对象内存的分配以重用来提高性能.
 * 2.生命周期绑定:
 * 第一个with方法,这个其实就是一个工厂方法,虽然有许多重载的形式,其实都是要创建一个RequestManager对象.
 * 图片的加载任务会与activity或者Fragment的生命周期绑定,当界面执行onStop的使用自动暂定,而当执行onStart的时候又会自动重新开启,
 * 同样的,动态Gif图的加载也是如此,以用来节省电量,同时Glide会对网络状态做监听,当网络状态发生改变时,会重启失败的任务,以减少任务因网络连接问题而失败的概率.
 * 3.预览图的使用
 * 为加快加载速度,提高体验,优先加载预览图
 * <p>
 * 传入的Context参数主要分Application类型和非Application类型:
 * 如果是Application类型参数,不需要处理生命周期,因为Application对象的生命周期就是应用程序的生命周期
 * 如果是非Application类型参数,需要处理生命周期,Glide使用添加隐藏Fragment的这种小技巧,因为Fragment的生命周期和Activity是同步的
 * 但是,在非主线程当中使用的Glide,那么不管你是传入的Activity还是Fragment,都会被强制当成Application来处理.
 * <p>
 */
@SuppressLint("NonConstantResourceId")
public class GlideActivity extends AppCompatActivity {
    private static final String TAG = "GlideActivity";
    private static final String FRAGMENT_TAG = "fragment_glide";
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        findViewById(R.id.btn1).setOnClickListener(this::onClick);
        findViewById(R.id.btn2).setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                testThreadName();
                break;
            case R.id.btn2:
                addEmptyFragment();
                break;
        }
    }

    private void testThreadName() {
        Log.i(TAG, "onClick: threadName = " + Thread.currentThread().getName() + ", isOnMainThread = " + (Looper.myLooper() == Looper.getMainLooper()));
        new Thread(() -> {
            Log.i(TAG, "onClick: Thread threadName = " + Thread.currentThread().getName() + ", isOnMainThread = " + (Looper.myLooper() == Looper.getMainLooper()));
            mHandler.post(() -> Log.i(TAG, "onClick: Handler threadName = " + Thread.currentThread().getName() + ", isOnMainThread = " + (Looper.myLooper() == Looper.getMainLooper())));
        }).start();
    }

    private void addEmptyFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new GlideFragment();
        }
        fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
