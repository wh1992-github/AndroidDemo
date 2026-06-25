package com.example.customview.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by test on 2017/4/26.
 */
class AnimationButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthValue = 0
    private var heightValue = 0
    private var circleAngle = 0
    private var defaultTwoCircleDistance = 0
    private var twoCircleDistance = 0
    private var bgColor = 0xffbc7d53.toInt()
    private var buttonString = "自定义动画按钮"
    private var duration = 1000
    private var moveDistance = 300
    private lateinit var paint: Paint
    private lateinit var textPaint: Paint
    private lateinit var okPaint: Paint
    private val textRect = Rect()
    private val animatorSet = AnimatorSet()
    private var animatorRectToAngle: ValueAnimator? = null
    private var animatorRectToSquare: ValueAnimator? = null
    private var animatorMoveToUp: ObjectAnimator? = null
    private var animatorDrawOk: ValueAnimator? = null
    private var startDrawOk = false
    private val rectf = RectF()
    private val path = Path()
    private lateinit var pathMeasure: PathMeasure
    private var effect: PathEffect? = null
    private var animationButtonListener: AnimationButtonListener? = null

    init {
        initPaint()
        setOnClickListener {
            animationButtonListener?.onClickListener()
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                animationButtonListener?.animationFinish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    fun setAnimationButtonListener(listener: AnimationButtonListener?) {
        animationButtonListener = listener
    }

    private fun initAnimation() {
        setRectToAngleAnimation()
        setRectToCircleAnimation()
        setMoveToUpAnimation()
        setDrawOkAnimation()

        animatorSet
            .play(animatorMoveToUp)
            .before(animatorDrawOk)
            .after(animatorRectToSquare)
            .after(animatorRectToAngle)
    }

    private fun setRectToAngleAnimation() {
        animatorRectToAngle = ValueAnimator.ofInt(0, heightValue / 2).apply {
            duration = this@AnimationButton.duration.toLong()
            addUpdateListener { animation ->
                circleAngle = animation.animatedValue as Int
                invalidate()
            }
        }
    }

    private fun setRectToCircleAnimation() {
        animatorRectToSquare = ValueAnimator.ofInt(0, defaultTwoCircleDistance).apply {
            duration = this@AnimationButton.duration.toLong()
            addUpdateListener { animation ->
                twoCircleDistance = animation.animatedValue as Int
                val alpha = 255 - twoCircleDistance * 255 / defaultTwoCircleDistance
                textPaint.alpha = alpha
                invalidate()
            }
        }
    }

    private fun setMoveToUpAnimation() {
        val curTranslationY = translationY
        animatorMoveToUp = ObjectAnimator.ofFloat(this, "translationY", curTranslationY, curTranslationY - moveDistance).apply {
            duration = this@AnimationButton.duration.toLong()
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun setDrawOkAnimation() {
        animatorDrawOk = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = this@AnimationButton.duration.toLong()
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

    private fun initPaint() {
        paint = Paint().apply {
            strokeWidth = 4f
            style = Paint.Style.FILL
            isAntiAlias = true
            color = bgColor
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 40f
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        okPaint = Paint().apply {
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = Color.WHITE
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthValue = w
        heightValue = h
        defaultTwoCircleDistance = (w - h) / 2
        initOk()
        initAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawOvalToCircle(canvas)
        drawText(canvas)
        if (startDrawOk) {
            canvas.drawPath(path, okPaint)
        }
    }

    private fun drawOvalToCircle(canvas: Canvas) {
        rectf.left = twoCircleDistance.toFloat()
        rectf.top = 0f
        rectf.right = (widthValue - twoCircleDistance).toFloat()
        rectf.bottom = heightValue.toFloat()
        canvas.drawRoundRect(rectf, circleAngle.toFloat(), circleAngle.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas) {
        textRect.left = 0
        textRect.top = 0
        textRect.right = widthValue
        textRect.bottom = heightValue
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(buttonString, textRect.centerX().toFloat(), baseline.toFloat(), textPaint)
    }

    private fun initOk() {
        path.moveTo((defaultTwoCircleDistance + heightValue / 8 * 3).toFloat(), (heightValue / 2).toFloat())
        path.lineTo((defaultTwoCircleDistance + heightValue / 2).toFloat(), (heightValue / 5 * 3).toFloat())
        path.lineTo((defaultTwoCircleDistance + heightValue / 3 * 2).toFloat(), (heightValue / 5 * 2).toFloat())
        pathMeasure = PathMeasure(path, true)
    }

    fun start() {
        animatorSet.start()
    }

    fun reset() {
        startDrawOk = false
        circleAngle = 0
        twoCircleDistance = 0
        defaultTwoCircleDistance = (widthValue - heightValue) / 2
        textPaint.alpha = 255
        translationY = translationY + moveDistance
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animatorSet.isRunning) {
            animatorSet.cancel()
        }
        if (animatorRectToAngle?.isRunning == true) {
            animatorRectToAngle?.cancel()
        }
        if (animatorRectToSquare?.isRunning == true) {
            animatorRectToSquare?.cancel()
        }
        if (animatorMoveToUp?.isRunning == true) {
            animatorMoveToUp?.cancel()
        }
        if (animatorDrawOk?.isRunning == true) {
            animatorDrawOk?.cancel()
        }
    }

    interface AnimationButtonListener {
        fun onClickListener()

        fun animationFinish()
    }
}
