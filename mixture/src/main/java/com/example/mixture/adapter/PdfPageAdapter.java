package com.example.mixture.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mixture.fragment.ImageFragment;

import java.util.ArrayList;

public class PdfPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> mImgArray = new ArrayList<>();

    public PdfPageAdapter(FragmentManager fm, ArrayList<String> imgArray) {
        super(fm);
        mImgArray = imgArray;
    }

    public int getCount() {
        return mImgArray.size();
    }

    public Fragment getItem(int position) {
        return ImageFragment.newInstance(mImgArray.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "第" + (position + 1) + "页";
    }

}
