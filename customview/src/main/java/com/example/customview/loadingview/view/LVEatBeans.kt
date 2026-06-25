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
open class LVEatBeans @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private lateinit var mPaintEye: Paint
    private var mWidth = 0f
    private var mHigh = 0f
    private var mPadding = 5f
    private var eatErWidth = 60f
    private var eatErPositionX = 0f

    @JvmField
    var eatSpeed = 5

    private var beansWidth = 10f
    private var mAngle = 34f
    private var eatErStartAngle = mAngle
    private var eatErEndAngle = 360 - 2 * eatErStartAngle

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHigh = measuredHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val eatRightX = mPadding + eatErWidth + eatErPositionX
        val rectF = RectF(mPadding + eatErPositionX, mHigh / 2 - eatErWidth / 2, eatRightX, mHigh / 2 + eatErWidth / 2)
        canvas.drawArc(rectF, eatErStartAngle, eatErEndAngle, true, mPaint)
        canvas.drawCircle(mPadding + eatErPositionX + eatErWidth / 2, mHigh / 2 - eatErWidth / 4, beansWidth / 2, mPaintEye)

        val beansCount = ((mWidth - mPadding * 2 - eatErWidth) / beansWidth / 2).toInt()
        for (i in 0 until beansCount) {
            val x = beansCount * i + beansWidth / 2 + mPadding + eatErWidth
            if (x > eatRightX) {
                canvas.drawCircle(x, mHigh / 2, beansWidth / 2, mPaint)
            }
        }
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        mPaintEye = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.BLACK
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    fun setEyeColor(color: Int) {
        mPaintEye.color = color
        postInvalidate()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        val mAnimatedValue = valueAnimator.animatedValue as Float
        eatErPositionX = (mWidth - 2 * mPadding - eatErWidth) * mAnimatedValue
        eatErStartAngle = mAngle * (1 - (mAnimatedValue * eatSpeed - (mAnimatedValue * eatSpeed).toInt()))
        eatErEndAngle = 360 - eatErStartAngle * 2
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        eatErPositionX = 0f
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
