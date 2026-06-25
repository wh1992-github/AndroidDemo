package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customview.R

/**
 * Created by test on 2017/5/14.
 */
class CircleProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var mProgress = 0f
    private var currentProgress = 0f
    private lateinit var circleBgPaint: Paint
    private lateinit var progressPaint: Paint
    private var circleBgColor = 0xFFe1e5e8.toInt()
    private var progressColor = 0xFFf66b12.toInt()
    private var defaultStrokeWidth = 10
    private var circleBgStrokeWidth = defaultStrokeWidth
    private var progressStrokeWidth = defaultStrokeWidth
    private val rectF = RectF()
    private var progressAnimator: ValueAnimator? = null
    private var duration = 1000
    private var startDelay = 500
    private var isDrawCenterProgressText = false
    private var centerProgressTextSize = 10
    private var centerProgressTextColor = Color.BLACK
    private var centerProgressTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressListener: ProgressListener? = null

    init {
        getAttr(context, attrs)
        initPaint()
        initTextPaint()
    }

    private fun getAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView)
        circleBgStrokeWidth = typedArray.getDimensionPixelOffset(
            R.styleable.CircleProgressBarView_circleBgStrokeWidth,
            defaultStrokeWidth
        )
        progressStrokeWidth = typedArray.getDimensionPixelOffset(
            R.styleable.CircleProgressBarView_progressStrokeWidth,
            defaultStrokeWidth
        )
        circleBgColor = typedArray.getColor(R.styleable.CircleProgressBarView_circleBgColor, circleBgColor)
        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progressColor, progressColor)
        duration = typedArray.getInt(R.styleable.CircleProgressBarView_circleAnimationDuration, duration)
        isDrawCenterProgressText = typedArray.getBoolean(
            R.styleable.CircleProgressBarView_isDrawCenterProgressText,
            false
        )
        centerProgressTextColor = typedArray.getColor(
            R.styleable.CircleProgressBarView_centerProgressTextColor,
            centerProgressTextColor
        )
        centerProgressTextSize = typedArray.getDimensionPixelOffset(
            R.styleable.CircleProgressBarView_centerProgressTextSize,
            sp2px(centerProgressTextSize)
        )
        typedArray.recycle()
    }

    private fun initPaint() {
        circleBgPaint = getPaint(circleBgStrokeWidth, circleBgColor)
        progressPaint = getPaint(progressStrokeWidth, progressColor)
    }

    private fun getPaint(strokeWidth: Int, color: Int): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }

    private fun initTextPaint() {
        centerProgressTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = centerProgressTextSize.toFloat()
            color = centerProgressTextColor
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    private fun initAnimation() {
        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(0f, mProgress).apply {
            duration = this@CircleProgressBarView.duration.toLong()
            startDelay = this@CircleProgressBarView.startDelay.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                mProgress = value
                currentProgress = value * 360 / 100
                progressListener?.currentProgressListener(roundTwo(value))
                invalidate()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = Math.min(w, h) / 2f - Math.max(circleBgStrokeWidth, progressStrokeWidth)
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, radius, circleBgPaint)
        canvas.drawArc(rectF, 90f, currentProgress, false, progressPaint)
        if (isDrawCenterProgressText) {
            drawCenterProgressText(canvas, "${mProgress.toInt()}%")
        }
    }

    private fun drawCenterProgressText(canvas: Canvas, currentProgress: String) {
        val fontMetrics = centerProgressTextPaint.fontMetricsInt
        val baseline = ((rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2).toInt()
        canvas.drawText(currentProgress, rectF.centerX(), baseline.toFloat(), centerProgressTextPaint)
    }

    fun startProgressAnimation() {
        progressAnimator?.start()
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

    fun setProgressWithAnimation(progress: Float): CircleProgressBarView {
        mProgress = progress
        initAnimation()
        return this
    }

    fun setCurrentProgress(progress: Float): CircleProgressBarView {
        mProgress = progress
        currentProgress = progress * 360 / 100
        invalidate()
        return this
    }

    fun interface ProgressListener {
        fun currentProgressListener(currentProgress: Float)
    }

    fun setProgressListener(listener: ProgressListener): CircleProgressBarView {
        progressListener = listener
        return this
    }

    override fun onDetachedFromWindow() {
        progressAnimator?.cancel()
        progressAnimator = null
        super.onDetachedFromWindow()
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
        fun roundTwo(originNum: Float): Float = (Math.round(originNum * 10) / 10.00).toFloat()
    }
}
