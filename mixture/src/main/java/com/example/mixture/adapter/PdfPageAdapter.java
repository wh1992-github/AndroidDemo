package com.example.mixture.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mixture.fragment.ImageFragment;

import java.util.ArrayList;
/**
 * 用于适配 Pdf Page 数据的适配器。
 */

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
