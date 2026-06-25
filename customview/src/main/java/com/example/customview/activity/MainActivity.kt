package com.example.customview.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.customview.adapter.MainAdapter
import com.example.customview.bean.TypeBean
import com.example.customview.databinding.ActivityMainBinding
import com.example.customview.kotlin.JiHe
import com.example.customview.tagview.TagActivity
import com.example.customview.widget.SuperDividerItemDecoration

/**
 * 用于展示 Main 功能的 Activity。
 */
open class MainActivity : AppCompatActivity(), BaseQuickAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mList = mutableListOf<TypeBean>()
    private lateinit var mAdapter: MainAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRecyclerView = binding.recyclerView

        mAdapter = MainAdapter(getData()).apply {
            setOnItemClickListener(this@MainActivity)
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(SuperDividerItemDecoration.Builder(this).build())
        mRecyclerView.adapter = mAdapter

        JiHe.test()

    }

    private fun getData(): List<TypeBean> = mList.apply {
        add(TypeBean("气泡漂浮动画", 0))
        add(TypeBean("波浪动画--贝塞尔曲线实现", 1))
        add(TypeBean("波浪动画--正余弦函数实现", 2))
        add(TypeBean("水波（雷达）扩散效果", 3))
        add(TypeBean("RecyclerView实现另类的Tag标签", 4))
        add(TypeBean("按钮自定义动画", 5))
        add(TypeBean("自定义支付密码输入框", 6))
        add(TypeBean("自定义进度条", 7))
        add(TypeBean("使用的带动画的view", 8))
        add(TypeBean("粘性小球", 9))
        add(TypeBean("banner", 10))
        add(TypeBean("吸顶效果--一行代码实现", 11))
        add(TypeBean("吸顶效果--动态实现", 12))
        add(TypeBean("吸顶效果--滑动切换", 13))
        add(TypeBean("揭露动画", 14))
        add(TypeBean("支付宝首页效果", 15))
        add(TypeBean("RecyclerView的item动画", 16))
        add(TypeBean("路径path动画", 17))
        add(TypeBean("仿新浪投票控件", 18))
        add(TypeBean("直播侧滑清屏效果", 19))
        add(TypeBean("指纹验证", 20))
        add(TypeBean("仿写QQ界面", 21))
        add(TypeBean("LoadingView", 22))
        add(TypeBean("LoadingViewMac", 23))
        add(TypeBean("FlowLayout", 24))
        add(TypeBean("TextSwitcher", 25))
        add(TypeBean("WaveView", 26))
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val target = when (mList[position].type) {
            0 -> BubbleViewActivity::class.java
            1 -> WaveByBezierActivity::class.java
            2 -> WaveBySinCosActivity::class.java
            3 -> RadarActivity::class.java
            4 -> TagActivity::class.java
            5 -> AnimationBtnActivity::class.java
            6 -> PayPsdViewActivity::class.java
            7 -> ProgressBarActivity::class.java
            8 -> AnimationViewActivity::class.java
            9 -> DragBallActivity::class.java
            10 -> BannerActivity::class.java
            11 -> HoverItemActivity::class.java
            12 -> HoverItemActivity2::class.java
            13 -> HoverItemActivity3::class.java
            14 -> RevealAnimationActivity::class.java
            15 -> AliPayHomeActivity::class.java
            16 -> RecyclerViewItemAnimActivity::class.java
            17 -> PathActivity::class.java
            18 -> SinaVoteActivity::class.java
            19 -> ClearScreenActivity::class.java
            20 -> FingerprintActivity::class.java
            21 -> RecyclerQQActivity::class.java
            22 -> LoadingViewActivity::class.java
            23 -> LoadingViewMacActivity::class.java
            24 -> FlowActivity::class.java
            25 -> TextSwitcherActivity::class.java
            26 -> WaveViewActivity::class.java
            else -> null
        }
        target?.let { startActivity(Intent(this, it)) }
    }
}
