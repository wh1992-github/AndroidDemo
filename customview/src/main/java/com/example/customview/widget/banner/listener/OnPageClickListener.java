package com.example.customview.widget.banner.listener;

import android.view.View;
/**
 * 用于约束 On Page Click 回调能力的接口。
 */

public interface OnPageClickListener {

    /**
     * item 点击事件
     *
     * @param view     view
     * @param position position
     */
    void onPageClick(View view, int position);

}
