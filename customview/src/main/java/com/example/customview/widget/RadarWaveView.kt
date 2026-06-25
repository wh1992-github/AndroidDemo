package com.example.customview.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Created by test on 2017/4/10.
 */
class RadarWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var mBgPaint: Paint
    private var mBgColor = 0x66FFFFFF
    private var mBgRadius = 0f
    private var mMaxRadiusRate = 0.85f
    private var mCircleEdgeColorGreen = 0x2fffd1
    private var mCircleEdgeColorRed = 0xFF4081
    private var mCircleEdgeColorYellow = 0xfeff38
    private val colors = intArrayOf(mCircleEdgeColorGreen, mCircleEdgeColorYellow, mCircleEdgeColorRed)
    @Suppress("unused")
    private var mCircleShadowColor = 0x3a0909
    private lateinit var mCircleWavePaint: Paint
    private var mDefaultWaveRadius = 0f
    private var mCurrentWaveRadius = 0f
    private var mDuration = 2000L
    private val valueAnimators = arrayOfNulls<ValueAnimator>(3)
    private lateinit var mAnimatorSet: AnimatorSet
    private var animationRequested = false

    init {
        initBgPaint()
        initCirclePaint()
        for (i in valueAnimators.indices) {
            initAnimation(i)
        }
        initAnimatorSet()
    }

    private fun initBgPaint() {
        mBgPaint = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.FILL
            isAntiAlias = true
            color = mBgColor
        }
    }

    private fun initCirclePaint() {
        mCircleWavePaint = Paint().apply {
            strokeWidth = 3f
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = mCircleEdgeColorGreen
        }
    }

    private fun initAnimation(animationP: Int) {
        valueAnimators[animationP] = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = mDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                val v = valueAnimator.animatedValue as Float
                mCurrentWaveRadius = mDefaultWaveRadius + mBgRadius * v
                mCircleWavePaint.color = colors[animationP]
                mCircleWavePaint.alpha = if (v > 0.9f) {
                    (((1 - v) + 0.4f) * 100).toInt()
                } else {
                    ((v + 0.2f) * 100).toInt()
                }
                mCircleWavePaint.strokeWidth = 2 + 3 * v
                invalidate()
            }
        }
    }

    private fun initAnimatorSet() {
        mAnimatorSet = AnimatorSet().apply {
            play(valueAnimators[1]).before(valueAnimators[2]).after(valueAnimators[0])
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    if (animationRequested && isAttachedToWindow) {
                        mAnimatorSet.start()
                    }
                }

                override fun onAnimationCancel(animator: Animator) {
                }

                override fun onAnimationRepeat(animator: Animator) {
                }
            })
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBgRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawWaveCircle(canvas)
    }

    private fun drawWaveCircle(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, mCurrentWaveRadius, mCircleWavePaint)
    }

    private fun drawBg(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, mBgRadius, mBgPaint)
    }

    fun start() {
        animationRequested = true
        if (mAnimatorSet.isPaused) {
            mAnimatorSet.resume()
        } else if (!mAnimatorSet.isStarted) {
            mAnimatorSet.start()
        }
    }

    fun pause() {
        if (mAnimatorSet.isStarted) {
            mAnimatorSet.pause()
        }
    }

    fun stop() {
        animationRequested = false
        mAnimatorSet.end()
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }
}
