package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.group.R;
import com.example.group.touchevent.BaseButton;
import com.example.group.touchevent.BaseFrameLayout;
import com.example.group.touchevent.BaseImageView;
import com.example.group.touchevent.BaseLinearLayout;
import com.example.group.touchevent.BaseRelativeLayout;
import com.example.group.touchevent.BaseTextView;
import com.example.group.util.LogUtil;

//事件分发机制
@SuppressLint("ClickableViewAccessibility")
public class TouchEventActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private BaseFrameLayout mFrameLayout;
    private BaseRelativeLayout mRelativeLayout;
    private BaseLinearLayout mLinearLayout;
    private BaseTextView mTextView;
    private BaseImageView mImageView;
    private BaseButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        mFrameLayout = findViewById(R.id.framelayout);
        mRelativeLayout = findViewById(R.id.relativelayout);
        mLinearLayout = findViewById(R.id.linearlayout);
        mTextView = findViewById(R.id.textview);
        mImageView = findViewById(R.id.imageview);
        mButton = findViewById(R.id.button);

        //先执行setOnTouchListener,然后执行onTouchEvent
        mButton.setOnTouchListener((v, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                LogUtil.i(TAG, "onTouch: BaseButton DOWN");
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                LogUtil.i(TAG, "onTouch: BaseButton UP");
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                LogUtil.i(TAG, "onTouch: BaseButton MOVE");
            }
            //返回值会影响后续事件的分发
            return false;
        });
        //注释掉和放开,结果是不一样的,可以看log加深理解
        //mFrameLayout.setOnClickListener(this);
        //mRelativeLayout.setOnClickListener(this);
        //mLinearLayout.setOnClickListener(this);
        //mTextView.setOnClickListener(this);
        //mImageView.setOnClickListener(this);
        //mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.framelayout:
                LogUtil.i(TAG, "onClick: BaseFrameLayout");
                break;
            case R.id.relativelayout:
                LogUtil.i(TAG, "onClick: BaseRelativeLayout");
                break;
            case R.id.linearlayout:
                LogUtil.i(TAG, "onClick: BaseLinearLayout");
                break;
            case R.id.textview:
                LogUtil.i(TAG, "onClick: BaseTextView");
                break;
            case R.id.imageview:
                LogUtil.i(TAG, "onClick: BaseImageView");
                break;
            case R.id.button:
                LogUtil.i(TAG, "onClick: BaseButton");
                break;
            default:
                break;
        }
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
