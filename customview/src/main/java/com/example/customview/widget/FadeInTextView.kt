package com.example.customview.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.TextView

/**
 * Created by test on 2017/6/5.
 *
 * 字符串逐字显示的view
 */
@SuppressLint("AppCompatCustomView")
open class FadeInTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextView(context, attrs) {

    private val textRect = Rect()
    private val stringBuffer = StringBuffer()
    private lateinit var arrayContent: Array<String>
    private var textCount = 0
    private var currentIndex = -1
    private var duration = 300
    private var textAnimation: ValueAnimator? = null
    private var textAnimationListener: TextAnimationListener? = null

    fun setTextAnimationListener(textAnimationListener: TextAnimationListener?): FadeInTextView {
        this.textAnimationListener = textAnimationListener
        return this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun drawText(canvas: Canvas, textString: String) {
        textRect.left = paddingLeft
        textRect.top = paddingTop
        textRect.right = width - paddingRight
        textRect.bottom = height - paddingBottom
        val fontMetrics: Paint.FontMetricsInt = paint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(textString, paddingLeft.toFloat(), baseline.toFloat(), paint)
    }

    private fun initAnimation() {
        textAnimation = ValueAnimator.ofInt(0, textCount - 1).apply {
            duration = (textCount * this@FadeInTextView.duration).toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val index = valueAnimator.animatedValue as Int
                if (currentIndex != index) {
                    stringBuffer.append(arrayContent[index])
                    currentIndex = index
                    if (currentIndex == textCount - 1) {
                        textAnimationListener?.animationFinish()
                    }
                    text = stringBuffer.toString()
                }
            }
        }
    }

    fun setTextString(textString: String?): FadeInTextView {
        if (textString != null) {
            textCount = textString.length
            arrayContent = Array(textCount) { i -> textString.substring(i, i + 1) }
            initAnimation()
        }
        return this
    }

    fun startFadeInAnimation(): FadeInTextView {
        textAnimation?.let {
            stringBuffer.setLength(0)
            currentIndex = -1
            it.start()
        }
        return this
    }

    fun stopFadeInAnimation(): FadeInTextView {
        textAnimation?.end()
        return this
    }

    fun interface TextAnimationListener {
        fun animationFinish()
    }
}
