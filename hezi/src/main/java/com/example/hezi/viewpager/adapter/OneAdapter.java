package com.example.hezi.viewpager.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.example.hezi.viewpager.LogUtil;
import com.example.hezi.viewpager.fragments.ChildrenFragment;
import com.example.hezi.viewpager.fragments.FragmentPagerAdapter;

public class OneAdapter extends FragmentPagerAdapter {
    private static final String TAG = "OneAdapter";
    private ViewPager mViewPager;

    public OneAdapter(ViewPager viewPager, FragmentManager fm) {
        super(fm);
        mViewPager = viewPager;
    }

    @Override
    public Fragment getItem(int i) {
        LogUtil.i(TAG, "getItem: " + i);
        return ChildrenFragment.newInstance(i % 4);
    }

    @Override
    public int getCount() {
        return 4 * 2;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
        LogUtil.i(TAG, "startUpdate: ");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
        LogUtil.i(TAG, "finishUpdate: " + mViewPager.getCurrentItem());
        int pos = mViewPager.getCurrentItem();
        if (pos == 0) {
            mViewPager.setCurrentItem(4, false);
        } else if (pos == 7) {
            mViewPager.setCurrentItem(3, false);
        }
    }
}