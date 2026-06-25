package com.example.customview.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Created by test on 2017/5/23.
 */
open class BaseView : View {

    @JvmField
    var mWidth = 0

    @JvmField
    var mHeight = 0

    @JvmField
    var mViewHeight = 0

    constructor(context: Context) : this(context, null) {
        mViewHeight = dp2px(45)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height))
    }

    private fun measureWidth(mode: Int, width: Int): Int {
        when (mode) {
            MeasureSpec.EXACTLY -> mWidth = width
        }
        return mWidth
    }

    private fun measureHeight(mode: Int, height: Int): Int {
        when (mode) {
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST -> mHeight = mViewHeight
            MeasureSpec.EXACTLY -> mHeight = height
        }
        return mHeight
    }

    fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            this.style = style
        }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()

    protected fun sp2px(spVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal.toFloat(),
            resources.displayMetrics
        ).toInt()
}
