package com.example.customview.widget.banner.holder;

public interface BannerViewHolderCreator<VH extends BannerViewHolder> {
    /**
     * 创建 BannerViewHolder
     *
     * @return BannerViewHolder
     */
    VH createViewHolder();
}
