package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Created by test on 16/6/23.
 */
open class LVGearsTwo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private lateinit var mPaint: Paint
    private lateinit var mPaintAxle: Paint
    private lateinit var mPaintRing: Paint
    private var mPadding = 0f
    private var mWheelLength = 0f
    private var mWheelSmallSpace = 10
    private var mWheelBigSpace = 8

    @JvmField
    var mAnimatedValue = 0f

    @JvmField
    var hypotenuse = 0f

    @JvmField
    var smallRingCenterX = 0f

    @JvmField
    var smallRingCenterY = 0f

    @JvmField
    var bigRingCenterX = 0f

    @JvmField
    var bigRingCenterY = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
    }

    private fun drawSmallRing(canvas: Canvas) {
        hypotenuse = (mWidth * sqrt(2.0)).toFloat()
        smallRingCenterX = (hypotenuse / 6f * cos(45 * Math.PI / 180f)).toFloat()
        smallRingCenterY = (hypotenuse / 6f * sin(45 * Math.PI / 180f)).toFloat()
        mPaintRing.strokeWidth = dip2px(1.0f).toFloat()
        canvas.drawCircle(mPadding + smallRingCenterX, smallRingCenterY + mPadding, smallRingCenterX, mPaintRing)
        mPaintRing.strokeWidth = dip2px(1.5f).toFloat()
        canvas.drawCircle(mPadding + smallRingCenterX, smallRingCenterY + mPadding, smallRingCenterX / 2, mPaintRing)
    }

    private fun drawSmallGear(canvas: Canvas) {
        mPaint.strokeWidth = dip2px(1f).toFloat()
        var i = 0
        while (i < 360) {
            val angle = (mAnimatedValue * mWheelSmallSpace + i).toInt()
            val x3 = (smallRingCenterX * cos(angle * Math.PI / 180f)).toFloat()
            val y3 = (smallRingCenterY * sin(angle * Math.PI / 180f)).toFloat()
            val x4 = ((smallRingCenterX + mWheelLength) * cos(angle * Math.PI / 180f)).toFloat()
            val y4 = ((smallRingCenterY + mWheelLength) * sin(angle * Math.PI / 180f)).toFloat()
            canvas.drawLine(
                mPadding + smallRingCenterX - x4,
                smallRingCenterY + mPadding - y4,
                smallRingCenterX + mPadding - x3,
                smallRingCenterY + mPadding - y3,
                mPaint
            )
            i += mWheelSmallSpace
        }
    }

    private fun drawBigGear(canvas: Canvas) {
        bigRingCenterX = (hypotenuse / 2f * cos(45 * Math.PI / 180f)).toFloat()
        bigRingCenterY = (hypotenuse / 2f * sin(45 * Math.PI / 180f)).toFloat()
        val strokeWidth = dip2px(1.5f) / 4f
        mPaint.strokeWidth = dip2px(1.5f).toFloat()
        var i = 0
        while (i < 360) {
            val angle = (360 - (mAnimatedValue * mWheelBigSpace + i)).toInt()
            val x3 = ((bigRingCenterX - smallRingCenterX) * cos(angle * Math.PI / 180f)).toFloat()
            val y3 = ((bigRingCenterY - smallRingCenterY) * sin(angle * Math.PI / 180f)).toFloat()
            val x4 = ((bigRingCenterX - smallRingCenterX + mWheelLength) * cos(angle * Math.PI / 180f)).toFloat()
            val y4 = ((bigRingCenterY - smallRingCenterY + mWheelLength) * sin(angle * Math.PI / 180f)).toFloat()
            canvas.drawLine(
                bigRingCenterX + mPadding - x4 + mWheelLength * 2 + strokeWidth,
                bigRingCenterY + mPadding - y4 + mWheelLength * 2 + strokeWidth,
                bigRingCenterX + mPadding - x3 + mWheelLength * 2 + strokeWidth,
                bigRingCenterY + mPadding - y3 + mWheelLength * 2 + strokeWidth,
                mPaint
            )
            i += mWheelBigSpace
        }
    }

    private fun drawBigRing(canvas: Canvas) {
        val strokeWidth = dip2px(1.5f) / 4f
        mPaintRing.strokeWidth = dip2px(1.5f).toFloat()
        canvas.drawCircle(
            bigRingCenterX + mPadding + mWheelLength * 2 + strokeWidth,
            bigRingCenterY + mPadding + mWheelLength * 2 + strokeWidth,
            bigRingCenterX - smallRingCenterX - strokeWidth,
            mPaintRing
        )
        mPaintRing.strokeWidth = dip2px(1.5f).toFloat()
        canvas.drawCircle(
            bigRingCenterX + mPadding + mWheelLength * 2 + strokeWidth,
            bigRingCenterY + mPadding + mWheelLength * 2 + strokeWidth,
            (bigRingCenterX - smallRingCenterX) / 2 - strokeWidth,
            mPaintRing
        )
    }

    private fun drawAxle(canvas: Canvas) {
        for (i in 0 until 3) {
            val x3 = (smallRingCenterX * cos(i * (360 / 3) * Math.PI / 180f)).toFloat()
            val y3 = (smallRingCenterY * sin(i * (360 / 3) * Math.PI / 180f)).toFloat()
            canvas.drawLine(mPadding + smallRingCenterX, mPadding + smallRingCenterY, mPadding + smallRingCenterX - x3, mPadding + smallRingCenterY - y3, mPaintAxle)
        }

        for (i in 0 until 3) {
            val x3 = ((bigRingCenterX - smallRingCenterX) * cos(i * (360 / 3) * Math.PI / 180f)).toFloat()
            val y3 = ((bigRingCenterY - smallRingCenterY) * sin(i * (360 / 3) * Math.PI / 180f)).toFloat()
            canvas.drawLine(
                bigRingCenterX + mPadding + mWheelLength * 2,
                bigRingCenterY + mPadding + mWheelLength * 2,
                bigRingCenterX + mPadding + mWheelLength * 2 - x3,
                bigRingCenterY + mPadding + mWheelLength * 2 - y3,
                mPaintAxle
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPadding = dip2px(5f).toFloat()
        canvas.save()
        canvas.rotate(180f, mWidth / 2, mWidth / 2)
        drawSmallRing(canvas)
        drawSmallGear(canvas)
        drawBigGear(canvas)
        drawBigRing(canvas)
        drawAxle(canvas)
        canvas.restore()
    }

    private fun initPaint() {
        mPaintRing = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(1.5f).toFloat()
        }
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(1f).toFloat()
        }
        mPaintAxle = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
            strokeWidth = dip2px(1.5f).toFloat()
        }
        mWheelLength = dip2px(2f).toFloat()
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        mPaintAxle.color = color
        mPaintRing.color = color
        postInvalidate()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        postInvalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
