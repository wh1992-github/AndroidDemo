package com.example.customview.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import com.example.customview.databinding.HoverItemThreeBinding

/**
 * 封装 Hover Item Adapter 2 相关逻辑的类。
 */
@SuppressLint("RecyclerView")
open class HoverItemAdapter2(private val mDatas: List<String>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "MainAdapter"
        private const val TYPE_ONE = 1
        private const val TYPE_TWO = 2
        private const val TYPE_THREE = 3
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_ONE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hover_item_one, parent, false)
                OneViewHolder(view)
            }

            TYPE_TWO -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hover_item_two, parent, false)
                TwoViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hover_item_three, parent, false)
                ThreeViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_ONE -> {
                val oneViewHolder = holder as OneViewHolder
                oneViewHolder.itemView.setOnClickListener {
                    Log.i(TAG, "onItemClick: 点击了 $position")
                }
            }

            TYPE_TWO -> {
                val twoViewHolder = holder as TwoViewHolder
                twoViewHolder.itemView.setOnClickListener { view ->
                   mOnItemClickListener?.onItemClick(view, position)
                }
            }

            TYPE_THREE -> {
                val threeViewHolder = holder as ThreeViewHolder
                threeViewHolder.tv_text.text = mDatas!![position]
                threeViewHolder.tv_text.setOnClickListener {
                    Log.i(TAG, "onItemClick: 点击了 $position")
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 3) {
            TYPE_ONE
        } else if (position == 3) {
            TYPE_TWO
        } else {
            TYPE_THREE
        }
    }

    override fun getItemCount(): Int {
        return mDatas!!.size
    }

    class OneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_text: TextView = HoverItemThreeBinding.bind(itemView).tvText
    }
}
