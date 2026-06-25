package com.example.group.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;

import com.example.group.databinding.ActivityViewDrag1Binding;

/**
 * 封装 VDH Linear Layout 1 相关逻辑的类。
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class VDHLinearLayout1 extends LinearLayout {
    private static final int MIN_TOP = 100; //距离顶部最小的距离
    private ActivityViewDrag1Binding mBinding;

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
                return child == mBinding.dragBtn; //只处理dragBtn
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
                LayoutParams bottomViewLayoutParams = (LayoutParams) mBinding.bottomView.getLayoutParams();
                bottomViewLayoutParams.height = bottomViewLayoutParams.height + dy * -1;
                mBinding.bottomView.setLayoutParams(bottomViewLayoutParams);

                //改变顶部区域高度
                LayoutParams topViewLayoutParams = (LayoutParams) mBinding.topView.getLayoutParams();
                topViewLayoutParams.height = topViewLayoutParams.height + dy;
                mBinding.topView.setLayoutParams(topViewLayoutParams);
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
        mBinding = ActivityViewDrag1Binding.bind(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDragBtnHeight = mBinding.dragBtn.getMeasuredHeight();
    }
}
