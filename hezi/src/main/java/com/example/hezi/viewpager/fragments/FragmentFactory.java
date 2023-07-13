package com.example.hezi.viewpager.fragments;

import android.app.Fragment;

import java.util.HashMap;

public class FragmentFactory {
    private static HashMap<Integer, Fragment> mSavedFragment = new HashMap<Integer, Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = mSavedFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new FragmentOne();
                    break;
                case 1:
                    fragment = new FragmentTwo();
                    break;
                case 2:
                    fragment = new FragmentThree();
                    break;
                case 3:
                    fragment = new FragmentFour();
                    break;
                case 4:
                    fragment = new FragmentFive();
                    break;
                case 5:
                    fragment = new FragmentSix();
                    break;
                case 6:
                    fragment = new FragmentSeven();
                    break;
            }
            mSavedFragment.put(position, fragment);
        }
        return fragment;
    }
}
