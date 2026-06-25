package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

/**
 * Created by test on 16/6/20.
 */
open class LVCircularZoom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private var mWidth = 0f
    private var mHigh = 0f
    private var mMaxRadius = 8f
    private var circularCount = 3
    private var mAnimatedValue = 1.0f
    private var mJumpValue = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHigh = measuredHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val circularX = mWidth / circularCount
        for (i in 0 until circularCount) {
            val radius = if (i == mJumpValue % circularCount) mMaxRadius * mAnimatedValue else mMaxRadius
            canvas.drawCircle(i * circularX + circularX / 2f, mHigh / 2, radius, mPaint)
        }
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
        mJumpValue++
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        if (mAnimatedValue < 0.2) {
            mAnimatedValue = 0.2f
        }
        invalidate()
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0f
        mJumpValue = 0
        return 0
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun InitPaint() {
        initPaint()
    }

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
