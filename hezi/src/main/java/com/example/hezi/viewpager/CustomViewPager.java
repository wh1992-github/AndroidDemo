package com.example.hezi.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.Objects;

public class CustomViewPager extends ViewPager {
    private static final String TAG = "CustomViewPager";
    private CustomScroller mScroller;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Class<ViewPager> clazz = ViewPager.class;
        try {
            Field mFirstLayout = clazz.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);
            Objects.requireNonNull(getAdapter()).notifyDataSetChanged();
            setCurrentItem(getCurrentItem());
        } catch (Exception e) {
            LogUtil.e(TAG, "onAttachedToWindow: e = " + e.getMessage());
        }
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            mScroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
        } else {
            mScroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }

    public void initScroller() {
        mScroller = new CustomScroller(getContext());
        Class<ViewPager> clazz = ViewPager.class;
        try {
            //利用反射设置属性mScroller为自定义的Scroller
            Field field = clazz.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, mScroller);
        } catch (Exception e) {
            LogUtil.e(TAG, "initScroller: Exception = " + e.getMessage());
        }
    }

    static class CustomScroller extends Scroller {
        private boolean noDuration;
        private static final Interpolator sInterpolator = t -> {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        };

        public void setNoDuration(boolean noDuration) {
            this.noDuration = noDuration;
        }

        public CustomScroller(Context context) {
            this(context, sInterpolator);
        }

        public CustomScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            //界面滑动不需要时间间隔
            if (noDuration) {
                super.startScroll(startX, startY, dx, dy, 0);
            } else {
                super.startScroll(startX, startY, dx, dy, duration);
            }
        }
    }
}
