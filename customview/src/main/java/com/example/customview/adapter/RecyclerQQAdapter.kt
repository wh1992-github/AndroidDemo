package com.example.customview.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.databinding.ItemRecyclerQqBinding
import com.example.customview.widget.RecyclerQQItemView

/**
 * 用于适配 Recycler QQ 数据的适配器。
 */
open class RecyclerQQAdapter(
    private var mContext: Context?,
    private var mDataImage: MutableList<Bitmap>?,
    private var mDataTitle: List<String>?,
    private var mDataContent: List<String>?
) : RecyclerView.Adapter<RecyclerQQAdapter.MyViewHolder>(), RecyclerQQItemView.onSlidingButtonListener {
    private var onSvcl: onSlidingViewClickListener? = null
    private var recyclers: RecyclerQQItemView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerQqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageBitmap(mDataImage!![position])
        holder.title.text = mDataTitle!![position]
        holder.content.text = mDataContent!![position]
        holder.layout_left.layoutParams.width = getScreenWidth(mContext!!)

        holder.layout_left.setOnClickListener { view ->
            if (menuIsOpen()) {
                closeMenu()
            } else {
                val subscript = holder.layoutPosition
                onSvcl!!.onItemClick(view, subscript)
            }
        }
        holder.other.setOnClickListener {
            Toast.makeText(mContext, "其他：$position", Toast.LENGTH_SHORT).show()
        }
        holder.delete.setOnClickListener { view ->
            Toast.makeText(mContext, "删除了：$position", Toast.LENGTH_SHORT).show()
            val subscript = holder.layoutPosition
            onSvcl!!.onDeleteBtnClick(view, subscript)
        }
    }

    override fun getItemCount(): Int {
        return mDataImage!!.size
    }

    inner class MyViewHolder(binding: ItemRecyclerQqBinding) : RecyclerView.ViewHolder(binding.root) {
        var image: ImageView = binding.image
        var title: TextView = binding.title
        var content: TextView = binding.content
        var other: TextView = binding.other
        var delete: TextView = binding.delete
        var layout_left: RelativeLayout = binding.layoutLeft

        init {
            binding.root.setSlidingButtonListener(this@RecyclerQQAdapter)
        }
    }

    //删除数据
    fun removeData(position: Int) {
        mDataImage!!.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onMenuIsOpen(view: View) {
        recyclers = view as RecyclerQQItemView
    }

    override fun onDownOrMove(recycler: RecyclerQQItemView) {
        if (menuIsOpen() && recyclers !== recycler) {
            closeMenu()
        }
    }

    //关闭菜单
    fun closeMenu() {
        recyclers!!.closeMenu()
        recyclers = null
    }

    //判断是否有菜单打开
    fun menuIsOpen(): Boolean {
        return recyclers != null
    }

    //设置在滑动侦听器上
    fun setOnSlidListener(listener: onSlidingViewClickListener?) {
        onSvcl = listener
    }

    //在滑动视图上单击侦听器
    interface onSlidingViewClickListener {
        fun onItemClick(view: View, position: Int)

        fun onDeleteBtnClick(view: View, position: Int)
    }

    //获取屏幕宽度
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    companion object {
        private const val TAG = "RecyclerViewAdapter"
    }
}
