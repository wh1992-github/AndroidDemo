package com.example.group.windowdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.group.R;

public class BottomDialog extends Dialog implements View.OnClickListener {
    private OnItemClickListener mListener;
    private TextView mTv1, mTv2, mTv3;

    public BottomDialog(Context context, OnItemClickListener listener) {
        super(context, R.style.BottomDialogStyle);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mTv3 = findViewById(R.id.tv3);
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mListener.onItemClick("底部的：" + ((TextView) v).getText().toString());
        dismissDialog();
    }

    public void showDialog() {
        show();
    }

    public void dismissDialog() {
        dismiss();
    }
}