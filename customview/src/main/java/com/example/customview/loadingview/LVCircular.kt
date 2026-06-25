package com.example.customview.loadingview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by test on 16/6/20.
 */
open class LVCircular @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaintCenter = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val mPaintRound = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private var mWidth = 0f
    private var mStartAngle = 0
    private var mMaxRadius = 4f
    private val mProgressRotateAnim = RotateAnimation(
        0f,
        360f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).apply {
        repeatCount = -1
        interpolator = LinearInterpolator()
        fillAfter = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mMaxRadius = mWidth / 30f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until 9) {
            val x2 = ((mWidth / 2f - mMaxRadius) * cos(mStartAngle + 45f * i * Math.PI / 180f)).toFloat()
            val y2 = ((mWidth / 2f - mMaxRadius) * sin(mStartAngle + 45f * i * Math.PI / 180f)).toFloat()
            canvas.drawCircle(mWidth / 2f - x2, mWidth / 2f - y2, mMaxRadius, mPaintRound)
        }
        canvas.drawCircle(mWidth / 2f, mWidth / 2f, mWidth / 2f - mMaxRadius * 6, mPaintCenter)
    }

    fun setViewColor(color: Int) {
        mPaintCenter.color = color
        postInvalidate()
    }

    fun setRoundColor(color: Int) {
        mPaintRound.color = color
        postInvalidate()
    }

    fun startAnim() {
        stopAnim()
        mProgressRotateAnim.duration = 3500
        startAnimation(mProgressRotateAnim)
    }

    fun startAnim(time: Int) {
        stopAnim()
        mProgressRotateAnim.duration = time.toLong()
        startAnimation(mProgressRotateAnim)
    }

    fun stopAnim() {
        clearAnimation()
    }
}
