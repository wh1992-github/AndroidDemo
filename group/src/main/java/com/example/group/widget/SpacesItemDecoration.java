package com.example.group.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
/**
 * 封装 Spaces Item Decoration 相关逻辑的类。
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space; //空白间隔

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space; //左边空白间隔
        outRect.right = space; //右边空白间隔
        outRect.bottom = space; //上方空白间隔
        outRect.top = space; //下方空白间隔
    }
}
