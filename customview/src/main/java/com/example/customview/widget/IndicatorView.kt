package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 用于展示 Indicator 效果的自定义 View。
 */
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0
    private var startX = 0f
    private var centerX = 0
    private var centerY = 0
    private var mViewHeight = 0
    private var mViewWidth = 0
    private var paintWidth = 0
    private var radius = 0
    private var r = 0
    private var circleBgColor = 0xFFCED3D6.toInt()
    private var currentColor = 0xFFA0946C.toInt()
    private lateinit var circlePaint: Paint
    private lateinit var indicatorPaint: Paint
    private var pointNum = 0
    private var currentIndex = 0
    private var dis = 0
    private lateinit var rectF: RectF

    init {
        init()
    }

    private fun init() {
        rectF = RectF()
        paintWidth = dp2px(1)
        mViewHeight = dp2px(8)
        radius = dp2px(3)
        r = 2 * radius
        dis = 2 * radius
        circlePaint = getPaint(paintWidth, circleBgColor, Paint.Style.FILL)
        indicatorPaint = getPaint(paintWidth, currentColor, Paint.Style.FILL)
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
        mViewWidth = (2 * pointNum + 1) * r
        startX = ((mWidth - mViewWidth) / 2).toFloat()
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
        for (i in 0 until pointNum + 1) {
            drawCircle(canvas, i)
        }
        drawCurrentIndicator(canvas, currentIndex)
    }

    private fun drawCircle(canvas: Canvas, i: Int) {
        val x = startX + radius + i * 2 * dis
        canvas.drawCircle(x, centerY.toFloat(), radius.toFloat(), circlePaint)
    }

    private fun drawCurrentIndicator(canvas: Canvas, i: Int) {
        val x = startX + 2 * i * dis
        rectF.apply {
            left = x
            top = (centerY - radius).toFloat()
            right = x + 3 * dis
            bottom = (centerY + radius).toFloat()
        }
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), indicatorPaint)
    }

    fun setPointNum(pointNum: Int): IndicatorView {
        this.pointNum = pointNum
        return this
    }

    fun setCurrentPosition(position: Int): IndicatorView {
        currentIndex = position
        invalidate()
        return this
    }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()
}
