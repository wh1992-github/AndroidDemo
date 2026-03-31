package com.example.customview.utils;


import com.example.customview.bean.UserBean;

import java.util.Comparator;
/**
 * 封装 Pinyin Comparator 相关逻辑的类。
 */

public class PinyinComparator implements Comparator<UserBean> {

    @Override
    public int compare(UserBean o1, UserBean o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
