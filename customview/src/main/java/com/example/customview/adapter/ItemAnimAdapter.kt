package com.example.customview.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.customview.R

class ItemAnimAdapter(list: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_danmu_layout, list) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.content_tv, item)
    }
}