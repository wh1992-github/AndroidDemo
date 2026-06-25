package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import java.text.DecimalFormat

/**
 * Created by test on 2017/11/30.
 */
class ProductProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var bgPaint: Paint
    private lateinit var progressPaint: Paint
    private lateinit var textPaint: Paint
    private var mWidth = 0
    private var mHeight = 0
    private var mViewHeight = 0
    private var mProgress = 0f
    private var textHeight = 0f
    private var textWidth = 0f
    private var currentProgress = 0f
    private var progressAnimator: ValueAnimator? = null
    private var duration = 1000
    private var startDelay = 500
    private var progressPaintWidth = 0
    private var progressHeight = 0
    private var progressMarginTop = 0
    private var moveDis = 0f
    private val textRect = Rect()
    private var textString = "宸插敭0%"
    private var textPaintSize = 0
    private var bgColor = 0xFFeaeef0.toInt()
    private var progressColor = 0xFFf66b12.toInt()
    private val bgRectF = RectF()
    private val progressRectF = RectF()
    private var roundRectRadius = 0
    private var progressListener: ProgressListener? = null

    init {
        init()
        initPaint()
        initTextPaint()
    }

    private fun init() {
        progressPaintWidth = dp2px(1)
        progressHeight = dp2px(3)
        roundRectRadius = dp2px(3)
        textPaintSize = sp2px(10)
        textHeight = dp2px(10).toFloat()
        progressMarginTop = dp2px(4)
        mViewHeight = (textHeight + progressMarginTop + progressPaintWidth * 2 + progressHeight).toInt()
    }

    private fun initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.FILL)
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.FILL)
    }

    private fun initTextPaint() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = textPaintSize.toFloat()
            color = progressColor
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    private fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            isAntiAlias = true
            this.style = style
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
        drawText(canvas, textString)
        drawBgProgress(canvas)
        drawProgress(canvas)
    }

    private fun drawBgProgress(canvas: Canvas) {
        bgRectF.left = 0f
        bgRectF.top = textHeight + progressMarginTop
        bgRectF.right = measuredWidth.toFloat()
        bgRectF.bottom = bgRectF.top + progressHeight
        canvas.drawRoundRect(bgRectF, roundRectRadius.toFloat(), roundRectRadius.toFloat(), bgPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        progressRectF.left = 0f
        progressRectF.top = textHeight + progressMarginTop
        progressRectF.right = currentProgress
        progressRectF.bottom = progressRectF.top + progressHeight
        canvas.drawRoundRect(progressRectF, roundRectRadius.toFloat(), roundRectRadius.toFloat(), progressPaint)
    }

    private fun drawText(canvas: Canvas, textString: String) {
        textRect.left = moveDis.toInt()
        textRect.top = 0
        textRect.right = (textPaint.measureText(textString) + moveDis).toInt()
        textRect.bottom = textHeight.toInt()
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(textString, textRect.centerX().toFloat(), baseline.toFloat(), textPaint)
    }

    private fun initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0f, mProgress).apply {
            duration = this@ProductProgressBar.duration.toLong()
            startDelay = this@ProductProgressBar.startDelay.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                textString = "宸插敭${formatNum(value.toInt())}%"
                textWidth = textPaint.measureText(textString)
                currentProgress = value * mWidth / 100
                progressListener?.currentProgressListener(value)
                if (currentProgress >= textWidth && currentProgress <= mWidth) {
                    moveDis = currentProgress - textWidth
                }
                invalidate()
            }
            if (!isStarted) {
                start()
            }
        }
    }

    fun interface ProgressListener {
        fun currentProgressListener(currentProgress: Float)
    }

    fun setProgressListener(listener: ProgressListener): ProductProgressBar {
        progressListener = listener
        return this
    }

    fun setProgress(progress: Float): ProductProgressBar {
        mProgress = progress
        initAnimation()
        return this
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

    companion object {
        @JvmStatic
        fun formatNum(money: Int): String = DecimalFormat("0").format(money)
    }
}
