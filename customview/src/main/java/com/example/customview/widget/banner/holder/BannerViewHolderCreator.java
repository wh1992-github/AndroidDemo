package com.example.customview.widget.banner.holder;
/**
 * 用于约束 Banner View Holder Creator 相关能力的接口。
 */

public interface BannerViewHolderCreator<VH extends BannerViewHolder> {
    /**
     * 创建 BannerViewHolder
     *
     * @return BannerViewHolder
     */
    VH createViewHolder();
}
