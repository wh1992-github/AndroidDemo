package com.example.hezi.viewpager.fragments;

import android.app.Fragment;
import android.content.Context;

import com.example.hezi.activity.MApp;
import com.example.hezi.viewpager.ViewpagerActivity;

import java.util.ArrayList;

import rx.Subscription;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected ViewpagerActivity mActivity;
    private Context mContext;

    public Context getContext() {
        if (mContext == null) {
            mContext = MApp.getInstance();
        }
        return mContext;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ViewpagerActivity) context;
    }

    public abstract String getTAG();


    protected ArrayList<Subscription> mRxBusList = new ArrayList<>();

    /**
     * 取消该页面所有订阅
     */
    private void clearSubscription() {
        for (Subscription subscription : mRxBusList) {
            if (subscription != null && subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        clearSubscription();
    }
}
