package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by test on 2018/1/31.
 */
class LoadingLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0
    private var centerX = 0
    private var centerY = 0
    private var dis = 0f
    private var mViewHeight = 0
    private lateinit var bgPaint: Paint
    private lateinit var loadingPaint: Paint
    private var paintWidth = 0
    private var bgColor = 0xFFe1e5e8.toInt()
    private var loadingColor = 0xFFf66b12.toInt()
    private var loadingAnimator: ValueAnimator? = null
    private var duration = 800
    private var startDelay = 0
    private var isStopAnimation = false

    init {
        getAtt(attrs)
        init()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun getAtt(attrs: AttributeSet?) {
    }

    private fun init() {
        paintWidth = dp2px(2)
        mViewHeight = paintWidth
        bgPaint = getPaint(paintWidth, bgColor, Paint.Style.FILL)
        loadingPaint = getPaint(paintWidth, loadingColor, Paint.Style.FILL)
    }

    private fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            isAntiAlias = true
            this.style = style
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        centerX = w / 2
        centerY = h / 2
        initLoadingAnimation()
        startLoading()
    }

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, centerY.toFloat(), mWidth.toFloat(), centerY.toFloat(), bgPaint)
        if (!isStopAnimation) {
            canvas.drawLine(centerX.toFloat(), centerY.toFloat(), centerX - dis, centerY.toFloat(), loadingPaint)
            canvas.drawLine(centerX.toFloat(), centerY.toFloat(), centerX + dis, centerY.toFloat(), loadingPaint)
        }
    }

    private fun initLoadingAnimation() {
        val loadingMoveDistance = mWidth / 2f
        loadingAnimator = ValueAnimator.ofFloat(0f, loadingMoveDistance).apply {
            duration = this@LoadingLineView.duration.toLong()
            startDelay = this@LoadingLineView.startDelay.toLong()
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                dis = value
                loadingPaint.alpha = if (value <= loadingMoveDistance / 2) {
                    ((255 * value) * 2 / loadingMoveDistance).toInt()
                } else {
                    (255 - (255 * value) * 2 / loadingMoveDistance).toInt()
                }
                invalidate()
            }
        }
    }

    fun startLoading() {
        loadingAnimator?.start()
        isStopAnimation = false
    }

    fun stopLoading() {
        loadingAnimator?.cancel()
        isStopAnimation = true
        invalidate()
    }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()
}
