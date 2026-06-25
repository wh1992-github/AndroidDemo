package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Created by test on 16/6/20.
 */
open class LVCircularRing @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private lateinit var mPaintPro: Paint
    private var mWidth = 0f
    private var mPadding = 0f
    private var startAngle = 0f

    @JvmField
    var rectF = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mPadding = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaintPro)
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        canvas.drawArc(rectF, startAngle, 100f, false, mPaint)
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = 8f
        }
        mPaintPro = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.argb(100, 255, 255, 255)
            strokeWidth = 8f
        }
    }

    fun setViewColor(color: Int) {
        mPaintPro.color = color
        postInvalidate()
    }

    fun setBarColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        val value = valueAnimator.animatedValue as Float
        startAngle = 360 * value
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int = 0

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
