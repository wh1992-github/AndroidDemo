package com.example.customview.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.customview.R
import com.example.customview.bean.UserBean
import com.example.customview.utils.DisplayUtils

/**
 * 用于适配 Hover Item 数据的适配器。
 */
class HoverItemAdapter(data: List<UserBean>?) :
    BaseQuickAdapter<UserBean, BaseViewHolder>(R.layout.adapter_item_hover_user, data) {
    override fun convert(helper: BaseViewHolder, item: UserBean) {
        helper.setText(R.id.user_name_tv, item.userName)
    }
}
