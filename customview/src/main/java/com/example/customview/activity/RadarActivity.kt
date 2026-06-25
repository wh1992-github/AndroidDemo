package com.example.customview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityRadarBinding
import com.example.customview.widget.RadarWaveView

/**
 * 用于展示 Radar 功能的 Activity。
 */
open class RadarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadarBinding
    private lateinit var radarView: RadarWaveView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        radarView = binding.radarView
    }

    override fun onResume() {
        super.onResume()
        radarView.start()
    }

    override fun onPause() {
        super.onPause()
        radarView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        radarView.stop()
    }
}
