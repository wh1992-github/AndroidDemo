package com.example.customview.loadingview.mac

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customview.R

/**
 * Created by test on 16/7/7.
 */
open class LVComputer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    protected lateinit var mPaint: Paint
    protected var mHigh = 0f
    protected var mWidth = 0f
    protected lateinit var ios: Bitmap
    protected lateinit var android: Bitmap
    protected var mShader: Shader? = null
    protected val rectBg = RectF()
    protected val rectScreen = RectF()
    protected val rectScreenWithin = RectF()
    protected val rectScreenShow = RectF()
    protected val rectKeyboard = RectF()
    protected val rectKeyboardShadow = RectF()
    protected val pathKeyboardTouch = Path()
    protected val pathKeyboardBottom = Path()
    protected val pathComputerShadow = Path()
    protected val pathScreenReflective = Path()
    protected val colorScreenWithin = Color.rgb(0, 0, 0)
    protected val colorScreenShow = Color.rgb(15, 15, 15)
    protected val colorCamera = Color.rgb(80, 81, 82)
    protected val colorCameraCenter = Color.rgb(15, 15, 15)
    protected val colorScreenReflective = Color.argb(10, 255, 255, 255)
    protected val colorKeyboard = Color.rgb(209, 211, 212)
    protected val colorKeyboardShadow = Color.rgb(188, 190, 192)
    protected val colorKeyboardTouch = Color.rgb(165, 165, 165)
    protected var mPadding = 2f
    private var valueAnimator: ValueAnimator? = null
    protected var mAnimatedValue = 1.0f

    init {
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHigh = measuredHeight.toFloat()
        mWidth = measuredWidth.toFloat()
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun drawScreen(canvas: Canvas) {
        rectBg.top = mPadding
        rectBg.left = mPadding
        rectBg.right = mWidth - mPadding
        rectBg.bottom = mHigh - mPadding
        rectScreen.top = rectBg.top
        rectScreen.left = rectBg.left + rectBg.width() / 6
        rectScreen.right = rectBg.right - rectBg.width() / 6
        rectScreen.bottom = rectBg.bottom - 20
        mPaint.color = Color.rgb(165, 165, 165)
        canvas.drawRoundRect(rectScreen, rectBg.width() / 6 / 5f, rectBg.width() / 6 / 5f, mPaint)
    }

    private fun drawScreenWithin(canvas: Canvas) {
        rectScreenWithin.top = rectScreen.top + 2
        rectScreenWithin.bottom = rectScreen.bottom - 2
        rectScreenWithin.left = rectScreen.left + 2
        rectScreenWithin.right = rectScreen.right - 2
        mPaint.color = colorScreenWithin
        canvas.drawRoundRect(rectScreenWithin, rectBg.width() / 6 / 5f - 2, rectBg.width() / 6 / 5f - 2, mPaint)
    }

    private fun drawScreenShow(canvas: Canvas) {
        rectScreenShow.top = rectScreenWithin.top + rectBg.width() / 6 / 6f * 1.1f
        rectScreenShow.bottom = rectScreenWithin.bottom - rectBg.width() / 6f / 6f - 15
        rectScreenShow.left = rectScreenWithin.left + rectBg.width() / 6 / 6f
        rectScreenShow.right = rectScreenWithin.right - rectBg.width() / 6 / 6f
        mPaint.color = colorScreenShow
        canvas.drawRect(rectScreenShow, mPaint)
    }

    private fun drawCamera(canvas: Canvas) {
        mPaint.color = colorCamera
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 3f, mPaint)
        mPaint.color = colorCameraCenter
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 1.5f, mPaint)
    }

    private fun drawContent(canvas: Canvas) {
        mPaint.color = Color.WHITE
        ios = setBitmapSize(R.drawable.apple, (rectScreen.width() / 10).toInt())
        canvas.drawBitmap(ios, rectScreenShow.centerX() - ios.width - 5, rectScreenShow.centerY() - ios.height / 2, mPaint)
        android = setBitmapSize(R.drawable.android, (rectScreenShow.width() / 11).toInt())
        canvas.drawBitmap(android, rectScreenShow.centerX() + 5, rectScreenShow.centerY() - android.height / 2, mPaint)
    }

    private fun drawScreenReflective(canvas: Canvas) {
        pathScreenReflective.reset()
        pathScreenReflective.moveTo(rectScreen.left + rectScreen.width() / 10f * 6f, rectScreen.top)
        pathScreenReflective.lineTo(rectScreen.right - rectScreen.width() / 10f, rectScreen.bottom)
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.bottom)
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.top)
        pathScreenReflective.close()
        mPaint.color = colorScreenReflective
        canvas.drawPath(pathScreenReflective, mPaint)
    }

    private fun drawKeyboard(canvas: Canvas) {
        rectKeyboard.top = rectScreenWithin.bottom - rectBg.width() / 6f / 8f
        rectKeyboard.bottom = rectScreen.bottom
        rectKeyboard.left = rectBg.left + rectBg.width() / 6f / 3f
        rectKeyboard.right = rectBg.right - rectBg.width() / 6f / 3f
        mPaint.color = colorKeyboard
        canvas.drawRect(rectKeyboard, mPaint)
    }

    private fun drawKeyboardShadow(canvas: Canvas) {
        rectKeyboardShadow.top = rectKeyboard.top + rectKeyboard.height() / 3
        rectKeyboardShadow.bottom = rectKeyboard.bottom
        rectKeyboardShadow.left = rectKeyboard.left + rectKeyboard.height() / 3 * 2
        rectKeyboardShadow.right = rectKeyboard.right - rectKeyboard.height() / 3 * 2
        mPaint.color = colorKeyboardShadow
        canvas.drawRect(rectKeyboardShadow, mPaint)
    }

    private fun drawKeyboardTouch(canvas: Canvas) {
        pathKeyboardTouch.reset()
        pathKeyboardTouch.moveTo(rectKeyboard.centerX() - rectBg.width() / 6f / 3f * 1.2f, rectKeyboard.top)
        pathKeyboardTouch.quadTo(
            rectKeyboard.centerX() - rectBg.width() / 6f / 3f * 1.2f,
            rectKeyboard.top + rectKeyboard.height() / 3f * 2f,
            rectKeyboard.centerX() - rectBg.width() / 6f / 3f * 1.2f + rectKeyboard.height(),
            rectKeyboard.top + rectKeyboard.height() / 3f * 2f
        )
        pathKeyboardTouch.lineTo(
            rectKeyboard.centerX() + rectBg.width() / 6f / 3f * 1.2f - rectKeyboard.height(),
            rectKeyboard.top + rectKeyboard.height() / 3f * 2f
        )
        pathKeyboardTouch.quadTo(
            rectKeyboard.centerX() + rectBg.width() / 6f / 3f * 1.2f,
            rectKeyboard.top + rectKeyboard.height() / 3f * 2f,
            rectKeyboard.centerX() + rectBg.width() / 6f / 3f * 1.2f,
            rectKeyboard.top
        )
        pathKeyboardTouch.close()
        mPaint.color = colorKeyboardTouch
        canvas.drawPath(pathKeyboardTouch, mPaint)
    }

    private fun drawKeyboardBottom(canvas: Canvas) {
        pathKeyboardBottom.reset()
        pathKeyboardBottom.moveTo(rectKeyboard.left, rectKeyboard.bottom)
        pathKeyboardBottom.quadTo(rectKeyboard.left, rectKeyboard.bottom + rectKeyboard.height() / 2f, rectScreen.left, rectKeyboard.bottom + rectKeyboard.height() / 2f)
        pathKeyboardBottom.lineTo(rectScreen.right, rectKeyboard.bottom + rectKeyboard.height() / 2f)
        pathKeyboardBottom.quadTo(rectKeyboard.right, rectKeyboard.bottom + rectKeyboard.height() / 2f, rectKeyboard.right, rectKeyboard.bottom)
        pathKeyboardBottom.close()
        canvas.drawPath(pathKeyboardBottom, mPaint)
    }

    private fun drawComputerShadow(canvas: Canvas) {
        pathComputerShadow.reset()
        pathComputerShadow.moveTo(rectScreen.left, rectKeyboard.bottom + rectKeyboard.height() / 2f)
        pathComputerShadow.quadTo(rectKeyboard.left, rectKeyboard.bottom + rectKeyboard.height() / 2f, rectKeyboard.left, rectBg.bottom + mPadding)
        pathComputerShadow.lineTo(rectKeyboard.right, rectBg.bottom + mPadding)
        pathComputerShadow.quadTo(rectKeyboard.right, rectKeyboard.bottom + rectKeyboard.height() / 2f, rectScreen.right, rectKeyboard.bottom + rectKeyboard.height() / 2f)
        pathComputerShadow.close()

        mShader = LinearGradient(
            rectKeyboard.centerX(),
            rectKeyboard.bottom + rectKeyboard.height() / 2f,
            rectKeyboard.centerX(),
            rectBg.bottom + mPadding,
            intArrayOf(Color.rgb(229, 230, 231), Color.rgb(245, 245, 245)),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
        mPaint.shader = mShader
        mPaint.color = Color.rgb(229, 230, 231)
        canvas.drawPath(pathComputerShadow, mPaint)
        mPaint.shader = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        if (mAnimatedValue >= 0) drawScreen(canvas)
        if (mAnimatedValue >= 1.0f / 11) drawScreenWithin(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 2) drawScreenShow(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 3) drawCamera(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 4) drawScreenReflective(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 5) drawKeyboard(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 6) drawKeyboardShadow(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 7) drawKeyboardTouch(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 8) drawKeyboardBottom(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 9) drawComputerShadow(canvas)
        if (mAnimatedValue >= 1.0f / 11 * 10 && mAnimatedValue <= 1.0f / 11 * 11) drawContent(canvas)
        canvas.restore()
    }

    protected open fun initPaint() {
        mPadding = dip2px(1f).toFloat()
        mPaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    protected fun setBitmapSize(iconId: Int, w: Int): Bitmap {
        var bitmap = BitmapFactory.decodeResource(context.resources, iconId)
        val s = w * 1.0f / bitmap.width
        bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * s).toInt(), (bitmap.height * s).toInt(), true)
        return bitmap
    }

    @Suppress("unused")
    private fun setBitmapRotation(bm: Bitmap, orientationDegree: Int): Bitmap? {
        val m = Matrix()
        m.setRotate(orientationDegree.toFloat(), bm.width.toFloat() / 2, bm.height.toFloat() / 2)
        return try {
            Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
        } catch (ex: OutOfMemoryError) {
            null
        }
    }

    fun startAnim(time: Int) {
        stopAnim()
        startViewAnim(0f, 1f, time.toLong())
    }

    fun stopAnim() {
        valueAnimator?.let {
            clearAnimation()
            it.repeatCount = 0
            it.cancel()
            it.end()
            mAnimatedValue = 1.0f
            postInvalidate()
        }
    }

    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator {
        valueAnimator = ValueAnimator.ofFloat(startF, endF).apply {
            duration = time
            interpolator = LinearInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.INFINITE
            addUpdateListener { valueAnimator ->
                mAnimatedValue = valueAnimator.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }

                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                }
            })
            if (!isRunning) {
                start()
            }
        }
        return valueAnimator!!
    }
}
