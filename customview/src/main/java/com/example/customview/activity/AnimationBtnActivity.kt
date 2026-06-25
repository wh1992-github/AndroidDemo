package com.example.customview.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityAnimationBtnBinding
import com.example.customview.widget.AnimationButton

/**
 * 用于展示 Animation Btn 功能的 Activity。
 */
open class AnimationBtnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimationBtnBinding
    private lateinit var animationButton: AnimationButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationBtnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animationButton = binding.animationBtn
        animationButton.setAnimationButtonListener(object : AnimationButton.AnimationButtonListener {
            override fun onClickListener() {
                animationButton.start()
            }

            override fun animationFinish() {
                Toast.makeText(this@AnimationBtnActivity, "动画执行完毕", Toast.LENGTH_SHORT).show()
                animationButton.reset()
            }
        })
    }
}
