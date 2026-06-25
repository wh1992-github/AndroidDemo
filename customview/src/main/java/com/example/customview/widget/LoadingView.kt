package com.example.customview.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by test on 2017/12/4.
 */
class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0
    private var centerX = 0
    private var centerY = 0
    private var radius = 0f
    private lateinit var bgCirclePaint: Paint
    private lateinit var arcPaint: Paint
    private lateinit var okPaint: Paint
    private var paintWidth = 0
    private val rectF = RectF()
    private var bgColor = 0xFFe1e5e8.toInt()
    private var progressColor = 0xFFf66b12.toInt()
    private var duration = 800
    private var startDelay = 0
    private var startAngle = 0f
    private var sweepAngle = 20f
    private lateinit var animatorDrawLoading: ValueAnimator
    private lateinit var animatorDrawArcToCircle: ValueAnimator
    private lateinit var animatorDrawOk: ValueAnimator
    private var path = Path()
    private lateinit var pathMeasure: PathMeasure
    private var effect: PathEffect? = null
    private var startDrawOk = false
    private val animatorSet = AnimatorSet()
    private var loadingViewListener: LoadingViewListener? = null

    init {
        paintWidth = dp2px(3)
        initPaint()
        initAnimatorSet()
    }

    fun setLoadingViewListener(loadingViewListener: LoadingViewListener?) {
        this.loadingViewListener = loadingViewListener
    }

    private fun initPaint() {
        bgCirclePaint = getPaint(paintWidth, bgColor, Paint.Style.STROKE)
        arcPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE)
        okPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE)
    }

    private fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.color = color
            this.isAntiAlias = true
            this.style = style
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        centerX = w / 2
        centerY = h / 2
        radius = Math.min(w, h) / 2f - paintWidth
        rectF.left = centerX - radius
        rectF.top = centerY - radius
        rectF.right = centerX + radius
        rectF.bottom = centerY + radius
        initOk()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius, bgCirclePaint)
        canvas.drawArc(rectF, startAngle, sweepAngle, false, arcPaint)
        if (startDrawOk) {
            canvas.drawPath(path, okPaint)
        }
    }

    private fun initOk() {
        path.moveTo((mWidth / 8 * 3).toFloat(), (mHeight / 2).toFloat())
        path.lineTo((mWidth / 2).toFloat(), (mHeight / 5 * 3).toFloat())
        path.lineTo((mWidth / 3 * 2).toFloat(), (mHeight / 5 * 2).toFloat())
        pathMeasure = PathMeasure(path, true)
    }

    private fun initLoadingAnimation() {
        animatorDrawLoading = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = this@LoadingView.duration.toLong()
            startDelay = this@LoadingView.startDelay.toLong()
            repeatCount = 2
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                startAngle = value
                if (startAngle <= 180) {
                    sweepAngle += 5
                } else {
                    sweepAngle -= 5
                }
                invalidate()
            }
        }
    }

    private fun initArcToCircleAnimation() {
        animatorDrawArcToCircle = ValueAnimator.ofFloat(sweepAngle, 360f).apply {
            duration = this@LoadingView.duration.toLong()
            startDelay = 0
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                sweepAngle = value
                invalidate()
            }
        }
    }

    private fun initOkAnimation() {
        animatorDrawOk = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = this@LoadingView.duration.toLong()
            addUpdateListener { animation ->
                startDrawOk = true
                val value = animation.animatedValue as Float
                effect = DashPathEffect(
                    floatArrayOf(pathMeasure.length, pathMeasure.length),
                    value * pathMeasure.length
                )
                okPaint.pathEffect = effect
                invalidate()
            }
        }
    }

    private fun initAnimatorSet() {
        initLoadingAnimation()
        initArcToCircleAnimation()
        initOkAnimation()
        animatorSet
            .play(animatorDrawArcToCircle)
            .before(animatorDrawOk)
            .after(animatorDrawLoading)

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                loadingViewListener?.animationFinish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    fun startAnimation(): LoadingView {
        startDrawOk = false
        startAngle = 0f
        sweepAngle = 20f
        path = Path()
        initOk()
        animatorSet.cancel()
        animatorSet.start()
        return this
    }

    fun interface LoadingViewListener {
        fun animationFinish()
    }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()
}
