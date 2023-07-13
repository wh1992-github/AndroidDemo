package com.example.hezi.viewpager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.hezi.databinding.FragmentOneBinding;
import com.example.hezi.viewpager.LogUtil;
import com.example.hezi.viewpager.adapter.OneAdapter;

public class FragmentOne extends BaseFragment {
    private static final String TAG = "FragmentOne";
    private OneAdapter mOneAdapter;
    private FragmentOneBinding mViewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView: " + getUserVisibleHint());
        mViewBinding = FragmentOneBinding.inflate(getLayoutInflater(), container, false);
        initData();
        return mViewBinding.getRoot();
    }

    private void initData() {
        mOneAdapter = new OneAdapter(mViewBinding.vp1, getChildFragmentManager());
        mViewBinding.vp1.setAdapter(mOneAdapter);
        mOneAdapter.notifyDataSetChanged();
        mViewBinding.vp1.setCurrentItem(4);
        mViewBinding.vp1.setOffscreenPageLimit(1);

        mViewBinding.vp1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "onPageScrollStateChanged: " + state + ", " + mViewBinding.vp1.getCurrentItem());
                if (state == 0) {
//                    if (mViewBinding.vp1.getCurrentItem() == 0) {
//                        mViewBinding.vp1.setCurrentItem(5, false);
//                    } else if (mViewBinding.vp1.getCurrentItem() == 9) {
//                        mViewBinding.vp1.setCurrentItem(4, false);
//                    }
                }
            }
        });
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
