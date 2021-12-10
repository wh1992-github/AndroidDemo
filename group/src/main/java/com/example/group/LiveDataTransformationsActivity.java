package com.example.group;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.group.bean.User;
import com.example.group.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class LiveDataTransformationsActivity extends AppCompatActivity {
    private static final String TAG = "LiveDataTransformationsActivity";
    private Button mBtnChangeUser;
    private Button mBtnChangeUserName;
    public long mUid;
    private MutableLiveData<User> mUserLiveData;
    private LiveData<String> mUserNameLiveData;
    private LiveData<List<String>> mListLiveData;
    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_transfrom);
        initView();
        initData();
    }

    private void initView() {
        mBtnChangeUser = findViewById(R.id.btn_change_user);
        mBtnChangeUserName = findViewById(R.id.btn_change_user_name);
        mTvContent = findViewById(R.id.tv_content);

        mBtnChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvContent.setText("");
                mUid++;
                User user = new User(getUserName(), String.valueOf(mUid));
                mUserLiveData.setValue(user);
            }
        });

        mBtnChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        mUserLiveData = new MutableLiveData<>();
        mUserLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                LogUtil.i(TAG, "onChanged: user = " + user);
                appendText("userLiveData", user);
            }
        });

        mUserNameLiveData = Transformations.map(mUserLiveData, new Function<User, String>() {
            @Override
            public String apply(User input) {
                return input.userName;
            }
        });
        mUserNameLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogUtil.i(TAG, "onChanged: s = " + s);
                appendText("userNameLiveData change", s);
            }
        });

        mListLiveData = Transformations.switchMap(mUserLiveData, new Function<User, LiveData<List<String>>>() {
            @Override
            public LiveData<List<String>> apply(User input) {
                MutableLiveData<List<String>> listMutableLiveData = new MutableLiveData<>();
                List<String> list = new ArrayList<>();
                list.add(input.userName);
                list.add(input.uid);
                listMutableLiveData.setValue(list);
                return listMutableLiveData;
            }
        });
        mListLiveData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                LogUtil.i(TAG, "onChanged: strings = " + strings);
                appendText("listLiveData change", strings);
            }
        });
    }

    private void appendText(String preText, Object object) {
        CharSequence text = mTvContent.getText();
        String result = text + preText + ":" + object + "\n";
        LogUtil.i(TAG, "appendText: result = " + result);
        mTvContent.setText(result);
    }

    @NonNull
    private String getUserName() {
        return "test_" + mUid;
    }
}
