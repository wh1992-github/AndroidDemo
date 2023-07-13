package com.example.hezi.viewpager.transformer;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9f;
    private static final float MIN_ALPHA = 0.5f;

    /**
     * position取值特点：
     * 假设页面从0～1，则：
     * 第一个页面position变化为[0,-1]
     * 第二个页面position变化为[1,0]
     */
    @Override
    public void transformPage(View view, float position) {
        //position表示page的位置，当前显示的为0，左边-1，右边1.以此类推
        //所以(position < -1 || position > 1) 直接设置透明度和缩放比例就行
        if (Math.abs(position) > 1) {
            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else {
            float scale = MIN_SCALE + (1 - Math.abs(position)) * (1 - MIN_SCALE);
            float alpha = MIN_ALPHA + (1 - Math.abs(position)) * (1 - MIN_ALPHA);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setAlpha(alpha);
        }
    }
}
