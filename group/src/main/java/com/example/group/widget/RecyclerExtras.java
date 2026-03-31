package com.example.group.widget;

import android.view.View;
/**
 * 封装 Recycler Extras 相关逻辑的类。
 */

public class RecyclerExtras {

    //定义一个循环视图列表项的点击监听器接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //定义一个循环视图列表项的长按监听器接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    //定义一个循环视图列表项的删除监听器接口
    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(View view, int position);
    }

}
