package com.example.customview.widget.banner.holder

import android.content.Context
import android.view.View

/**
 * 用于缓存 Banner V 列表项视图的 ViewHolder。
 */
interface BannerViewHolder<T> {
    /**
     * 创建View
     *
     * @param context
     * @return
     */
    fun createView(context: Context): View

    /**
     * 绑定数据
     *
     * @param context
     * @param position
     * @param data
     */
    fun onBind(context: Context, position: Int, data: T)
}
