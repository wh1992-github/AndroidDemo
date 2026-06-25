package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by test on 2016/12/2.
 */
abstract class LVBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @JvmField
    var valueAnimator: ValueAnimator? = null

    init {
        InitPaint()
    }

    open fun startAnim() {
        stopAnim()
        startViewAnim(0f, 1f, 500)
    }

    open fun startAnim(time: Int) {
        stopAnim()
        startViewAnim(0f, 1f, time.toLong())
    }

    open fun stopAnim() {
        valueAnimator?.let {
            clearAnimation()
            it.repeatCount = 0
            it.cancel()
            it.end()
            if (OnStopAnim() == 0) {
                it.repeatCount = 0
                it.cancel()
                it.end()
            }
        }
    }

    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator {
        valueAnimator = ValueAnimator.ofFloat(startF, endF).apply {
            duration = time
            interpolator = LinearInterpolator()
            repeatCount = SetAnimRepeatCount()
            if (ValueAnimator.RESTART == SetAnimRepeatMode()) {
                repeatMode = ValueAnimator.RESTART
            } else if (ValueAnimator.REVERSE == SetAnimRepeatMode()) {
                repeatMode = ValueAnimator.REVERSE
            }
            addUpdateListener { valueAnimator -> OnAnimationUpdate(valueAnimator) }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                    OnAnimationRepeat(animation)
                }
            })
            if (!isRunning) {
                AnimIsRunning()
                start()
            }
        }
        return valueAnimator!!
    }

    protected abstract fun InitPaint()

    protected abstract fun OnAnimationUpdate(valueAnimator: ValueAnimator)

    protected abstract fun OnAnimationRepeat(animation: Animator)

    protected abstract fun OnStopAnim(): Int

    protected abstract fun SetAnimRepeatMode(): Int

    protected abstract fun SetAnimRepeatCount(): Int

    protected abstract fun AnimIsRunning()

    open fun dip2px(dpValue: Float): Int =
        (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()

    open fun getFontLength(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.width().toFloat()
    }

    open fun getFontHeight(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.height().toFloat()
    }

    open fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }
}
