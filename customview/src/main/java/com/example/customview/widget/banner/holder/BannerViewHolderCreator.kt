package com.example.customview.widget.banner.holder

/**
 * 用于约束 Banner View Holder Creator 相关能力的接口。
 */
fun interface BannerViewHolderCreator<VH : BannerViewHolder<*>> {
    /**
     * 创建 BannerViewHolder
     *
     * @return BannerViewHolder
     */
    fun createViewHolder(): VH
}
