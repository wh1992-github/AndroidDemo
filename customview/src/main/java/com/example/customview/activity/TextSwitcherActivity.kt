package com.example.customview.activity

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customview.R
import com.example.customview.databinding.ActivityTextSwitcherBinding
import com.example.customview.utils.ShaderSpan

/**
 * 用于展示 Text Switcher 功能的 Activity。
 */
open class TextSwitcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextSwitcherBinding
    private lateinit var mTextSwitcher: TextSwitcher
    private var mCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityTextSwitcherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mTextSwitcher = binding.switcher

        mTextSwitcher.setFactory {
            TextView(this).apply {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                textSize = 20f
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    100,
                    Gravity.CENTER
                )
            }
        }

        mTextSwitcher.inAnimation = AnimationUtils.loadAnimation(this, R.anim.text_in)
        mTextSwitcher.outAnimation = AnimationUtils.loadAnimation(this, R.anim.text_out)
        mTextSwitcher.setCurrentText(mList[0])

        binding.button1.setOnClickListener {
            nextText()
            mTextSwitcher.setCurrentText(mList[mCount])
            applyGradientToCurrentText()
        }

        binding.button2.setOnClickListener {
            nextText()
            mTextSwitcher.setText(mList[mCount])
            applyGradientToCurrentText()
        }

        binding.button3.setOnClickListener {
            val layoutParams = mTextSwitcher.layoutParams as ConstraintLayout.LayoutParams
            val originalPosition = if (layoutParams.topMargin <= 200) 400 else layoutParams.topMargin
            Log.i(TAG, "onCreate: $originalPosition")
            ValueAnimator.ofInt(0, 400).setDuration(1000).apply {
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    layoutParams.topMargin = originalPosition - value
                    Log.i(TAG, "onCreate: ${layoutParams.topMargin}")
                    mTextSwitcher.layoutParams = layoutParams
                }
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }

        binding.button4.setOnClickListener {
            listOf(
                "思考中",
                "搜索中 111",
                "搜索中 222",
                "搜索中 333",
                "整理中 111",
                "整理中 222",
                "整理中 333",
                "整理完成"
            ).forEach(binding.textSwitcherView::addText)
        }
    }

    private fun nextText() {
        mCount++
        if (mCount >= mList.size) {
            mCount = 0
        }
    }

    private fun applyGradientToCurrentText() {
        val textView = mTextSwitcher.currentView as? TextView ?: return
        val colors = intArrayOf(Color.RED, Color.BLUE, Color.RED, Color.BLUE, Color.RED)
        val positions = floatArrayOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f)
        val text = mList[mCount]
        val width = textView.paint.measureText(text)
        val startX = (textView.measuredWidth - width) / 2
        val linearGradient = LinearGradient(
            startX,
            0f,
            startX + width,
            0f,
            colors,
            positions,
            Shader.TileMode.CLAMP
        )
        textView.text = SpannableString(text).apply {
            setSpan(ShaderSpan(linearGradient), 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        private const val TAG = "TextSwitcherActivity"
        private val mList = listOf("你好丰田呢", "你有啥事呢你说吧", "你好雷克萨斯你在干嘛", "有啥事就说吧")
    }
}
