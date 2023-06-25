package com.example.group.livedata;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.group.activity.LiveDataSampleActivity;
import com.example.group.R;
import com.example.group.util.LogUtil;

@SuppressLint("SetTextI18n")
public class TwoFragment extends BaseFragment {
    private static final String TAG = "TwoFragment";
    private View mView;
    private TextView mTvName;
    private TestViewModel mViewModel;

    public static TwoFragment newInstance() {
        TwoFragment fragment = new TwoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(mActivity).get(TestViewModel.class);
        mViewModel.getNameEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogUtil.i(TAG, "onChanged: s = " + s);
                mTvName.setText("TwoFragment: " + s);
                boolean result = (mViewModel == ((LiveDataSampleActivity) mActivity).mTestViewModel);
                LogUtil.i(TAG, "onChanged: s result = " + result);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_text, container, false);
        initView();
        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        mTvName = mView.findViewById(R.id.tv_name);
    }

}
