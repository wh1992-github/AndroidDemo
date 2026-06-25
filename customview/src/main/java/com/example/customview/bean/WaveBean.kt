package com.example.customview.bean

/**
 * Created by test on 2016/12/14.
 * 水波参数的实体
 */
data class WaveBean @JvmOverloads constructor(
    var waveLength: Int = 0,
    var waveAmplitude: Int = 0,
    var waveColor: Int = 0,
    var waveType: Int = 0,
    var duration: Int = 0
)
