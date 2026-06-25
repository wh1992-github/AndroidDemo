package com.example.customview.utils

import com.example.customview.bean.UserBean
import java.util.Comparator

/**
 * 封装 Pinyin Comparator 相关逻辑的类。
 */
open class PinyinComparator : Comparator<UserBean> {
    override fun compare(o1: UserBean, o2: UserBean): Int = when {
        o1.sortLetters!!.equals("@") || o2.sortLetters!!.equals("#") -> -1
        o1.sortLetters!!.equals("#") || o2.sortLetters!!.equals("@") -> 1
        else -> o1.sortLetters!!.compareTo(o2.sortLetters!!)
    }
}
