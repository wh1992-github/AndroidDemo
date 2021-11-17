package com.example.group.windowdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.group.R;

//可以作为悬浮窗。
@RequiresApi(api = Build.VERSION_CODES.O)
public class WindowDialog extends Dialog {
    private static final String TAG = "WindowDialog";
    private Context mContext;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    public WindowDialog(@NonNull Context context) {
        //加上这个style，可以不让背景变暗
        super(context, R.style.TransparentDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.dialog_window);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWindow() {
        mWindow = getWindow();
        mLayoutParams = mWindow.getAttributes();
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.format = PixelFormat.TRANSPARENT;

        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.y = -220;
        mWindow.setAttributes(mLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void showDialog() {
        show();
    }

    public void dismissDialog() {
        dismiss();
    }
}
