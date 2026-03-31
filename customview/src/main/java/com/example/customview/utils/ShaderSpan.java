package com.example.customview.utils;

import android.graphics.Shader;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
/**
 * 封装 Shader Span 相关逻辑的类。
 */

public class ShaderSpan extends CharacterStyle {

    private Shader shader;

    public ShaderSpan(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setShader(shader);
    }
}