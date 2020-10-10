package com.example.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtil {
    private static final String TAG = "KeyboardUtil";

    //打开软键盘
    public static void showKeyboard(View view) {
        LogUtil.i(TAG, "showKeyboard: ");
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    //关闭软键盘
    public static void hideKeyboard(View view) {
        LogUtil.i(TAG, "hideKeyboard: ");
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void openKeyboard(EditText mEditText, Context mContext) {
        LogUtil.i(TAG, "openKeyboard: ");
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
