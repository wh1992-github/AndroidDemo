package com.example.customview.tagview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import com.example.customview.databinding.TagLayoutBinding

/**
 * Created by test on 2017/4/14.
 *
 * 评论页面的适配器
 */
open class TagAdapter(private var tagList: List<TagBean>) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    private var isSelected = false
    private val selectList = ArrayList<TagBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TagLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = tagList[position].tag_name
        holder.itemView.tag = tagList[position]
        holder.mTextView.setOnClickListener {
            isSelected = !holder.mTextView.isSelected
            if (isSelected) {
                holder.mTextView.isSelected = true
                holder.mTextView.setBackgroundResource(R.drawable.tag_checked_bg)
                selectList.add(tagList[position])
            } else {
                holder.mTextView.isSelected = false
                holder.mTextView.setBackgroundResource(R.drawable.tag_normal_bg)
                selectList.remove(tagList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    inner class ViewHolder(binding: TagLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @JvmField
        var mTextView: TextView = binding.tagTv
    }

    fun getSelectData(): List<TagBean> {
        return selectList
    }
}
