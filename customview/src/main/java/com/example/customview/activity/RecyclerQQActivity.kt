package com.example.customview.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import com.example.customview.adapter.RecyclerQQAdapter
import com.example.customview.databinding.ActivityRecyclerQqBinding

/**
 * 用于展示 Recycler QQ 功能的 Activity。
 */
open class RecyclerQQActivity : AppCompatActivity(), RecyclerQQAdapter.onSlidingViewClickListener {

    private lateinit var binding: ActivityRecyclerQqBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerQQAdapter
    private lateinit var dataImage: MutableList<Bitmap>
    private lateinit var dataTitle: MutableList<String>
    private lateinit var dataContent: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerQqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRecyclerView = binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@RecyclerQQActivity)
        }
        initData()
        mAdapter = RecyclerQQAdapter(this, dataImage, dataTitle, dataContent)
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnSlidListener(this)
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "点击了：$position", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteBtnClick(view: View, position: Int) {
        mAdapter.removeData(position)
    }

    fun initData() {
        dataImage = mutableListOf(
            R.mipmap.a1,
            R.mipmap.a2,
            R.mipmap.a3,
            R.mipmap.a4,
            R.mipmap.a5,
            R.mipmap.a6,
            R.mipmap.a7,
            R.mipmap.a8,
            R.mipmap.a9,
            R.mipmap.a10,
            R.mipmap.a11,
            R.mipmap.a12
        ).mapTo(mutableListOf()) { BitmapFactory.decodeResource(resources, it) }

        dataTitle = mutableListOf(
            "Android开发交流群",
            "R语言初级入门学习",
            "刘亦菲",
            "策划书交流群",
            "15生态宜居学院学生群",
            "湘环资助 （助学贷款）",
            "湘环编程研讨会",
            "丰风",
            "阿娇",
            "图书馆流通服务交流群",
            "one3胡了",
            "读者协会策划部"
        )
        dataContent = mutableListOf(
            "广州_Even：[图片]",
            "轻舟飘飘：auto基本不准",
            "不会的",
            "残留的余温。：分享[熊猫直播]",
            "刘老师：[文件]2018年6月全国大学……",
            "17级园林",
            "黄晓明：baby一般般吧",
            "[文件]编程之美",
            "i5的处理器，比较稳定，蛮好的",
            "寥寥：好的，谢谢老师",
            "易天：阿龙还在面试呢",
            "策划部陈若依：请大家把备注改好"
        )
    }
}
