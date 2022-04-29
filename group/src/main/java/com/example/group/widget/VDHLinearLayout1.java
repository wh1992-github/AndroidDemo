package com.example.group.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.group.R;

public class VDHLinearLayout1 extends LinearLayout {
    private static final int MIN_TOP = 100; //距离顶部最小的距离
    private ScrollView mScrollView;
    private Button mDragButton;
    private ScrollView mBottomView;

    private int mDragBtnHeight;
    private ViewDragHelper mViewDragHelper;

    public VDHLinearLayout1(Context context) {
        this(context, null);
    }

    public VDHLinearLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLinearLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragButton; //只处理dragBtn
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > getHeight() - mDragBtnHeight) //底部边界
                {
                    top = getHeight() - mDragBtnHeight;
                } else if (top < MIN_TOP) //顶部边界
                {
                    top = MIN_TOP;
                }
                return top;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                //改变底部区域高度
                LayoutParams bottomViewLayoutParams = (LayoutParams) mBottomView.getLayoutParams();
                bottomViewLayoutParams.height = bottomViewLayoutParams.height + dy * -1;
                mBottomView.setLayoutParams(bottomViewLayoutParams);

                //改变顶部区域高度
                LayoutParams topViewLayoutParams = (LayoutParams) mScrollView.getLayoutParams();
                topViewLayoutParams.height = topViewLayoutParams.height + dy;
                mScrollView.setLayoutParams(topViewLayoutParams);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScrollView = (ScrollView) findViewById(R.id.topView);
        mDragButton = (Button) findViewById(R.id.dragBtn);
        mBottomView = (ScrollView) findViewById(R.id.bottomView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDragBtnHeight = mDragButton.getMeasuredHeight();
    }
}
