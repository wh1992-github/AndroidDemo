package com.example.customview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.customview.bean.TypeBean
import com.example.customview.databinding.AdapterItemMainBinding

/**
 * 用于适配 Main 数据的适配器。
 *
 * Item 的创建和数据绑定都直接使用 ViewBinding，避免 BaseViewHolder.setText()
 * 在运行时通过 findViewById 查找不到控件而产生空指针异常。
 */
open class MainAdapter(data: List<TypeBean>?) :
    BaseQuickAdapter<TypeBean, BaseViewHolder>(data) {

    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val binding = AdapterItemMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseViewHolder(binding.root)
    }

    override fun convert(helper: BaseViewHolder, item: TypeBean) {
        val binding = AdapterItemMainBinding.bind(helper.itemView)
        binding.titleTv.text = item.title
    }
}
