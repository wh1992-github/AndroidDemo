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
open class LVCircularJump @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private var mWidth = 0f
    private var mHigh = 0f
    private var mMaxRadius = 6f
    private var circularCount = 4
    private var mAnimatedValue = 0f
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
            val y = if (i == mJumpValue % circularCount) {
                mHigh / 2 - mHigh / 2 * mAnimatedValue
            } else {
                mHigh / 2
            }
            canvas.drawCircle(i * circularX + circularX / 2f, y, mMaxRadius, mPaint)
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

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        if (mAnimatedValue > 0.5) {
            mAnimatedValue = 1 - mAnimatedValue
        }
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
        mJumpValue++
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0f
        mJumpValue = 0
        return 0
    }

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
