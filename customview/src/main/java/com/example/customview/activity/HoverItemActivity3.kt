package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.adapter.HoverItemAdapter3
import com.example.customview.bean.HoverItemModel
import com.example.customview.databinding.ActivityHoverItem3Binding

/**
 * 封装 Hover Item Activity 3 相关逻辑的类。
 */
open class HoverItemActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityHoverItem3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoverItem3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val mRecyclerView = binding.recyclerview
        val tvStickyHeaderView = binding.headerView
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = HoverItemAdapter3(this, getData())
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.findChildViewUnder((tvStickyHeaderView.measuredWidth shr 1).toFloat(), 1f)
                    ?.let { tvStickyHeaderView.text = it.contentDescription.toString() }

                val transInfoView = recyclerView.findChildViewUnder(
                    (tvStickyHeaderView.measuredWidth shr 1).toFloat(),
                    (tvStickyHeaderView.measuredHeight + 1).toFloat()
                )
                if (transInfoView != null) {
                    val transViewStatus = transInfoView.tag as Int
                    val dealtY = transInfoView.top - tvStickyHeaderView.measuredHeight
                    tvStickyHeaderView.translationY =
                        if (transViewStatus == HoverItemAdapter3.HAS_STICKY_VIEW && transInfoView.top > 0) {
                            dealtY.toFloat()
                        } else {
                            0f
                        }
                }
            }
        })
    }

    fun getData(): List<HoverItemModel> =
        (0 until MODEL_COUNT).map { index ->
            val sticky: String = when {
                index < 6 -> "吸顶文本1"
                index < 12 -> "吸顶文本2"
                index < 18 -> "吸顶文本3"
                else -> "吸顶文本4"
            }
            HoverItemModel(sticky, "name:$index")
        }


    companion object {
        const val MODEL_COUNT = 40
    }
}
