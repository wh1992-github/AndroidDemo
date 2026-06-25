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
import kotlin.math.roundToInt

/**
 * Created by test on 2016/12/13.
 */
class WaveViewByBezier @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var mScreenHeight = 0
    private var mScreenWidth = 0
    private lateinit var mWavePaint: Paint
    private var mWaveLength = 0
    private lateinit var mWavePath: Path
    private var mOffset = 0
    private var mWaveCount = 0
    private var mWaveAmplitude = 0
    private var waveColor = 0xaaFF7E37.toInt()
    private var waveType = DEFAULT
    private var valueAnimator: ValueAnimator? = null

    init {
        init()
    }

    private fun init() {
        mWaveAmplitude = dp2px(10)
        mWaveLength = dp2px(500)
        initPaint()
    }

    private fun initPaint() {
        mWavePath = Path()
        mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = waveColor
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mScreenHeight = h
        mScreenWidth = w
        mWaveCount = (mScreenWidth / mWaveLength + 1.5).roundToInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (waveType) {
            SIN -> drawSinPath(canvas)
            COS -> drawCosPath(canvas)
        }
    }

    private fun drawSinPath(canvas: Canvas) {
        mWavePath.reset()
        mWavePath.moveTo((-mWaveLength + mOffset).toFloat(), mWaveAmplitude.toFloat())

        for (i in 0 until mWaveCount) {
            mWavePath.quadTo(
                (-mWaveLength * 3 / 4 + mOffset + i * mWaveLength).toFloat(),
                (-mWaveAmplitude).toFloat(),
                (-mWaveLength / 2 + mOffset + i * mWaveLength).toFloat(),
                mWaveAmplitude.toFloat()
            )
            mWavePath.quadTo(
                (-mWaveLength / 4 + mOffset + i * mWaveLength).toFloat(),
                (3 * mWaveAmplitude).toFloat(),
                (mOffset + i * mWaveLength).toFloat(),
                mWaveAmplitude.toFloat()
            )
        }

        mWavePath.lineTo(width.toFloat(), height.toFloat())
        mWavePath.lineTo(0f, height.toFloat())
        mWavePath.close()
        canvas.drawPath(mWavePath, mWavePaint)
    }

    private fun drawCosPath(canvas: Canvas) {
        mWavePath.reset()
        mWavePath.moveTo((-mWaveLength + mOffset).toFloat(), mWaveAmplitude.toFloat())

        for (i in 0 until mWaveCount) {
            mWavePath.quadTo(
                (-mWaveLength * 3 / 4 + mOffset + i * mWaveLength).toFloat(),
                (3 * mWaveAmplitude).toFloat(),
                (-mWaveLength / 2 + mOffset + i * mWaveLength).toFloat(),
                mWaveAmplitude.toFloat()
            )
            mWavePath.quadTo(
                (-mWaveLength / 4 + mOffset + i * mWaveLength).toFloat(),
                (-mWaveAmplitude).toFloat(),
                (mOffset + i * mWaveLength).toFloat(),
                mWaveAmplitude.toFloat()
            )
        }

        mWavePath.lineTo(width.toFloat(), height.toFloat())
        mWavePath.lineTo(0f, height.toFloat())
        mWavePath.close()
        canvas.drawPath(mWavePath, mWavePaint)
    }

    private fun initAnimation() {
        valueAnimator?.cancel()
        valueAnimator = ValueAnimator.ofInt(0, mWaveLength).apply {
            duration = 2000
            startDelay = 300
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                mOffset = animation.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun startAnimation() {
        if (valueAnimator?.isStarted == true) return
        initAnimation()
    }

    fun stopAnimation() {
        valueAnimator?.cancel()
        valueAnimator = null
    }

    fun pauseAnimation() {
        valueAnimator?.pause()
    }

    fun resumeAnimation() {
        valueAnimator?.resume()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
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
        private const val DEFAULT = SIN
    }
}
