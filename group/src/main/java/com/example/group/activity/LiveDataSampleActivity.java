package com.example.group.activity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.group.R;
import com.example.group.databinding.ActivityLiveDataSampleBinding;
import com.example.group.livedata.NetworkLiveData;
import com.example.group.livedata.OneFragment;
import com.example.group.livedata.TestViewModel;
import com.example.group.livedata.TwoFragment;
import com.example.group.util.LogUtil;
/**
 * 用于展示 Live Data Sample 功能的 Activity。
 */

public class LiveDataSampleActivity extends AppCompatActivity {
    private ActivityLiveDataSampleBinding binding;
    private static final String TAG = "LiveDataSampleActivity";
    private static final String mKey = "LiveData";

    public TestViewModel mTestViewModel;
    private TextView mTvName, mTvNameForever;
    private Button mBtnChangeName;
    private final String[] mNames = new String[]{"lufei", "sulong", "shanzhi", "NaMei"};
    private int i = 0;
    private OneFragment mOneFragment;
    private TwoFragment mTwoFragment;
    private MutableLiveData<String> mNameEvent;
    private Observer<String> mForeverObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveDataSampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNameEvent.removeObserver(mForeverObserver);
    }

    private void initView() {
        mTvName = binding.tvName;
        mTvNameForever = binding.tvNameForever;
        mBtnChangeName = binding.btnChangeName;
        mBtnChangeName.setOnClickListener(v -> {
            i++;
            String name = mNames[i % mNames.length];
            mNameEvent.setValue(name);
            LogUtil.i(TAG, "initView: value = " + mNameEvent.getValue());
        });

        mOneFragment = OneFragment.newInstance();
        mTwoFragment = TwoFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container1, mOneFragment);
        fragmentTransaction.replace(R.id.fl_container2, mTwoFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initData() {
        mTestViewModel = ViewModelProviders.of(this, new TestViewModel.Factory(mKey)).get(TestViewModel.class);
        mNameEvent = mTestViewModel.getNameEvent();
        mNameEvent.setValue(mNames[0]);

        mNameEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogUtil.i(TAG, "onChanged: s = " + s);
                mTvName.setText(s);
            }
        });

        mForeverObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogUtil.i(TAG, "onChanged: forever s = " + s);
                mTvNameForever.setText(s);
            }
        };
        mNameEvent.observeForever(mForeverObserver);

        NetworkLiveData.getInstance(this).observe(this, new Observer<NetworkInfo>() {
            @Override
            public void onChanged(@Nullable NetworkInfo networkInfo) {
                //网络状态变化
                LogUtil.i(TAG, "onChanged: networkInfo = " + networkInfo);
            }
        });
    }
}
