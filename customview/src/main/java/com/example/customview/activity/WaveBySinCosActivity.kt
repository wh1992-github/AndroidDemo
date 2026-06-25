package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityWaveBySinCosBinding

/**
 * 用于展示 Wave By Sin Cos 功能的 Activity。
 */
open class WaveBySinCosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaveBySinCosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaveBySinCosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
