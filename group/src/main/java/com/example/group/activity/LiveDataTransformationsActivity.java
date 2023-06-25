package com.example.group.activity;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.group.R;
import com.example.group.bean.User;

import java.util.Locale;

@SuppressLint({"LongLogTag", "LogNotTimber"})
public class LiveDataTransformationsActivity extends AppCompatActivity {
    private static final String TAG = "LiveDataTransformationsActivity";
    private Button mBtnChangeUser;
    private Button mBtnSwitchLiveData;
    private Button mBtnMergeLiveData;
    private MutableLiveData<User> mUserLiveData;
    private LiveData<User> mTransformedLiveData;

    private MutableLiveData<Integer> mSwitchLiveData;
    private MutableLiveData<String> mSwitchLiveData1;
    private MutableLiveData<String> mSwitchLiveData2;
    private MutableLiveData<String> mSwitchLiveData3;
    private LiveData<String> mSwitchListLiveData;
    private MutableLiveData<String> mMergeLiveData1;
    private MutableLiveData<String> mMergeLiveData2;
    private MutableLiveData<String> mMergeLiveData3;
    private MediatorLiveData<String> mMergeListLiveData;
    private int mUid;
    private int mLiveDataType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_transfrom);
        initView();
        initData();
    }

    private void initView() {
        mBtnChangeUser = findViewById(R.id.btn_change_user);
        mBtnSwitchLiveData = findViewById(R.id.btn_switch_livedata);
        mBtnMergeLiveData = findViewById(R.id.btn_merge_livedata);

        mBtnChangeUser.setOnClickListener(v -> {
            mUid++;
            User user = new User(String.format(Locale.getDefault(), "test_%d", mUid), String.valueOf(mUid));
            mUserLiveData.setValue(user);
        });

        mBtnSwitchLiveData.setOnClickListener(v -> {
            mLiveDataType++;
            mSwitchLiveData.setValue(mLiveDataType);
            mSwitchLiveData1.setValue("switchLiveData1");
            mSwitchLiveData2.setValue("switchLiveData2");
            mSwitchLiveData3.setValue("switchLiveData3");
        });

        mBtnMergeLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMergeLiveData1.setValue("mMergeLiveData1");
                mMergeLiveData2.setValue("mMergeLiveData2");
                mMergeLiveData3.setValue("mMergeLiveData3");
                mMergeListLiveData.setValue("mMergeListLiveData");
            }
        });
    }

    private void initData() {
        mUserLiveData = new MutableLiveData<>();
        mUserLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i(TAG, "mUserLiveData onChanged: user = " + user);
            }
        });

        //如果想要在LiveData对象分发给观察者之前对其中存储的值进行更改,可以使用Transformations.map()
        mTransformedLiveData = Transformations.map(mUserLiveData, new Function<User, User>() {
            @Override
            public User apply(User user) {
                Log.i(TAG, "mUserLiveData apply: " + user.toString());
                return new User("apply:" + user.userName, "apply:" + user.uid);
            }
        });
        mTransformedLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i(TAG, "mUserLiveData onChanged: transformedUser = " + user);
            }
        });

        //如果想要手动控制监听其中一个的数据变化,并能根据需要随时切换监听,这时可以使用Transformations.switchMap()
        mSwitchLiveData = new MutableLiveData<>();
        mSwitchLiveData1 = new MutableLiveData<>();
        mSwitchLiveData2 = new MutableLiveData<>();
        mSwitchLiveData3 = new MutableLiveData<>();
        mSwitchListLiveData = Transformations.switchMap(mSwitchLiveData, new Function<Integer, LiveData<String>>() {
            @Override
            public LiveData<String> apply(Integer type) {
                Log.i(TAG, "mListLiveData apply: flag = " + type);
                switch (type % 3) {
                    case 0:
                        return mSwitchLiveData1;
                    case 1:
                        return mSwitchLiveData2;
                    case 2:
                        return mSwitchLiveData3;
                    default:
                        return null;
                }
            }
        });
        mSwitchListLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                Log.i(TAG, "mListLiveData onChanged: string = " + string);
            }
        });

        //合并多个LiveData数据源
        //MediatorLiveData继承自MutableLiveData,它可以将多个LiveData数据源集合起来,可以达到一个组件监听多个LiveData数据变化的目的
        mMergeLiveData1 = new MutableLiveData<>();
        mMergeLiveData2 = new MutableLiveData<>();
        mMergeLiveData3 = new MutableLiveData<>();
        mMergeListLiveData = new MediatorLiveData<>();
        mMergeListLiveData.addSource(mMergeLiveData1, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "mMergeLiveData1 onChanged: s = " + s);
            }
        });
        mMergeListLiveData.addSource(mMergeLiveData2, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "mMergeLiveData2 onChanged: s = " + s);
            }
        });
        mMergeListLiveData.addSource(mMergeLiveData3, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "mMergeLiveData3 onChanged: s = " + s);
            }
        });
        mMergeListLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "mMergeListLiveData onChanged: s = " + s);
            }
        });
    }
}
