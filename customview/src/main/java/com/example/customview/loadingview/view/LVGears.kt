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

/**
 * Created by test on 16/6/23.
 */
open class LVGears @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private lateinit var mPaint: Paint
    private lateinit var mPaintWheelBig: Paint
    private lateinit var mPaintWheelSmall: Paint
    private lateinit var mPaintAxle: Paint
    private lateinit var mPaintCenter: Paint
    private var mPadding = 0f
    private var mPaintCenterRadius = 0f
    private var mWheelSmallLength = 0f
    private var mWheelBigLength = 0f
    private var mWheelSmallSpace = 8
    private var mWheelBigSpace = 6

    @JvmField
    var mAnimatedValue = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint)
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 4, mPaint)
    }

    private fun drawAxleAndCenter(canvas: Canvas) {
        for (i in 0 until 3) {
            val x2 = ((mWidth / 2f - mPadding) * cos(i * (360 / 3) * Math.PI / 180f)).toFloat()
            val y2 = ((mWidth / 2f - mPadding) * sin(i * (360 / 3) * Math.PI / 180f)).toFloat()
            val x = (mPaintCenterRadius * cos(i * (360 / 3) * Math.PI / 180f)).toFloat()
            val y = (mPaintCenterRadius * sin(i * (360 / 3) * Math.PI / 180f)).toFloat()
            canvas.drawLine(mWidth / 2 - x, mWidth / 2 - y, mWidth / 2 - x2, mWidth / 2 - y2, mPaintAxle)
        }
        canvas.drawCircle(mWidth / 2, mWidth / 2, mPaintCenterRadius, mPaintCenter)
    }

    private fun drawWheelBig(canvas: Canvas) {
        var i = 0
        while (i < 360) {
            val angle = (mAnimatedValue * mWheelBigSpace + i).toInt()
            val x = ((mWidth / 2f - mPadding + mWheelBigLength) * cos(angle * Math.PI / 180f)).toFloat()
            val y = ((mWidth / 2f - mPadding + mWheelBigLength) * sin(angle * Math.PI / 180f)).toFloat()
            val x2 = ((mWidth / 2f - mPadding) * cos(angle * Math.PI / 180f)).toFloat()
            val y2 = ((mWidth / 2f - mPadding) * sin(angle * Math.PI / 180f)).toFloat()
            canvas.drawLine(mWidth / 2f - x, mWidth / 2f - y, mWidth / 2f - x2, mWidth / 2f - y2, mPaintWheelBig)
            i += mWheelBigSpace
        }
    }

    private fun drawWheelSmall(canvas: Canvas) {
        var i = 0
        while (i < 360) {
            val angle = (360 - mAnimatedValue * mWheelBigSpace + i).toInt()
            val x = (mWidth / 4f * cos(angle * Math.PI / 180f)).toFloat()
            val y = (mWidth / 4f * sin(angle * Math.PI / 180f)).toFloat()
            val x2 = ((mWidth / 4f + mWheelSmallLength) * cos(angle * Math.PI / 180f)).toFloat()
            val y2 = ((mWidth / 4f + mWheelSmallLength) * sin(angle * Math.PI / 180f)).toFloat()
            canvas.drawLine(mWidth / 2f - x, mWidth / 2f - y, mWidth / 2f - x2, mWidth / 2f - y2, mPaintWheelSmall)
            i += mWheelSmallSpace
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        mPadding = dip2px(5f).toFloat()
        drawCircle(canvas)
        drawWheelBig(canvas)
        drawWheelSmall(canvas)
        drawAxleAndCenter(canvas)
        canvas.restore()
    }

    private fun initPaint() {
        mPaintCenterRadius = dip2px(2.5f) / 2f
        mPaintCenter = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(0.5f).toFloat()
        }
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(2f).toFloat()
        }
        mPaintAxle = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
            strokeWidth = dip2px(2f).toFloat()
        }
        mPaintWheelBig = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(1f).toFloat()
        }
        mPaintWheelSmall = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(0.5f).toFloat()
        }
        mWheelSmallLength = dip2px(3f).toFloat()
        mWheelBigLength = dip2px(2.5f).toFloat()
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        mPaintCenter.color = color
        mPaintAxle.color = color
        mPaintWheelBig.color = color
        mPaintWheelSmall.color = color
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
