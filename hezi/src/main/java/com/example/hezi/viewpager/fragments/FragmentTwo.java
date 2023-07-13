package com.example.hezi.viewpager.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.hezi.R;
import com.example.hezi.databinding.FragmentTwoBinding;
import com.example.hezi.viewpager.LogUtil;
import com.example.hezi.viewpager.adapter.TwoAdapter;
import com.example.hezi.viewpager.transformer.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class FragmentTwo extends BaseFragment {
    private static final String TAG = "FragmentTwo";
    private FragmentTwoBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView: " + getUserVisibleHint());
        mBinding = FragmentTwoBinding.inflate(getLayoutInflater(), container, false);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.p001);
        list.add(R.drawable.p002);
        list.add(R.drawable.p003);
        list.add(R.drawable.p004);
        list.add(R.drawable.p005);
        TwoAdapter adapter = new TwoAdapter(getActivity(), list);
        mBinding.viewpager.setAdapter(adapter);
        mBinding.viewpager.setPageTransformer(false, new ScaleTransformer());
        mBinding.viewpager.setOffscreenPageLimit(5);
        mBinding.viewpager.addOnPageChangeListener(mOnPageChangeListener);
        return mBinding.getRoot();
    }

    //截取View截图
    public Bitmap captureScreen(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //LogUtil.i(TAG, "onPageScrolled: position = " + position + ", positionOffset = " + positionOffset + ", positionOffsetPixels = " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.i(TAG, "onPageSelected: position = " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //LogUtil.i(TAG, "onPageScrollStateChanged: state = " + state);
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mBinding.iv.setImageBitmap(captureScreen(mBinding.viewpager));
            }
        }
    };

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
