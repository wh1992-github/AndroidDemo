package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import kotlin.math.max

/**
 * Created by test on 16/6/20.
 */
open class LVPlayBall @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private lateinit var mPaintCircle: Paint
    private lateinit var mPaintBall: Paint
    private var mPaintStrokeWidth = 0f
    private var mHigh = 0f
    private var mWidth = 0f
    private var quadToStart = 0f
    private var mRadius = 0f
    private var mRadiusBall = 0f
    private var ballY = 0f
    private val mPath = Path()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHigh = measuredHeight.toFloat()
        mWidth = measuredWidth.toFloat()
        quadToStart = mHigh / 2
        mRadius = dip2px(3f).toFloat()
        mPaintStrokeWidth = 2f
        ballY = mHigh / 2
        mRadiusBall = dip2px(4f).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.moveTo(0 + mRadius * 2 + mPaintStrokeWidth, measuredHeight / 2f)
        mPath.quadTo(mWidth / 2, quadToStart, mWidth - mRadius * 2 - mPaintStrokeWidth, mHigh / 2)
        mPaint.strokeWidth = 2f
        canvas.drawPath(mPath, mPaint)

        mPaintCircle.strokeWidth = mPaintStrokeWidth
        canvas.drawCircle(mRadius + mPaintStrokeWidth, mHigh / 2, mRadius, mPaintCircle)
        canvas.drawCircle(mWidth - mRadius - mPaintStrokeWidth, mHigh / 2, mRadius, mPaintCircle)
        canvas.drawCircle(mWidth / 2, max(ballY - mRadiusBall, mRadiusBall), mRadiusBall, mPaintBall)
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
        mPaintCircle = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
        mPaintBall = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        mPaintCircle.color = color
        postInvalidate()
    }

    fun setBallColor(color: Int) {
        mPaintBall.color = color
        postInvalidate()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        val value = valueAnimator.animatedValue as Float
        quadToStart = if (value > 0.75) {
            mHigh / 2 - (1f - valueAnimator.animatedValue as Float) * mHigh / 3f
        } else {
            mHigh / 2 + (1f - valueAnimator.animatedValue as Float) * mHigh / 3f
        }

        ballY = if (value > 0.35f) {
            mHigh / 2 - mHigh / 2 * value
        } else {
            mHigh / 2 + mHigh / 6 * value
        }
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        quadToStart = mHigh / 2
        ballY = mHigh / 2
        return 0
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.REVERSE

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
