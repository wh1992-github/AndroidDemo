package com.example.customview.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityAlipayHomeBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * 用于展示 Ali Pay Home 功能的 Activity。
 */
class AliPayHomeActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var binding: ActivityAlipayHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlipayHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val offset = abs(verticalOffset)
        val scrollRange = appBarLayout.totalScrollRange
        if (offset <= scrollRange / 2) {
            binding.includeToolbarOpen.root.visibility = View.VISIBLE
            binding.includeToolbarClose.root.visibility = View.GONE
            val scale2 = offset.toFloat() / (scrollRange / 2)
            val alpha2 = (255 * scale2).toInt()
            binding.includeToolbarOpen.toolbarOpenBgView.setBackgroundColor(Color.argb(alpha2, 25, 131, 209))
        } else {
            binding.includeToolbarOpen.root.visibility = View.GONE
            binding.includeToolbarClose.root.visibility = View.VISIBLE
            val scale3 = (scrollRange - offset).toFloat() / (scrollRange / 2)
            val alpha3 = (255 * scale3).toInt()
            binding.includeToolbarClose.bgToolbarClose.setBackgroundColor(Color.argb(alpha3, 25, 131, 209))
        }
        val scale = offset.toFloat() / scrollRange
        val alpha = (255 * scale).toInt()
        binding.includeDefaultLayout.contentBgView.setBackgroundColor(Color.argb(alpha, 25, 131, 209))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.appBarLayout.removeOnOffsetChangedListener(this)
    }
}
