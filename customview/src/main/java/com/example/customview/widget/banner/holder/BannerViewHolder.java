package com.example.customview.widget.banner.holder;

import android.content.Context;
import android.view.View;
/**
 * 用于缓存 Banner V 列表项视图的 ViewHolder。
 */

public interface BannerViewHolder<T> {

    /**
     * 创建View
     *
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     *
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);

}
