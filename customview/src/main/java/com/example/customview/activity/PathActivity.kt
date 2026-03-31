package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityPathBinding
/**
 * 用于展示 Path 功能的 Activity。
 */

class PathActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPathBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startBtn.setOnClickListener {
            binding.carAnimView.startAnim()
        }
    }
}
