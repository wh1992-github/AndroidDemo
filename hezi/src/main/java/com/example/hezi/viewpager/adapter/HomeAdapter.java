package com.example.hezi.viewpager.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.hezi.viewpager.fragments.FragmentFactory;
import com.example.hezi.viewpager.fragments.FragmentPagerAdapter;

public class HomeAdapter extends FragmentPagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentFactory.getFragment(i);
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "第" + (position + 1) + "页";
    }
}