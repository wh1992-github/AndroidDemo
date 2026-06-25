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
open class LVCircularSmile @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private var mWidth = 0f
    private var mEyeWidth = 0f
    private var mPadding = 0f
    private var startAngle = 0f
    private var isSmile = false

    @JvmField
    var rectF = RectF()

    @JvmField
    var mAnimatedValue = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mPadding = dip2px(10f).toFloat()
        mEyeWidth = dip2px(3f).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        mPaint.style = Paint.Style.STROKE
        canvas.drawArc(rectF, startAngle, 180f, false, mPaint)

        mPaint.style = Paint.Style.FILL
        if (isSmile) {
            canvas.drawCircle(mPadding + mEyeWidth + mEyeWidth / 2, mWidth / 3, mEyeWidth, mPaint)
            canvas.drawCircle(mWidth - mPadding - mEyeWidth - mEyeWidth / 2, mWidth / 3, mEyeWidth, mPaint)
        }
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(2f).toFloat()
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        if (mAnimatedValue < 0.5) {
            isSmile = false
            startAngle = 720 * mAnimatedValue
        } else {
            startAngle = 720f
            isSmile = true
        }
        invalidate()
    }

    override fun OnStopAnim(): Int {
        isSmile = false
        mAnimatedValue = 0f
        startAngle = 0f
        return 0
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
