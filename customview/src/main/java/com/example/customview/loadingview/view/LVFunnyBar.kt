package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import kotlin.math.sqrt

/**
 * Created by test on 16/7/1.
 */
class LVFunnyBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaintLeftTop: Paint
    private lateinit var mPaintLeftLeft: Paint
    private lateinit var mPaintLeftRight: Paint
    private lateinit var mPaintRightTop: Paint
    private lateinit var mPaintRightLeft: Paint
    private lateinit var mPaintRightRight: Paint
    private var mWidth = 0
    private var mHeight = 0
    private var mAnimatedValue = 1f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        val height = (width / sqrt(3.0)).toFloat()
        setMeasuredDimension(width.toInt(), height.toInt())
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        val wspace = mWidth / 8f
        val hspace = mHeight / 8f
        val p = Path()

        for (i in 0 until 3) {
            var leftLong = mAnimatedValue * (1 + i / 4f)
            if (leftLong > 1f) {
                leftLong = 1f
            }
            var wlong = mWidth / 2 * leftLong - wspace / 2
            var hlong = mHeight / 2 * leftLong - hspace / 2

            if (wlong < wspace / 8 / 8 / 2) {
                wlong = wspace / 8 / 8 / 2
            }
            if (hlong < hspace / 8 / 8 / 2) {
                hlong = wspace / 8 / 8 / 2
            }

            p.reset()
            p.moveTo((i + 0.5f) * wspace, mHeight / 2 + i * hspace)
            p.lineTo((i + 1f) * wspace + wlong, mHeight / 2 - hspace / 2f + i * hspace - hlong)
            p.lineTo((i + 1.5f) * wspace + wlong, mHeight / 2 + i * hspace - hlong)
            p.lineTo((i + 1f) * wspace, mHeight / 2 + hspace / 2f + i * hspace)
            p.close()
            canvas.drawPath(p, mPaintLeftTop)

            p.reset()
            p.moveTo((i + 0.5f) * wspace, mHeight / 2 + i * hspace)
            p.lineTo((i + 1f) * wspace, mHeight / 2 + hspace / 2f + i * hspace)
            p.lineTo((i + 1f) * wspace, mHeight / 2 + hspace / 2f + i * hspace + hspace)
            p.lineTo((i + 0.5f) * wspace, mHeight / 2 + i * hspace + hspace)
            p.close()
            canvas.drawPath(p, mPaintLeftLeft)

            p.reset()
            p.moveTo((i + 1.5f) * wspace + wlong, mHeight / 2 + i * hspace - hlong)
            p.lineTo((i + 1f) * wspace, mHeight / 2 + hspace / 2f + i * hspace)
            p.lineTo((i + 1f) * wspace, mHeight / 2 + hspace / 2f + i * hspace + hspace)
            p.lineTo((i + 1.5f) * wspace + wlong, mHeight / 2 + i * hspace + hspace - hlong)
            p.close()
            canvas.drawPath(p, mPaintLeftRight)

            val rightPosition = i
            p.reset()
            p.moveTo(mWidth - (rightPosition + 1.5f) * wspace - wlong, mHeight / 2 + rightPosition * hspace - hlong)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace - wlong, mHeight / 2 - hspace / 2f + rightPosition * hspace - hlong)
            p.lineTo(mWidth - (rightPosition + 0.5f) * wspace, mHeight / 2 + rightPosition * hspace)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace, mHeight / 2 + hspace / 2f + rightPosition * hspace)
            p.close()
            canvas.drawPath(p, mPaintRightTop)

            p.reset()
            p.moveTo(mWidth - (rightPosition + 1.5f) * wspace - wlong, mHeight / 2 + rightPosition * hspace - hlong)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace, mHeight / 2 + hspace / 2f + rightPosition * hspace)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace, mHeight / 2 + hspace / 2f + rightPosition * hspace + hspace)
            p.lineTo(mWidth - (rightPosition + 1.5f) * wspace - wlong, mHeight / 2 + rightPosition * hspace + hspace - hlong)
            p.close()
            canvas.drawPath(p, mPaintRightLeft)

            p.reset()
            p.moveTo(mWidth - (rightPosition + 0.5f) * wspace, mHeight / 2 + rightPosition * hspace)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace, mHeight / 2 + hspace / 2f + rightPosition * hspace)
            p.lineTo(mWidth - (rightPosition + 1f) * wspace, mHeight / 2 + hspace / 2f + rightPosition * hspace + hspace)
            p.lineTo(mWidth - (rightPosition + 0.5f) * wspace, mHeight / 2 + rightPosition * hspace + hspace)
            p.close()
            canvas.drawPath(p, mPaintRightRight)
        }
        canvas.restore()
    }

    @Suppress("unused")
    private fun drawFire(canvas: Canvas) {
        RectF()
    }

    private fun initPaint() {
        mPaintLeftTop = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(234, 167, 107)
        }
        mPaintLeftLeft = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(174, 113, 94)
        }
        mPaintLeftRight = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(138, 97, 85)
        }
        mPaintRightTop = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(234, 167, 107)
        }
        mPaintRightLeft = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(174, 113, 94)
        }
        mPaintRightRight = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(138, 97, 85)
        }
    }

    fun setViewColor(color: Int) {
        mPaintLeftTop.color = color
        mPaintRightTop.color = color
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff

        mPaintLeftLeft.color = Color.rgb(
            if (red - 60 > 0) red - 60 else 0,
            if (green - 54 > 0) green - 54 else 0,
            if (blue - 13 > 0) blue - 13 else 0
        )
        mPaintRightLeft.color = Color.rgb(
            if (red - 60 > 0) red - 60 else 0,
            if (green - 54 > 0) green - 54 else 0,
            if (blue - 13 > 0) blue - 13 else 0
        )
        mPaintRightRight.color = Color.rgb(
            if (red - 96 > 0) red - 96 else 0,
            if (green - 70 > 0) green - 70 else 0,
            if (blue - 22 > 0) blue - 22 else 0
        )
        mPaintLeftRight.color = Color.rgb(
            if (red - 96 > 0) red - 96 else 0,
            if (green - 70 > 0) green - 70 else 0,
            if (blue - 22 > 0) blue - 22 else 0
        )
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 1f
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.REVERSE

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
