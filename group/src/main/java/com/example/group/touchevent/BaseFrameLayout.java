package com.example.group.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.group.util.LogUtil;

/**
 * Created by test on 18-10-19.
 */
@SuppressLint("ClickableViewAccessibility")
public class BaseFrameLayout extends FrameLayout {
    private static final String TAG = "BaseFrameLayout";

    public BaseFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.i(TAG, "dispatchTouchEvent: DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.i(TAG, "dispatchTouchEvent: UP");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            LogUtil.i(TAG, "dispatchTouchEvent: MOVE");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.i(TAG, "onInterceptTouchEvent: DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.i(TAG, "onInterceptTouchEvent: UP");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            LogUtil.i(TAG, "onInterceptTouchEvent: MOVE");
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.i(TAG, "onTouchEvent: DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.i(TAG, "onTouchEvent: UP");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            LogUtil.i(TAG, "onTouchEvent: MOVE");
        }
        return super.onTouchEvent(ev);
    }
}
