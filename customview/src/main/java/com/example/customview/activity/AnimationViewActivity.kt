package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityAnimationViewBinding
import com.example.customview.widget.FadeInTextView
import com.example.customview.widget.LoadingButton

/**
 * 用于展示 Animation View 功能的 Activity。
 */
open class AnimationViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimationViewBinding
    private lateinit var fadeInTextView: FadeInTextView
    private lateinit var loadingButton: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fadeInTextView = binding.fadeInTv
        loadingButton = binding.loadingBtn

        fadeInTextView
            .setTextString("自定义view实现字符串逐字显示，后边的文字是为了测试换行是否正常显示！")
            .setTextAnimationListener(object : FadeInTextView.TextAnimationListener {
                override fun animationFinish() {
                    loadingButton.stopLoading()
                }
            })

        loadingButton.setOnClickListener {
            loadingButton.startLoading()
            fadeInTextView.startFadeInAnimation()
        }
    }
}
