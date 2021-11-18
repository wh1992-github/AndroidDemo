package com.example.group.windowdialog;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.group.R;

@SuppressLint("LongLogTag")
@RequiresApi(api = Build.VERSION_CODES.O)
public class FloatingService extends Service implements DragView.OnDragListener {
    private static final String TAG = "FloatingService";
    private WindowManager mWindowManager;
    private DragView mDragView;
    private WindowManager.LayoutParams mLayoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        //window 分为属于activity的和属于应用的。activity的随着activity消失，应用的不同。
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDragView = new DragView(this);
        mDragView.setText("Floating Button");
        mDragView.setAllCaps(false);
        mDragView.setBackgroundResource(R.drawable.floating_button_bg);
        mDragView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingService.this, "点击了FloatingButton", Toast.LENGTH_SHORT).show();
            }
        });

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 1000; //偏移量
        mLayoutParams.y = 300; //偏移量
        //mLayoutParams.alpha = 0.6f; //透明度
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        //应用在后台运行时，window可以不消失
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        //更多flags:https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        mWindowManager.addView(mDragView, mLayoutParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mWindowManager.removeView(mDragView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void moveTo(float x, float y) {
        Log.i(TAG, "moveTo: x = " + x + ", y = " + y);
        mLayoutParams.x = (int) x; //偏移量
        mLayoutParams.y = (int) y; //偏移量
        mWindowManager.updateViewLayout(mDragView, mLayoutParams);
    }
}
