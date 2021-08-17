package com.example.group.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by wh on 18-10-19.
 */
@SuppressLint("ClickableViewAccessibility")
public class BaseRelativeLayout extends RelativeLayout {
    private static final String TAG = "BaseRelativeLayout";

    public BaseRelativeLayout(Context context) {
        super(context);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
