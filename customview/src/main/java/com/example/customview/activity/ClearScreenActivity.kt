package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.customview.databinding.ActivityClearScreenBinding
import com.example.customview.widget.ClearScreenMode
import com.example.customview.widget.ClearScreenView
/**
 * 用于展示 Clear Screen 功能的 Activity。
 */

class ClearScreenActivity : AppCompatActivity(), ClearScreenView.OnClearScreenListener {
    private lateinit var binding: ActivityClearScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClearScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clearScreenContainer.addClearView(binding.ivClearContent)

        binding.clearScreenContainer.setOnClearScreenListener(this)

        binding.btnQuickClear.setOnClickListener {
            binding.clearScreenContainer.clearScreenMode = ClearScreenMode.QUICK_SCROLL
        }
        binding.btnSlowClear.setOnClickListener {
            binding.clearScreenContainer.clearScreenMode = ClearScreenMode.SLOW_SCROLL
        }
    }

    override fun onCleared() {
        Toast.makeText(this, "清屏了", Toast.LENGTH_SHORT).show()
    }

    override fun onRestored() {
        Toast.makeText(this, "恢复了", Toast.LENGTH_SHORT).show()
    }
}
