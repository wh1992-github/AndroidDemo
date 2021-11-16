package com.example.middle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.example.middle.adapter.SearchAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EditAutoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditAutoActivity";
    private static final ArrayList<String> mList = new ArrayList<>();
    private ImageView mEmptyIv;
    private AutoCompleteTextView mAutoCompleteTextView;
    private SearchAdapter mAdapter;
    //AutoCompleteTextView的doBeforeTextChanged方法
    private Method doBeforeTextChanged;
    //AutoCompleteTextView的doAfterTextChanged方法
    private Method doAfterTextChanged;

    static {
        mList.add("大大大1");
        mList.add("大大小2");
        mList.add("大小大3");
        mList.add("大小小4");
        mList.add("小大大5");
        mList.add("小大小6");
        mList.add("小小大7");
        mList.add("小小小8");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auto);
        mEmptyIv = findViewById(R.id.empty);
        mEmptyIv.setOnClickListener(this);
        mAutoCompleteTextView = findViewById(R.id.search);
        mAdapter = new SearchAdapter(this, mList);
        mAutoCompleteTextView.setAdapter(mAdapter);
        mAutoCompleteTextView.addTextChangedListener(textWatcher);
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, "beforeTextChanged: ");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "onTextChanged: ");
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.i(TAG, "afterTextChanged: " + mAutoCompleteTextView.getText().toString());
            dealSearchHint();
        }
    };

    private void dealSearchHint() {
        //更新提示列表
        if (mList.size() == 8) {
            mList.add("大大大小小小9");
            mList.add("小小小大大大10");
            mAdapter.setData(mList);
            refreshDropList();
        }
    }


    /**
     * 下拉提示框数据获取到后刷新此框
     * 通过反射调用AutoCompleteTextView的doBeforeTextChanged和doAfterTextChanged方法来实现刷新下拉提示框
     */
    @SuppressLint("DiscouragedPrivateApi")
    private void refreshDropList() {
        try {
            if (doAfterTextChanged == null) {
                Class<?> autoCompleteTextView = Class.forName("android.widget.AutoCompleteTextView");
                doBeforeTextChanged = autoCompleteTextView.getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
                doAfterTextChanged = autoCompleteTextView.getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            }
            mAutoCompleteTextView.showDropDown();
            doBeforeTextChanged.invoke(mAutoCompleteTextView);
            doAfterTextChanged.invoke(mAutoCompleteTextView);
        } catch (Exception e) {
            Log.i(TAG, "refreshDropList: e = " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                Log.i(TAG, "onClick: text = " + mAutoCompleteTextView.getText().toString());
                mAutoCompleteTextView.setText("");
                break;
        }
    }
}
