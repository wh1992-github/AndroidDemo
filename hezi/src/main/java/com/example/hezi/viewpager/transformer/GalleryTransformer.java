package com.example.hezi.viewpager.transformer;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class GalleryTransformer implements ViewPager.PageTransformer {
    private static final float MAX_ALPHA = 0.5f;
    private static final float MAX_SCALE = 0.8f;

    @Override
    public void transformPage(View view, float position) {
        if (Math.abs(position) > 1) {
            view.setAlpha(MAX_ALPHA);
            view.setScaleX(MAX_SCALE);
            view.setScaleY(MAX_SCALE);
        } else {
            view.setAlpha(MAX_ALPHA + MAX_ALPHA * (1 - Math.abs(position)));
            float scale = Math.max(MAX_SCALE, 1 - Math.abs(position));
            view.setScaleX(scale);
            view.setScaleY(scale);
        }
        view.setRotation(position * 20);
    }
}
