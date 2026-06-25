package com.example.group.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import com.example.group.databinding.ActivityViewDrag2Binding;

/**
 * 封装 VDH Linear Layout 2 相关逻辑的类。
 */

public class VDHLinearLayout2 extends LinearLayout {

    private ViewDragHelper mViewDragHelper;
    private int mAutoBackViewOriginLeft;
    private int mAutoBackViewOriginTop;

    private ActivityViewDrag2Binding mBinding;

    public VDHLinearLayout2(Context context) {
        this(context, null);
    }

    public VDHLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLinearLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                //true表示允许拖动，false表示不允许拖动
                return child == mBinding.dragView || child == mBinding.autoBackView;
            }

            //确认可以拖动capturedChild的回调
            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            //拖动状态的变化的回调
            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }

            //child位置发生变化的回调
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            //松开子View时的回调
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild == mBinding.autoBackView) {
                    mViewDragHelper.settleCapturedViewAt(mAutoBackViewOriginLeft, mAutoBackViewOriginTop);
                    invalidate();
                }
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mBinding.edgeDragView, pointerId);
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }
        });
        //设置左边缘可以被Drag
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
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
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBinding = ActivityViewDrag2Binding.bind(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackViewOriginLeft = mBinding.autoBackView.getLeft();
        mAutoBackViewOriginTop = mBinding.autoBackView.getTop();
    }
}
