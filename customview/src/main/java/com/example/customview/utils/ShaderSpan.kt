package com.example.customview.utils

import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle

/**
 * 封装 Shader Span 相关逻辑的类。
 */
open class ShaderSpan(private val shader: Shader?) : CharacterStyle() {
    override fun updateDrawState(tp: TextPaint) {
        tp.shader = shader
    }
}
