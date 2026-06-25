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
 * Created by test on 16/6/28.
 */
class LVNews @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mPadding = 0f
    private lateinit var mPaint: Paint
    private var cornerRadius = 0f
    private val rectFTopRight = RectF()
    private val rectFBottomRight = RectF()
    private val rectFBottomLeft = RectF()
    private val rectFTopLeft = RectF()
    private val rectFSquare = RectF()
    private val rectFSquareBG = RectF()
    private var marggingSquareH = 0f
    private var marggingSquareV = 0f
    private var marggingLineH = 0f
    private var marggingLineV = 0f
    private var mValue = 100
    private var mStep = 1
    private var mAnimatedValue = 0f

    fun setValue(value: Int) {
        stopAnim()
        if (value <= 100) {
            mValue = value
            postInvalidate()
            if (mValue == 100) {
                startAnim()
            }
        } else {
            mValue = 100
        }
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
        cornerRadius = dip2px(3.0f).toFloat()
        mPadding = dip2px(1f).toFloat()

        canvas.save()
        mPaint.strokeWidth = dip2px(1.0f).toFloat()
        mPaint.style = Paint.Style.STROKE

        rectFSquareBG.top = mPadding
        rectFSquareBG.left = mPadding
        rectFSquareBG.right = mWidth - mPadding
        rectFSquareBG.bottom = mWidth - mPadding

        drawContent(canvas, mStep)

        if (mValue <= 25) {
            if (mValue <= 5) mValue = 5
            drawLineToRight(canvas, mValue)
            drawContentSquareLineToRight(canvas, mValue)
        } else if (mValue > 25 && mValue <= 50) {
            drawLineToBottom(canvas, mValue)
            drawContentSquareLineToBottom(canvas, mValue)
        } else if (mValue > 50 && mValue <= 75) {
            drawLineToLeft(canvas, mValue)
            drawContentSquareLineToLeft(canvas, mValue)
        } else if (mValue > 75) {
            if (mValue > 100) mValue = 100
            drawLineToTop(canvas, mValue)
            drawContentSquareLineToTop(canvas, mValue)
        }

        if (mValue <= 16) {
            drawLine(canvas, 1, mValue)
        } else if (mValue > 16 && mValue <= 32) {
            drawLine(canvas, 2, mValue)
        } else if (mValue > 32 && mValue <= 48) {
            drawLine(canvas, 3, mValue)
        } else if (mValue > 48 && mValue <= 64) {
            drawLine(canvas, 4, mValue)
        } else if (mValue > 64 && mValue <= 80) {
            drawLine(canvas, 5, mValue)
        } else if (mValue > 80) {
            drawLine(canvas, 6, mValue)
        }

        canvas.restore()
    }

    private fun drawLineToRight(canvas: Canvas, value: Int) {
        if (value <= 20) {
            canvas.drawLine(
                rectFSquareBG.left + cornerRadius,
                rectFSquareBG.top,
                rectFSquareBG.width() * value / 20f - cornerRadius,
                rectFSquareBG.top,
                mPaint
            )
        } else {
            canvas.drawLine(rectFSquareBG.left + cornerRadius, rectFSquareBG.top, rectFSquareBG.right - cornerRadius, rectFSquareBG.top, mPaint)
            rectFTopRight.top = mPadding
            rectFTopRight.left = mWidth - mPadding - cornerRadius * 2
            rectFTopRight.bottom = mPadding + cornerRadius * 2
            rectFTopRight.right = mWidth - mPadding
            canvas.drawArc(rectFTopRight, -90f, 90.0f / 5 * (value - 20), false, mPaint)
        }
    }

    private fun drawLineToBottom(canvas: Canvas, value: Int) {
        drawLineToRight(canvas, 25)
        if (value <= 45) {
            canvas.drawLine(rectFSquareBG.right, rectFSquareBG.top + cornerRadius, rectFSquareBG.right, rectFSquareBG.height() * (value - 25) / 20f, mPaint)
        } else {
            canvas.drawLine(rectFSquareBG.right, rectFSquareBG.top + cornerRadius, rectFSquareBG.right, rectFSquareBG.bottom - cornerRadius, mPaint)
            rectFBottomRight.top = mWidth - mPadding - cornerRadius * 2
            rectFBottomRight.left = mWidth - mPadding - cornerRadius * 2
            rectFBottomRight.bottom = mWidth - mPadding
            rectFBottomRight.right = mWidth - mPadding
            canvas.drawArc(rectFBottomRight, 0f, 90.0f / 5 * (value - 45), false, mPaint)
        }
    }

    private fun drawLineToLeft(canvas: Canvas, value: Int) {
        drawLineToBottom(canvas, 50)
        if (value <= 70) {
            canvas.drawLine(
                rectFSquareBG.right - cornerRadius,
                rectFSquareBG.bottom,
                rectFSquareBG.left + rectFSquareBG.width() - rectFSquareBG.width() * (value - 50) / 20f,
                rectFSquareBG.bottom,
                mPaint
            )
        } else {
            canvas.drawLine(rectFSquareBG.right - cornerRadius, rectFSquareBG.bottom, rectFSquareBG.left + cornerRadius, rectFSquareBG.bottom, mPaint)
            rectFBottomLeft.top = mWidth - mPadding - cornerRadius * 2
            rectFBottomLeft.left = mPadding
            rectFBottomLeft.bottom = mWidth - mPadding
            rectFBottomLeft.right = mPadding + cornerRadius * 2
            canvas.drawArc(rectFBottomLeft, 90f, 90f / 5 * (value - 70), false, mPaint)
        }
    }

    private fun drawLineToTop(canvas: Canvas, value: Int) {
        drawLineToLeft(canvas, 75)
        if (value <= 95) {
            canvas.drawLine(
                rectFSquareBG.left,
                rectFSquareBG.bottom - cornerRadius,
                rectFSquareBG.left,
                rectFSquareBG.top + rectFSquareBG.height() - rectFSquareBG.height() * (value - 75) / 20f,
                mPaint
            )
        } else {
            canvas.drawLine(rectFSquareBG.left, rectFSquareBG.bottom - cornerRadius, rectFSquareBG.left, rectFSquareBG.top + cornerRadius, mPaint)
            rectFTopLeft.top = mPadding
            rectFTopLeft.left = mPadding
            rectFTopLeft.bottom = mPadding + cornerRadius * 2
            rectFTopLeft.right = mPadding + cornerRadius * 2
            canvas.drawArc(rectFTopLeft, 180f, 90f / 5 * (value - 95), false, mPaint)
        }
    }

    private fun drawContentSquareLineToRight(canvas: Canvas, value: Int) {
        canvas.drawLine(rectFSquare.left, rectFSquare.top, rectFSquare.left + rectFSquare.width() * value / 25f, rectFSquare.top, mPaint)
    }

    private fun drawContentSquareLineToBottom(canvas: Canvas, value: Int) {
        drawContentSquareLineToRight(canvas, 25)
        canvas.drawLine(rectFSquare.right, rectFSquare.top, rectFSquare.right, rectFSquare.top + rectFSquare.height() * (value - 25) / 25f, mPaint)
    }

    private fun drawContentSquareLineToLeft(canvas: Canvas, value: Int) {
        drawContentSquareLineToBottom(canvas, 50)
        canvas.drawLine(rectFSquare.right, rectFSquare.bottom, rectFSquare.left + rectFSquare.width() - rectFSquare.width() * (value - 50) / 25f, rectFSquare.bottom, mPaint)
    }

    private fun drawContentSquareLineToTop(canvas: Canvas, value: Int) {
        drawContentSquareLineToLeft(canvas, 75)
        canvas.drawLine(rectFSquare.left, rectFSquare.bottom, rectFSquare.left, rectFSquare.top + rectFSquare.height() - rectFSquare.height() * (value - 75) / 25f, mPaint)
    }

    private fun drawContent(canvas: Canvas, step: Int) {
        if (step == 1) {
            marggingSquareH = mAnimatedValue * (mWidth / 2 - cornerRadius) / 0.25f
            marggingSquareV = 0f
            marggingLineH = mAnimatedValue * (mWidth / 2 - cornerRadius) / 0.25f
            marggingLineV = 0f
        } else if (step == 2) {
            marggingSquareH = mWidth / 2 - cornerRadius
            marggingSquareV = (mWidth / 2 - cornerRadius) / 0.25f * (mAnimatedValue - 0.25f)
            marggingLineH = mWidth / 2 - cornerRadius
            marggingLineV = (-mWidth / 2 + cornerRadius) / 0.25f * (mAnimatedValue - 0.25f)
        } else if (step == 3) {
            marggingSquareH = (mWidth / 2 - cornerRadius) - (mWidth / 2 - cornerRadius) / 0.25f * (mAnimatedValue - 0.5f)
            marggingSquareV = mWidth / 2 - cornerRadius
            marggingLineH = (mWidth / 2 - cornerRadius) - (mWidth / 2 - cornerRadius) / 0.25f * (mAnimatedValue - 0.5f)
            marggingLineV = -mWidth / 2 + cornerRadius
        } else if (step == 4) {
            marggingSquareH = 0f
            marggingSquareV = (mWidth / 2 - cornerRadius) - (mWidth / 2 - cornerRadius) / 0.25f * (mAnimatedValue - 0.75f)
            marggingLineH = 0f
            marggingLineV = (-mWidth / 2 + cornerRadius) - (-mWidth / 2 + cornerRadius) / 0.25f * (mAnimatedValue - 0.75f)
        }
        if (mValue == 100) {
            mPaint.style = Paint.Style.FILL
            mPaint.alpha = 100
            rectFSquare.top = mPadding + cornerRadius + marggingSquareV
            rectFSquare.left = mPadding + cornerRadius + marggingSquareH
            rectFSquare.bottom = mWidth / 2 - mPadding + marggingSquareV
            rectFSquare.right = mWidth / 2 - mPadding + marggingSquareH
            canvas.drawRect(rectFSquare, mPaint)
        }
        mPaint.style = Paint.Style.STROKE
        mPaint.alpha = 255
    }

    private fun drawShortLine(canvas: Canvas, lineWidthShort: Float, value: Int, position: Int) {
        val positon = position - 1
        canvas.drawLine(
            mWidth / 2 + mPadding + cornerRadius / 2 - marggingLineH,
            mPadding + cornerRadius + cornerRadius - marggingLineV + rectFSquare.height() / 3 * positon,
            mWidth / 2 + mPadding + cornerRadius / 2 - marggingLineH + lineWidthShort / 16.0f * value,
            mPadding + cornerRadius + cornerRadius - marggingLineV + rectFSquare.height() / 3 * positon,
            mPaint
        )
    }

    private fun drawlongLine(canvas: Canvas, lineWidthLong: Float, value: Int, position: Int) {
        val positon = position - 4
        canvas.drawLine(
            mPadding + cornerRadius,
            mPadding + cornerRadius + rectFSquare.height() / 3 * positon + mWidth / 2 + marggingLineV,
            mPadding + cornerRadius + lineWidthLong / 16.0f * value,
            mPadding + cornerRadius + rectFSquare.height() / 3 * positon + mWidth / 2 + marggingLineV,
            mPaint
        )
    }

    private fun drawLine(canvas: Canvas, count: Int, mValue: Int) {
        val lineWidthShort = (mWidth - mPadding - cornerRadius - marggingLineH) - (mWidth / 2 + mPadding + cornerRadius / 2 - marggingLineH)
        val lineWidthLong = (mWidth - mPadding - cornerRadius) - (mPadding + cornerRadius)
        if (count == 1) {
            drawShortLine(canvas, lineWidthShort, mValue, 1)
        } else if (count == 2) {
            drawShortLine(canvas, lineWidthShort, 16, 1)
            drawShortLine(canvas, lineWidthShort, mValue - 16, 2)
        } else if (count == 3) {
            drawShortLine(canvas, lineWidthShort, 16, 1)
            drawShortLine(canvas, lineWidthShort, 16, 2)
            drawShortLine(canvas, lineWidthShort, mValue - 32, 3)
        } else if (count == 4) {
            drawShortLine(canvas, lineWidthShort, 16, 1)
            drawShortLine(canvas, lineWidthShort, 16, 2)
            drawShortLine(canvas, lineWidthShort, 16, 3)
            drawlongLine(canvas, lineWidthLong, mValue - 48, 4)
        } else if (count == 5) {
            drawShortLine(canvas, lineWidthShort, 16, 1)
            drawShortLine(canvas, lineWidthShort, 16, 2)
            drawShortLine(canvas, lineWidthShort, 16, 3)
            drawlongLine(canvas, lineWidthLong, 16, 4)
            drawlongLine(canvas, lineWidthLong, mValue - 64, 5)
        } else if (count == 6) {
            drawShortLine(canvas, lineWidthShort, 16, 1)
            drawShortLine(canvas, lineWidthShort, 16, 2)
            drawShortLine(canvas, lineWidthShort, 16, 3)
            drawlongLine(canvas, lineWidthLong, 16, 4)
            drawlongLine(canvas, lineWidthLong, 16, 5)
            canvas.drawLine(
                mPadding + cornerRadius,
                mPadding + cornerRadius + rectFSquare.height() / 3 * 2 + mWidth / 2 + marggingLineV,
                mPadding + cornerRadius + lineWidthLong / 20.0f * (mValue - 80),
                mPadding + cornerRadius + rectFSquare.height() / 3 * 2 + mWidth / 2 + marggingLineV,
                mPaint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
    }

    override fun stopAnim() {
        if (valueAnimator != null) {
            clearAnimation()
            valueAnimator?.repeatCount = 0
            valueAnimator?.cancel()
            valueAnimator?.end()
            mAnimatedValue = 0f
            mStep = 1
            invalidate()
        } else {
            mAnimatedValue = 0f
            mStep = 1
            mValue = 100
            invalidate()
        }
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        if (mAnimatedValue > 0 && mAnimatedValue <= 0.25f) {
            mStep = 1
        } else if (mAnimatedValue > 0.25f && mAnimatedValue <= 0.5f) {
            mStep = 2
        } else if (mAnimatedValue > 0.5f && mAnimatedValue <= 0.75f) {
            mStep = 3
        } else if (mAnimatedValue > 0.75f && mAnimatedValue <= 1.0f) {
            mStep = 4
        }
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
