package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet

/**
 * Created by test on 16/6/27.
 */
open class LVRingProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private var mBitmapBg: Bitmap? = null
    private lateinit var mPaintText: Paint
    private var MaxAngle = 359f
    private var mPadding = 0
    private var mWidth = 0
    private var rectFBg = RectF()
    private var Progress = 0
    private var mAnimatedValue = 0f

    @JvmField
    var ProStartColor = Color.argb(100, 0, 242, 123)

    @JvmField
    var ProEndColor = Color.argb(100, 86, 171, 228)

    fun getProgress(): Int = Progress

    fun setProgress(progress: Int) {
        Progress = progress
        invalidate()
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
        }
        mPaintText = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    fun setTextColor(color: Int) {
        mPaintText.color = color
        postInvalidate()
    }

    fun setPorBarStartColor(color: Int) {
        ProStartColor = color
    }

    fun setPorBarEndColor(color: Int) {
        ProEndColor = color
    }

    private fun getmBitmapBg(paint: Paint): Bitmap {
        if (mBitmapBg == null) {
            mBitmapBg = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(mBitmapBg!!)
            paint.apply {
                isAntiAlias = true
                strokeWidth = mPadding.toFloat()
                style = Paint.Style.STROKE
                setShadowLayer((mPadding / 3).toFloat(), 0f, (mPadding / 4).toFloat(), Color.argb(100, 0, 0, 0))
            }
            val pathBg = Path().apply {
                addArc(rectFBg, 0f, 360f)
            }
            canvas.drawPath(pathBg, paint)
        }
        return mBitmapBg!!
    }

    private fun drawBg(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(getmBitmapBg(paint), 0f, 0f, paint)
    }

    private fun drawProgress(canvas: Canvas, paint: Paint, sweepAngle: Int) {
        paint.apply {
            reset()
            isAntiAlias = true
            strokeWidth = mPadding.toFloat()
            style = Paint.Style.STROKE
        }

        val pathProgress = Path().apply {
            addArc(rectFBg, -90f, sweepAngle.toFloat())
        }

        val mShader = LinearGradient(
            rectFBg.left,
            rectFBg.top,
            rectFBg.left,
            rectFBg.bottom,
            intArrayOf(ProStartColor, ProEndColor),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )

        paint.apply {
            shader = mShader
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        canvas.drawPath(pathProgress, paint)
        paint.shader = null

        mPaintText.textSize = mPaint.strokeWidth / 2
        val text = "${(sweepAngle / MaxAngle * 100).toInt()}%"
        canvas.drawTextOnPath(
            text,
            pathProgress,
            (Math.PI * rectFBg.width() * (sweepAngle / MaxAngle) - getFontLength(mPaintText, text) * 1.5f).toFloat(),
            getFontHeight(mPaintText) / 3,
            mPaintText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        mPadding = mWidth / 10
        rectFBg = RectF(
            measuredWidth / 2f - mWidth / 2f + mPadding,
            measuredHeight / 2f - mWidth / 2f + mPadding,
            measuredWidth / 2f + mWidth / 2f - mPadding,
            measuredHeight / 2f + mWidth / 2f - mPadding
        )

        drawBg(canvas, mPaint)
        drawProgress(canvas, mPaint, (MaxAngle / 100f * getProgress()).toInt())
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight else measuredWidth
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        setProgress((mAnimatedValue * 100).toInt())
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0f
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    override fun SetAnimRepeatCount(): Int = 0

    override fun AnimIsRunning() {
    }
}
