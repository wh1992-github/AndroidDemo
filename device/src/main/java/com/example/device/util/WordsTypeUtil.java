package com.example.device.util;

import android.util.Log;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

public class WordsTypeUtil {
    private static final String TAG = "WordsTypeUtil";

    //繁体转简体
    public static String changeT_S(String changeText) {
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            changeText = jChineseConvertor.t2s(changeText);
        } catch (IOException e) {
            Log.e(TAG, "繁体转简体: e = " + e.getMessage());
        }
        return changeText;
    }

    //简体转繁体
    public static String changeS_T(String changeText) {
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            changeText = jChineseConvertor.s2t(changeText);
        } catch (IOException e) {
            Log.e(TAG, "简体转繁体: e = " + e.getMessage());
        }
        return changeText;
    }
}
