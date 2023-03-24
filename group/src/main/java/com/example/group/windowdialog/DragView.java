package com.example.group.windowdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class DragView extends Button {
    private static final String TAG = "DragView";
    private OnDragListener mListener;
    private float mStartX;
    private float mStartY;
    private float mHeight;
    private float mWidth;
    private float mStatusBarHeight;

    public DragView(@NonNull Context context) {
        this(context, null);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mListener = (OnDragListener) context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mStatusBarHeight = getStatusBarHeight(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX() - mStartX;
                float moveY = event.getRawY() - mStartY;
                if (Math.abs(moveX) > 8 || Math.abs(moveY) > 8) {
                    mListener.moveTo(event.getRawX() - (mWidth / 2), event.getRawY() - (mHeight / 2) - mStatusBarHeight);
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    //获得状态栏的高度
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "getStatusBarHeight: result = " + result);
        return result;
    }

    public interface OnDragListener {
        void moveTo(float x, float y);
    }
}