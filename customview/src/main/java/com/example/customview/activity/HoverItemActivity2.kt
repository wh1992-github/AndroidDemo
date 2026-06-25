package com.example.customview.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.example.customview.R
import com.example.customview.adapter.HoverItemAdapter2
import com.example.customview.databinding.ActivityHoverItem2Binding

/**
 * 封装 Hover Item Activity 2 相关逻辑的类。
 */
open class HoverItemActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityHoverItem2Binding
    private lateinit var mAdapter: HoverItemAdapter2
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mDatas: MutableList<String>
    private lateinit var mIncludeView: View
    private var mTypeName: String? = null
    private var mShouldScroll = false
    private var mToPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoverItem2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mIncludeView = binding.include.root
        mRecyclerView = binding.recyclerView
        addData()
        mAdapter = HoverItemAdapter2(mDatas)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val stickyInfoView = recyclerView.findChildViewUnder((recyclerView.measuredWidth shr 1).toFloat(), 1f)
                if (stickyInfoView != null) {
                    ViewBindings.findChildViewById<TextView>(stickyInfoView, R.id.typeName)?.let {
                        mTypeName = it.text.toString()
                    }
                    Log.i(TAG, "onScrolled: mTypeName = $mTypeName")
                    when (mTypeName) {
                        "第一项" -> mIncludeView.visibility = View.GONE
                        "第二项", "第三项" -> mIncludeView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false
                    smoothMoveToPosition(recyclerView, mToPosition)
                }
            }
        })

        mAdapter.setOnItemClickListener(object : HoverItemAdapter2.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (position != -1) {
                    smoothMoveToPosition(mRecyclerView, 20)
                }
            }
        })
    }

    private fun smoothMoveToPosition(recyclerView: RecyclerView, position: Int) {
        val firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0))
        val lastItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.childCount - 1))
        Log.i(TAG, "smoothMoveToPosition: first = $firstItem, position = $position, last = $lastItem")
        when {
            position < firstItem -> recyclerView.smoothScrollToPosition(position)
            position <= lastItem -> {
                val movePosition = position - firstItem
                val top = recyclerView.getChildAt(movePosition).top
                recyclerView.smoothScrollBy(0, top)
            }
            else -> {
                recyclerView.smoothScrollToPosition(position)
                mToPosition = position
                mShouldScroll = true
            }
        }
    }

    private fun addData() {
        mDatas = mutableListOf("0", "1", "2", "3").apply {
            for (i in 1..20) {
                add("这是第${i}条数据")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
