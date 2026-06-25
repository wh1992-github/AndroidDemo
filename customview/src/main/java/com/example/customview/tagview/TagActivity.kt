package com.example.customview.tagview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.databinding.ActivityTagBinding

/**
 * 用于展示 Tag 功能的 Activity。
 */
open class TagActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTagBinding
    private lateinit var tagRecyclerView: RecyclerView
    private val tagBeanList = mutableListOf<TagBean>()
    private lateinit var tagAdapter: TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initData()
        setRecyclerView()
    }

    private fun initData() {
        tagBeanList += listOf(
            TagBean("1", "准时"),
            TagBean("2", "非常绅士"),
            TagBean("3", "非常有礼貌"),
            TagBean("4", "很会照顾女生"),
            TagBean("5", "我的男神是个大暖男哦"),
            TagBean("6", "谈吐优雅"),
            TagBean("7", "送我到楼下"),
            TagBean("9", "迟到"),
            TagBean("10", "态度恶劣"),
            TagBean("11", "有不礼貌行为"),
            TagBean("12", "有侮辱性语言有暴力倾向"),
            TagBean("13", "人身攻击"),
            TagBean("14", "临时改变行程"),
            TagBean("15", "客户迟到并无理要求延长约会时间")
        )
    }

    private fun setRecyclerView() {
        val layoutManage = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (tagBeanList[position].tag_name!!.length > MAX) 2 else 1
                }
            }
        }
        tagRecyclerView.layoutManager = layoutManage
        tagAdapter = TagAdapter(tagBeanList)
        tagRecyclerView.adapter = tagAdapter
    }

    private fun initView() {
        tagRecyclerView = binding.tagRv
    }

    companion object {
        private const val MAX = 9
    }
}
