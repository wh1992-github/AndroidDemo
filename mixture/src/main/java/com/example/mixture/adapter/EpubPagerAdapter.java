package com.example.mixture.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mixture.fragment.HtmlFragment;

import java.util.ArrayList;

public class EpubPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> mHtmlArray = new ArrayList<>();

    public EpubPagerAdapter(FragmentManager fm, ArrayList<String> htmlArray) {
        super(fm);
        mHtmlArray = htmlArray;
    }

    public int getCount() {
        return mHtmlArray.size();
    }

    public Fragment getItem(int position) {
        return HtmlFragment.newInstance(mHtmlArray.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "封面";
        } else {
            return "第" + position + "页";
        }
    }

}
