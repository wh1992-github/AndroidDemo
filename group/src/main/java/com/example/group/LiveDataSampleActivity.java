package com.example.group;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.group.livedata.NetworkLiveData;
import com.example.group.livedata.OneFragment;
import com.example.group.livedata.TestViewModel;
import com.example.group.livedata.TwoFragment;
import com.example.group.util.LogUtil;

public class LiveDataSampleActivity extends AppCompatActivity {
    private static final String TAG = "LiveDataSampleActivity";
    private static final String mKey = "LiveData";

    public TestViewModel mTestViewModel;
    private TextView mTvName, mTvNameForever;
    private Button mBtnChangeName;
    private String[] mNames = new String[]{"lufei", "sulong", "shanzhi", "NaMei"};
    private int i = 0;
    private OneFragment mOneFragment;
    private TwoFragment mTwoFragment;
    private MutableLiveData<String> mNameEvent;
    private Observer<String> mForeverObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_sample);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNameEvent.removeObserver(mForeverObserver);
    }

    private void initView() {
        mTvName = findViewById(R.id.tv_name);
        mTvNameForever = findViewById(R.id.tv_name_forever);
        mBtnChangeName = findViewById(R.id.btn_change_name);
        mBtnChangeName.setOnClickListener(v -> {
            i++;
            String name = mNames[i % mNames.length];
            mNameEvent.setValue(name);
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
