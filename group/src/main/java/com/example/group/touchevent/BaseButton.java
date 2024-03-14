package com.example.group.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.group.util.LogUtil;

/**
 * Created by test on 18-10-19.
 */

@SuppressLint({"AppCompatCustomView", "ClickableViewAccessibility"})
public class BaseButton extends Button implements View.OnTouchListener {
    private static final String TAG = "BaseButton";

    public BaseButton(Context context) {
        this(context, null);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.i(TAG, "dispatchTouchEvent: DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.i(TAG, "dispatchTouchEvent: UP");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            LogUtil.i(TAG, "onTouchEvent: MOVE");
        }
        return super.dispatchTouchEvent(ev);
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

    //先执行setOnTouchListener,然后执行onTouchEvent
    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.i(TAG, "OnTouchListener: BaseButton DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.i(TAG, "OnTouchListener: BaseButton UP");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            LogUtil.i(TAG, "OnTouchListener: BaseButton MOVE");
        }
        //返回值会影响后续事件的分发
        return false;
    }
}
