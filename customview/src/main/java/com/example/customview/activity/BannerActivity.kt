package com.example.customview.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R
import com.example.customview.databinding.ActivityBannerBinding
import com.example.customview.widget.banner.BannerView
import com.example.customview.widget.banner.PagerOptions
import com.example.customview.widget.banner.holder.BannerViewHolder

/**
 * 用于展示 Banner 功能的 Activity。
 */
open class BannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBannerBinding
    private lateinit var bannerView: BannerView
    private val datas = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datas += listOf(
            "http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/20180102083655_3t4ytm_Screenshot.jpeg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg"
        )

        bannerView = binding.bannerView

        val options = PagerOptions.Builder(this)
            .setPageMargin(20)
            .setPrePagerWidth(50)
            .setIndicatorDrawable(R.mipmap.banner_point_disabled, R.mipmap.banner_point_enabled)
            // .setIndicatorColor(Color.YELLOW, Color.RED)
            .setOnPageClickListener { _, position ->
                Toast.makeText(this, "Click$position", Toast.LENGTH_SHORT).show()
            }
            .build()

        bannerView
            .setPagerOptions(options)
            .setPages(datas) { MyBanner() }
    }

    class MyBanner : BannerViewHolder<String> {
        override fun createView(context: Context): View {
            return LayoutInflater.from(context).inflate(R.layout.banner_item, null)
        }

        override fun onBind(context: Context, position: Int, data: String) = Unit
    }
}
