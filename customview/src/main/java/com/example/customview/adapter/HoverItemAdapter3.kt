package com.example.customview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.bean.HoverItemModel
import com.example.customview.databinding.LayoutListItemBinding
import com.example.customview.utils.DisplayUtils

/**
 * 封装 Hover Item Adapter 3 相关逻辑的类。
 */

open class HoverItemAdapter3(
    private val mContext: Context?,
    private val mList: List<HoverItemModel>?
) : RecyclerView.Adapter<HoverItemAdapter3.MyViewHolder>() {

    companion object {
        const val HAS_STICKY_VIEW = 1
        const val NONE_STICKY_VIEW = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LayoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val hoverItemModel: HoverItemModel = mList!![position]
        viewHolder.tvName.text = hoverItemModel.name
        if ((position == 0) || hoverItemModel.sticky!! != mList[position - 1].sticky) {
            viewHolder.tvHeader.visibility = View.VISIBLE
            viewHolder.tvHeader.text = hoverItemModel.sticky
            viewHolder.itemView.tag = HAS_STICKY_VIEW
        } else {
            viewHolder.tvHeader.visibility = View.GONE
            viewHolder.itemView.tag = NONE_STICKY_VIEW
        }
        viewHolder.itemView.setContentDescription(hoverItemModel.sticky)
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    open class MyViewHolder(binding: LayoutListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvHeader: TextView = binding.headerView
        val tvName: TextView = binding.tvName
    }
}
