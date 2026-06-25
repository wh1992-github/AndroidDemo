package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customview.R

/**
 * Created by test on 2017/6/20.
 *
 * y = A * sin(omega * x + phi) + k
 */
class WaveViewBySinCos(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {

    private var amplitude = 0
    private var offsetY = 0
    private var waveColor = 0xaaFF7E37.toInt()
    private var phi = 0f
    private var waveSpeed = 3f
    private var omega = 0.0
    private var startPeriod = 0.0
    private var waveStart = false
    private lateinit var path: Path
    private lateinit var paint: Paint
    private var waveType = SIN
    private var waveFillType = BOTTOM
    private var valueAnimator: ValueAnimator? = null
    private var animationRequested = false
    private var animationPaused = false

    init {
        getAttr(context, attrs)
        animationRequested = waveStart
        offsetY = amplitude
        initPaint()
        initAnimation()
    }

    private fun getAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarWaveView)
        waveType = typedArray.getInt(R.styleable.RadarWaveView_waveType, SIN)
        waveFillType = typedArray.getInt(R.styleable.RadarWaveView_waveFillType, BOTTOM)
        amplitude = typedArray.getDimensionPixelOffset(R.styleable.RadarWaveView_waveAmplitude, dp2px(10))
        waveColor = typedArray.getColor(R.styleable.RadarWaveView_waveColor, waveColor)
        waveSpeed = typedArray.getFloat(R.styleable.RadarWaveView_waveSpeed, waveSpeed)
        startPeriod = typedArray.getFloat(R.styleable.RadarWaveView_waveStartPeriod, 0f).toDouble()
        waveStart = typedArray.getBoolean(R.styleable.RadarWaveView_waveStart, false)
        typedArray.recycle()
    }

    private fun initPaint() {
        path = Path()
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            color = waveColor
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        omega = 2 * Math.PI / width
    }

    override fun onDraw(canvas: Canvas) {
        when (waveType) {
            SIN -> drawSin(canvas)
            COS -> drawCos(canvas)
        }
    }

    private fun drawCos(canvas: Canvas) {
        when (waveFillType) {
            TOP -> fillTop(canvas, useCosine = true)
            BOTTOM -> fillBottom(canvas, useCosine = true)
        }
    }

    private fun drawSin(canvas: Canvas) {
        when (waveFillType) {
            TOP -> fillTop(canvas, useCosine = false)
            BOTTOM -> fillBottom(canvas, useCosine = false)
        }
    }

    private fun fillTop(canvas: Canvas, useCosine: Boolean) {
        path.reset()
        path.moveTo(0f, height.toFloat())

        var x = 0f
        while (x <= width) {
            val y = calculateWaveY(x, useCosine)
            path.lineTo(x, height - y)
            x += 20f
        }

        path.lineTo(width.toFloat(), 0f)
        path.lineTo(0f, 0f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun fillBottom(canvas: Canvas, useCosine: Boolean) {
        path.reset()
        path.moveTo(0f, 0f)

        var x = 0f
        while (x <= width) {
            val y = calculateWaveY(x, useCosine)
            path.lineTo(x, y)
            x += 20f
        }

        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun calculateWaveY(x: Float, useCosine: Boolean): Float {
        val angle = omega * x + phi + Math.PI * startPeriod
        val waveValue = if (useCosine) Math.cos(angle) else Math.sin(angle)
        return (amplitude * waveValue + offsetY).toFloat()
    }

    private fun initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                phi -= waveSpeed / 100f
                if (phi <= -2 * Math.PI) {
                    phi = 0f
                }
                invalidate()
            }
        }
    }

    fun startAnimation() {
        animationRequested = true
        animationPaused = false
        if (isAttachedToWindow) {
            valueAnimator?.start()
        }
    }

    fun stopAnimation() {
        animationRequested = false
        animationPaused = false
        valueAnimator?.cancel()
    }

    fun pauseAnimation() {
        if (valueAnimator?.isStarted == true) {
            animationPaused = true
            valueAnimator?.pause()
        }
    }

    fun resumeAnimation() {
        if (animationRequested && animationPaused) {
            animationPaused = false
            if (valueAnimator?.isPaused == true) {
                valueAnimator?.resume()
            } else if (isAttachedToWindow) {
                valueAnimator?.start()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (animationRequested && !animationPaused) {
            valueAnimator?.start()
        }
    }

    override fun onDetachedFromWindow() {
        valueAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()

    companion object {
        private const val SIN = 0
        private const val COS = 1
        private const val TOP = 0
        private const val BOTTOM = 1
    }
}
