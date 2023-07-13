package com.example.hezi.viewpager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hezi.viewpager.EventBean;
import com.example.hezi.viewpager.LogUtil;
import com.example.hezi.viewpager.RxBus;

public class FragmentSeven extends BaseFragment {
    private static final String TAG = "FragmentSeven";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView: " + getUserVisibleHint());
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setText("第7页");

        textView.setOnClickListener(v -> RxBus.getDefault().post(new EventBean(1, "nickName")));

        return textView;
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
