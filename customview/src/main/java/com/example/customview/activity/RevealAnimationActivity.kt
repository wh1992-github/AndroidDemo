package com.example.customview.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityRevealAnimationBinding
import kotlin.math.hypot

/**
 * 用于展示 Reveal Animation 功能的 Activity。
 */
open class RevealAnimationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRevealAnimationBinding
    private lateinit var imageView: ImageView
    private lateinit var startBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevealAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imgIv
        startBtn = binding.startBtn

        val centerX = 0
        val centerY = 0
        startBtn.setOnClickListener {
            val radius = hypot(imageView.width.toFloat(), imageView.height.toFloat())
            if (imageView.visibility == View.VISIBLE) {
                ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, radius, 0f).apply {
                    duration = 3000
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            imageView.visibility = View.GONE
                        }
                    })
                    start()
                }
            } else {
                ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, 0f, radius).apply {
                    duration = 3000
                    imageView.visibility = View.VISIBLE
                    start()
                }
            }
        }
    }
}
