package com.example.hezi.viewpager.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChildrenFragment extends BaseFragment {
    private static final String TAG = "ChildrenFragment";

    public static ChildrenFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putString("index", String.valueOf(index));
        ChildrenFragment childrenFragment = new ChildrenFragment();
        childrenFragment.setArguments(bundle);
        return childrenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setText("第" + bundle.getString("index") + "页");
        return textView;
    }

    @Override
    public String getTAG() {
        return TAG;
    }
}
