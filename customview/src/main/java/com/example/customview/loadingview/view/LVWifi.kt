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
 * Created by test on 16/6/27.
 */
open class LVWifi @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mPadding = 0f
    private lateinit var mPaint: Paint
    private var signalSize = 4
    private var mAnimatedValue = 0.9f

    init {
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(0f, mWidth / signalSize)
        mPaint.strokeWidth = mWidth / signalSize / 2 / 2 / 2
        val scale = ((mAnimatedValue * signalSize - (mAnimatedValue * signalSize).toInt()) * signalSize).toInt() + 1
        val signalRadius = mWidth / 2 / signalSize
        for (i in 0 until signalSize) {
            if (i >= signalSize - scale) {
                val radius = signalRadius * i
                val rect = RectF(radius, radius, mWidth - radius, mWidth - radius)
                if (i < signalSize - 1) {
                    mPaint.style = Paint.Style.STROKE
                    canvas.drawArc(rect, -135f, 90f, false, mPaint)
                } else {
                    mPaint.style = Paint.Style.FILL
                    canvas.drawArc(rect, -135f, 90f, true, mPaint)
                }
            }
        }
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mPadding = dip2px(1f).toFloat()
    }

    override fun InitPaint() {
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0.9f
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
