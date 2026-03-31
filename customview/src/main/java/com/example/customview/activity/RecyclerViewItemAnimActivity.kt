package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.adapter.ItemAnimAdapter
import com.example.customview.anim.RotateXItemAnimation
import com.example.customview.anim.RotateYItemAnimation
import com.example.customview.anim.ScaleItemAnimation
import com.example.customview.anim.SlideItemAnimation
import com.example.customview.databinding.ActivityRecyclerViewItemAnimBinding
import kotlin.random.Random
/**
 * 用于展示 Recycler View Item Anim 功能的 Activity。
 */

class RecyclerViewItemAnimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewItemAnimBinding

    var adapter: ItemAnimAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewItemAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
        initView()
        initData()
    }

    private fun initView() {
        adapter = ItemAnimAdapter(arrayListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun initListener() {
        binding.normalBtn.setOnClickListener { setReverseLayout(false) }
        binding.reverseBtn.setOnClickListener { setReverseLayout(true) }
        binding.scaleBtn.setOnClickListener { setItemAnimation(ScaleItemAnimation()) }
        binding.slideBtn.setOnClickListener { setItemAnimation(SlideItemAnimation()) }
        binding.rotateXBtn.setOnClickListener { setItemAnimation(RotateXItemAnimation()) }
        binding.rotateYBtn.setOnClickListener { setItemAnimation(RotateYItemAnimation()) }

        binding.addBtn.setOnClickListener {
            adapter?.addData(0, getItemData())
            (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }

        binding.removeBtn.setOnClickListener {
            if (adapter?.data?.size ?: 0 > 0) {
                adapter?.remove(0)
                (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    0,
                    0
                )
            }
        }
    }

    private fun setReverseLayout(reverseLayout: Boolean) {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, reverseLayout)
        adapter?.data?.clear()
        adapter?.notifyDataSetChanged()
    }

    private fun setItemAnimation(itemAnimation: RecyclerView.ItemAnimator) {
        adapter?.data?.clear()
        adapter?.notifyDataSetChanged()
        binding.recyclerView.itemAnimator = itemAnimation
    }

    private fun initData(): ArrayList<String> {
        val list: ArrayList<String> = arrayListOf()
        list.add("人生如戏，全靠演技")
        list.add("年轻就是资本")
        list.add("我的一颗眼泪掉进了海洋，当我找到它的那一天就是我停止爱你的那一天")
        list.add("你若一直在，我便一直爱")
        list.add("路，跪着也要走完")
        list.add("美丽的彩虹就像一座七彩的桥一样高挂在雨后的天空")
        list.add("留情不留命，留命伤感情")
        list.add("宽容就是在别人和自己意见不一致时也不要勉强")
        list.add("那些曾经以为念念不忘的事情，就在我们念念不忘的过程里，被我们遗忘了")
        list.add("朝花夕拾捡的是枯萎")
        list.add("黄绢幼妇，其土老人")
        list.add("要有最朴素的生活，与最遥远的梦想，即使明日天寒地冻，路远马亡")
        list.add("不是路不平，而是你不行")
        return list
    }

    private fun getItemData(): String {
        return initData()[Random.nextInt(100) / 10 + 1]
    }
}
