package com.example.customview.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.Button

/**
 * Created by test on 2017/5/23.
 *
 * loading按钮
 */
@SuppressLint("AppCompatCustomView")
open class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : Button(context, attrs) {

    @JvmField
    var mWidth = 0

    @JvmField
    var mHeight = 0

    private val textRect = RectF()
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var duration = 300
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var isLoading = false
    private var mLoadingIndex = 0
    private lateinit var mRunnable: Runnable

    init {
        initPaint()
    }

    private fun initPaint() {
        initTextPaint()
        circlePaint = getPaint(dp2px(1), Paint.Style.FILL)
        mRunnable = Runnable {
            if (isLoading && isAttachedToWindow) {
                invalidate()
            }
        }
    }

    private fun initTextPaint() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = sp2px(16).toFloat()
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = width
        mHeight = height
        centerX = (mWidth / 2).toFloat()
        centerY = (mHeight / 2).toFloat()
        radius = (mHeight / 8).toFloat()
        Log.i(TAG, "onMeasure: $centerX")
    }

    override fun onDraw(canvas: Canvas) {
        if (isLoading) {
            drawLoading(canvas, mLoadingIndex)
            mLoadingIndex = (mLoadingIndex + 1) % 3
            removeCallbacks(mRunnable)
            postDelayed(mRunnable, duration.toLong())
        } else {
            super.onDraw(canvas)
        }
    }

    private fun drawLoading(canvas: Canvas, index: Int) {
        if (index < 0 || index > 2) {
            return
        }
        when (index) {
            0 -> {
                circlePaint.color = POINT_COLOR_1
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_2
                canvas.drawCircle(centerX, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_3
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint)
            }
            1 -> {
                circlePaint.color = POINT_COLOR_3
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_1
                canvas.drawCircle(centerX, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_2
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint)
            }
            2 -> {
                circlePaint.color = POINT_COLOR_2
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_3
                canvas.drawCircle(centerX, centerY, radius, circlePaint)
                circlePaint.color = POINT_COLOR_1
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint)
            }
        }
    }

    private fun drawText(canvas: Canvas, textString: String) {
        textRect.set(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = ((textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2).toInt()
        canvas.drawText(textString, textRect.centerX(), baseline.toFloat(), textPaint)
    }

    fun startLoading() {
        if (isLoading) {
            return
        }
        isLoading = true
        mLoadingIndex = 0
        invalidate()
    }

    fun stopLoading() {
        if (!isLoading) {
            return
        }
        isLoading = false
        removeCallbacks(mRunnable)
        invalidate()
    }

    fun isLoading(): Boolean = isLoading

    override fun onDetachedFromWindow() {
        removeCallbacks(mRunnable)
        super.onDetachedFromWindow()
    }

    fun getPaint(strokeWidth: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            this.style = style
        }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources.displayMetrics).toInt()

    protected fun sp2px(spVal: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal.toFloat(), resources.displayMetrics).toInt()

    companion object {
        private const val TAG = "LoadingButton"
        private const val POINT_COLOR_1 = 0x4CFFFFFF
        private const val POINT_COLOR_2 = 0x7FFFFFFF
        private val POINT_COLOR_3 = 0xFFFFFFFF.toInt()
    }
}
