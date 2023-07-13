package com.example.hezi.viewpager.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hezi.R;
import com.example.hezi.databinding.FragmentThreeBinding;
import com.example.hezi.viewpager.LogUtil;
import com.example.hezi.viewpager.adapter.ThreeAdapter;
import com.example.hezi.viewpager.transformer.GalleryTransformer;

public class FragmentThree extends BaseFragment {
    private static final String TAG = "FragmentThree";
    private int[] mImages = {R.drawable.b, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h};
    private ThreeAdapter mThreeAdapter;
    private FragmentThreeBinding mViewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView: " + getUserVisibleHint());
        mViewBinding = FragmentThreeBinding.inflate(getLayoutInflater(), container, false);
        initDate();
        return mViewBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDate() {
        mThreeAdapter = new ThreeAdapter(mActivity, mImages);
        mViewBinding.vp2.setAdapter(mThreeAdapter);
        mViewBinding.vp2.setCurrentItem(2);
        mViewBinding.vp2.setOffscreenPageLimit(5);
        mViewBinding.vp2.setPageMargin(20);
        mThreeAdapter.notifyDataSetChanged();
        mViewBinding.vp2.setPageTransformer(false, new GalleryTransformer());
        //事件分发，处理页面滑动问题
        mViewBinding.lll.setOnTouchListener((v, event) -> mViewBinding.vp2.dispatchTouchEvent(event));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i(TAG, "onDestroyView: ");
    }

    @Override
    public String getTAG() {
        return TAG;
    }
}
