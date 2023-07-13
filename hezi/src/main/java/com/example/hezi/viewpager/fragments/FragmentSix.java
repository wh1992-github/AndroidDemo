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

import rx.Subscription;
import rx.functions.Action1;

public class FragmentSix extends BaseFragment {
    private static final String TAG = "FragmentSix";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView: " + getUserVisibleHint());
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setText("第6页");

        Subscription subscription = RxBus.getDefault().toObservable(EventBean.class).subscribe(new Action1<EventBean>() {
            @Override
            public void call(EventBean eventBean) {
                textView.setText(eventBean.getUserId() + " --- " + eventBean.getNickName());
            }
        });
        mRxBusList.add(subscription);

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
