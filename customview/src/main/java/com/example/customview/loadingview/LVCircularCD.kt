package com.example.customview.loadingview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

/**
 * Created by test on 16/6/20.
 */
open class LVCircularCD @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.WHITE
    }
    private var mWidth = 0f
    private var mPadding = 0f

    @JvmField
    var rectF = RectF()

    @JvmField
    var rectF2 = RectF()

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
        mPadding = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        mPaint.strokeWidth = 2f
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint)
        mPaint.strokeWidth = 3f
        canvas.drawCircle(mWidth / 2, mWidth / 2, mPadding, mPaint)

        mPaint.strokeWidth = 2f
        rectF = RectF(mWidth / 2 - mWidth / 3, mWidth / 2 - mWidth / 3, mWidth / 2 + mWidth / 3, mWidth / 2 + mWidth / 3)
        canvas.drawArc(rectF, 0f, 80f, false, mPaint)
        canvas.drawArc(rectF, 180f, 80f, false, mPaint)

        rectF2 = RectF(mWidth / 2 - mWidth / 4, mWidth / 2 - mWidth / 4, mWidth / 2 + mWidth / 4, mWidth / 2 + mWidth / 4)
        canvas.drawArc(rectF2, 0f, 80f, false, mPaint)
        canvas.drawArc(rectF2, 180f, 80f, false, mPaint)
        canvas.restore()
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    fun startAnim() {
        stopAnim()
        mProgressRotateAnim.duration = 1500
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
