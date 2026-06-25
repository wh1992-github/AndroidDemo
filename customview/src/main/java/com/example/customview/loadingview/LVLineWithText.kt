package com.example.customview.loadingview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Created by test on 16/6/20.
 */
open class LVLineWithText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaintBar = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = dip2px(10f).toFloat()
        strokeWidth = dip2px(1f).toFloat()
    }
    private val mPaintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = dip2px(10f).toFloat()
        strokeWidth = dip2px(1f).toFloat()
    }
    private var mWidth = 0f
    private var mHigh = 0f
    private var mValue = 0
    private var mPadding = 5f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHigh = measuredHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val text = "$mValue%"
        val textLength = getFontLength(mPaintText, text)
        val textHigh = getFontHeight(mPaintText, text)
        if (mValue == 0) {
            canvas.drawText(text, mPadding, mHigh / 2 + textHigh / 2, mPaintText)
            canvas.drawLine(mPadding + textLength, mHigh / 2, mWidth - mPadding, mHigh / 2, mPaintBar)
        } else if (mValue >= 100) {
            canvas.drawText(text, mWidth - mPadding - textLength, mHigh / 2 + textHigh / 2, mPaintText)
            canvas.drawLine(mPadding, mHigh / 2, mWidth - mPadding - textLength, mHigh / 2, mPaintBar)
        } else {
            val w = mWidth - 2 * mPadding - textLength
            canvas.drawLine(mPadding, mHigh / 2, mPadding + w * mValue / 100, mHigh / 2, mPaintBar)
            canvas.drawLine(mPadding + w * mValue / 100 + textLength, mHigh / 2, mWidth - mPadding, mHigh / 2, mPaintBar)
            canvas.drawText(text, mPadding + w * mValue / 100, mHigh / 2 + textHigh / 2, mPaintText)
        }
    }

    fun setTextColor(color: Int) {
        mPaintText.color = color
        postInvalidate()
    }

    fun setViewColor(color: Int) {
        mPaintBar.color = color
        postInvalidate()
    }

    fun getFontLength(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.width().toFloat()
    }

    fun getFontHeight(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.height().toFloat()
    }

    fun dip2px(dpValue: Float): Int =
        (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()

    fun setValue(value: Int) {
        mValue = value
        invalidate()
    }
}
