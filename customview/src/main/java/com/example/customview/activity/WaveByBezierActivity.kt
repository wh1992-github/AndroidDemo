package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityWaveByBezierBinding
import com.example.customview.widget.WaveViewByBezier

/**
 * 用于展示 Wave By Bezier 功能的 Activity。
 */
open class WaveByBezierActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaveByBezierBinding
    private lateinit var waveViewByBezier: WaveViewByBezier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaveByBezierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        waveViewByBezier = binding.waveBezier
        waveViewByBezier.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        waveViewByBezier.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        waveViewByBezier.resumeAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        waveViewByBezier.stopAnimation()
    }
}
