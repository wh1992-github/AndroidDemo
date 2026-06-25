package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import java.text.DecimalFormat

/**
 * Created by test on 2017/5/14.
 */
class HorizontalProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private lateinit var bgPaint: Paint
    private lateinit var progressPaint: Paint
    private lateinit var tipPaint: Paint
    private lateinit var textPaint: Paint
    private var mWidth = 0
    private var mHeight = 0
    private var mViewHeight = 0
    private var mProgress = 0f
    private var currentProgress = 0f
    private var progressAnimator: ValueAnimator? = null
    private var duration = 1000
    private var startDelay = 500
    private var progressPaintWidth = 0
    private var tipPaintWidth = 0
    private var tipHeight = 0
    private var tipWidth = 0
    private val path = Path()
    private var triangleHeight = 0
    private var progressMarginTop = 0
    private var moveDis = 0f
    private val textRect = Rect()
    private var textString = "0"
    private var textPaintSize = 0
    private var bgColor = 0xFFe1e5e8.toInt()
    private var progressColor = 0xFFf66b12.toInt()
    private val rectF = RectF()
    private var roundRectRadius = 0
    private var progressListener: ProgressListener? = null

    init {
        init()
        initPaint()
    }

    private fun init() {
        progressPaintWidth = dp2px(4)
        tipHeight = dp2px(15)
        tipWidth = dp2px(30)
        tipPaintWidth = dp2px(1)
        triangleHeight = dp2px(3)
        roundRectRadius = dp2px(2)
        textPaintSize = sp2px(10)
        progressMarginTop = dp2px(8)
        mViewHeight = tipHeight + tipPaintWidth + triangleHeight + progressPaintWidth + progressMarginTop
    }

    private fun initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE)
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE)
        tipPaint = getPaint(tipPaintWidth, progressColor, Paint.Style.FILL)
        initTextPaint()
    }

    private fun initTextPaint() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = textPaintSize.toFloat()
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    private fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
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
        canvas.drawLine(paddingLeft.toFloat(), (tipHeight + progressMarginTop).toFloat(), width.toFloat(), (tipHeight + progressMarginTop).toFloat(), bgPaint)
        canvas.drawLine(paddingLeft.toFloat(), (tipHeight + progressMarginTop).toFloat(), currentProgress, (tipHeight + progressMarginTop).toFloat(), progressPaint)
        drawTipView(canvas)
        drawText(canvas, textString)
    }

    private fun drawTipView(canvas: Canvas) {
        drawRoundRect(canvas)
        drawTriangle(canvas)
    }

    private fun drawRoundRect(canvas: Canvas) {
        rectF.set(moveDis, 0f, tipWidth + moveDis, tipHeight.toFloat())
        canvas.drawRoundRect(rectF, roundRectRadius.toFloat(), roundRectRadius.toFloat(), tipPaint)
    }

    private fun drawTriangle(canvas: Canvas) {
        path.moveTo(tipWidth / 2 - triangleHeight + moveDis, tipHeight.toFloat())
        path.lineTo(tipWidth / 2 + moveDis, (tipHeight + triangleHeight).toFloat())
        path.lineTo(tipWidth / 2 + triangleHeight + moveDis, tipHeight.toFloat())
        canvas.drawPath(path, tipPaint)
        path.reset()
    }

    private fun drawText(canvas: Canvas, textString: String) {
        textRect.left = moveDis.toInt()
        textRect.top = 0
        textRect.right = (tipWidth + moveDis).toInt()
        textRect.bottom = tipHeight
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText("$textString%", textRect.centerX().toFloat(), baseline.toFloat(), textPaint)
    }

    private fun initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0f, mProgress).apply {
            duration = this@HorizontalProgressBar.duration.toLong()
            startDelay = this@HorizontalProgressBar.startDelay.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                textString = formatNum(format2Int(value.toDouble()))
                currentProgress = value * mWidth / 100
                progressListener?.currentProgressListener(value)
                if (currentProgress >= tipWidth / 2 && currentProgress <= mWidth - tipWidth / 2) {
                    moveDis = currentProgress - tipWidth / 2
                }
                invalidate()
                setCurrentProgress(value)
            }
            start()
        }
    }

    fun setProgressWithAnimation(progress: Float): HorizontalProgressBar {
        mProgress = progress
        initAnimation()
        return this
    }

    fun setCurrentProgress(progress: Float): HorizontalProgressBar {
        mProgress = progress
        currentProgress = progress * mWidth / 100
        textString = formatNum(format2Int(progress.toDouble()))
        if (currentProgress >= tipWidth / 2 && currentProgress <= mWidth - tipWidth / 2) {
            moveDis = currentProgress - tipWidth / 2
        }
        invalidate()
        return this
    }

    fun startProgressAnimation() {
        if (progressAnimator != null && progressAnimator?.isRunning == false && progressAnimator?.isStarted == false) {
            progressAnimator?.start()
        }
    }

    fun pauseProgressAnimation() {
        progressAnimator?.pause()
    }

    fun resumeProgressAnimation() {
        progressAnimator?.resume()
    }

    fun stopProgressAnimation() {
        progressAnimator?.end()
    }

    fun interface ProgressListener {
        fun currentProgressListener(currentProgress: Float)
    }

    fun setProgressListener(listener: ProgressListener): HorizontalProgressBar {
        progressListener = listener
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
        fun formatNumTwo(money: Double): String = DecimalFormat("0.00").format(money)

        @JvmStatic
        fun formatNum(money: Int): String = DecimalFormat("0").format(money)

        @JvmStatic
        fun format2Int(i: Double): Int = i.toInt()
    }
}
