package com.example.customview.activity

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.bean.CircleBean
import com.example.customview.databinding.ActivityBubbleViewBinding
import com.example.customview.utils.DisplayUtils
import com.example.customview.widget.BubbleView

/**
 * 用于展示 Bubble View 功能的 Activity。
 */
open class BubbleViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBubbleViewBinding
    private lateinit var hxbIv: ImageView
    private lateinit var hxbTv: TextView
    private lateinit var button: Button
    private lateinit var bezierView: BubbleView
    private val circleBeanList = mutableListOf<CircleBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBubbleViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hxbIv = binding.hxbIv
        hxbTv = binding.centerTv
        bezierView = binding.circleView
        button = binding.startBtn
        button.setOnClickListener {
            bezierView.setCenterImg(hxbTv)
            bezierView.openAnimation()
        }

        initPoint()
        bezierView.setCircleBeen(circleBeanList)
    }

    private fun initPoint() {
        val height = DisplayUtils.getDisplayHeight(this)
        val width = DisplayUtils.getDisplayWidth(this)
        val centerX = width / 2
        val centerY = height / 2
        Log.i(TAG, "initPoint: $centerX----$centerY")

        circleBeanList += CircleBean(
            PointF((-width / 5.1).toFloat(), (height / 1.5).toFloat()),
            PointF((centerX - 30).toFloat(), (height * 2 / 3).toFloat()),
            PointF((width / 2.4).toFloat(), (height / 3.4).toFloat()),
            PointF((width / 6).toFloat(), (centerY - 120).toFloat()),
            PointF((width / 7.2).toFloat(), (-height / 128).toFloat()),
            (width / 14.4).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((-width / 4).toFloat(), (height / 1.3).toFloat()),
            PointF((centerX - 20).toFloat(), (height * 3 / 5).toFloat()),
            PointF((width / 2.1).toFloat(), (height / 2.5).toFloat()),
            PointF((width / 3).toFloat(), (centerY - 10).toFloat()),
            PointF((width / 4).toFloat(), (-height / 5.3).toFloat()),
            (width / 4).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((-width / 12).toFloat(), (height / 1.1).toFloat()),
            PointF((centerX - 100).toFloat(), (height * 2 / 3).toFloat()),
            PointF((width / 3.4).toFloat(), (height / 2).toFloat()),
            PointF(0f, (centerY + 100).toFloat()),
            PointF(0f, 0f),
            (width / 24).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((-width / 9).toFloat(), (height / 0.9).toFloat()),
            PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
            PointF((width / 2.1).toFloat(), (height / 2.3).toFloat()),
            PointF((width / 2).toFloat(), centerY.toFloat()),
            PointF((width / 1.5).toFloat(), (-height / 5.6).toFloat()),
            (width / 4).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((width / 1.4).toFloat(), (height / 0.9).toFloat()),
            PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
            PointF((width / 2).toFloat(), (height / 2.37).toFloat()),
            PointF((width * 10 / 13).toFloat(), (centerY - 20).toFloat()),
            PointF((width / 2).toFloat(), (-height / 7.1).toFloat()),
            (width / 4).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((width / 0.8).toFloat(), height.toFloat()),
            PointF((centerX + 20).toFloat(), (height * 2 / 3).toFloat()),
            PointF((width / 1.9).toFloat(), (height / 2.3).toFloat()),
            PointF((width * 11 / 14).toFloat(), (centerY + 10).toFloat()),
            PointF((width / 1.1).toFloat(), (-height / 6.4).toFloat()),
            (width / 4).toFloat(),
            60
        )
        circleBeanList += CircleBean(
            PointF((width / 0.9).toFloat(), (height / 1.2).toFloat()),
            PointF((centerX + 20).toFloat(), (height * 4 / 7).toFloat()),
            PointF((width / 1.6).toFloat(), (height / 1.9).toFloat()),
            PointF(width.toFloat(), (centerY + 10).toFloat()),
            PointF(width.toFloat(), 0f),
            (width / 9.6).toFloat(),
            60
        )
    }

    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    companion object {
        private const val TAG = "BubbleViewActivity"
    }
}
