package com.example.customview.loadingview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.Locale

/**
 * 用于展示 LV Sun Set 效果的自定义 View。
 */
class LVSunSetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var sunAngle = 12
    private var sunStartTime = "05:38"
    private var sunEndTime = "18:16"
    private lateinit var mPaint: Paint
    private var mWidth = 0
    private var mHeight = 0
    private var valueAnimator: ValueAnimator? = null
    private var mAnimatedValue = 0f

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                mAnimatedValue = 0f
                invalidate()
            } else if (msg.what == 1) {
                start()
            }
        }
    }

    fun setSunstartTime(sunstartTime: String) {
        sunStartTime = sunstartTime
        invalidate()
    }

    fun setSunendTime(sunendTime: String) {
        sunEndTime = sunendTime
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        initPaint()
        canvas.drawLine(
            mWidth / 12f,
            mHeight - mWidth / 6f,
            mWidth - mWidth / 12f,
            mHeight - mWidth / 6f,
            mPaint
        )

        mPaint.style = Paint.Style.FILL
        mPaint.textSize = 35f
        mPaint.strokeWidth = 1f
        val startTime = getTimeText(sunStartTime)
        val endTime = getTimeText(sunEndTime)
        val rect = Rect()
        mPaint.getTextBounds(startTime, 0, startTime.length, rect)
        val w = rect.width()
        val h = rect.height() * 2
        canvas.drawText(startTime, mWidth / 6f - w / 2f, mHeight - mWidth / 6f + h, mPaint)
        canvas.drawText(endTime, mWidth - mWidth / 6f - w / 2f, mHeight - mWidth / 6f + h, mPaint)

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2.5f
        mPaint.pathEffect = DashPathEffect(floatArrayOf(14f, 12f), 0f)

        val oval1 = getSunPathRect()
        canvas.drawArc(oval1, (180 + sunAngle).toFloat(), (180 - 2 * sunAngle).toFloat(), false, mPaint)

        /*
         * 这是演示动画，绘制进度只由 ValueAnimator 决定。
         * 不再使用“2019 年日出时间”和当前系统日期计算，否则年份不同会让
         * intervalf 第一帧就超过 1，太阳直接跳到终点，看起来像没有动画。
         */
        val intervalf = mAnimatedValue.coerceIn(0f, 1f)
        canvas.drawBitmap(getSunbg(intervalf), 0f, 0f, mPaint)
        canvas.restore()
    }

    fun startSunset() {
        mHandler.obtainMessage(0).sendToTarget()
        val m = Message().apply {
            what = 1
        }
        mHandler.sendMessageDelayed(m, 200)
    }

    fun start() {
        stopAnim()
        startViewAnim(0f, 1f, SUNSET_ANIMATION_DURATION_MS)
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
            strokeWidth = 4f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = measuredHeight
        mWidth = measuredWidth
    }

    fun stopAnim() {
        valueAnimator?.let {
            clearAnimation()
            it.repeatCount = 0
            it.cancel()
            mAnimatedValue = 0f
            postInvalidate()
        }
    }

    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator {
        valueAnimator = ValueAnimator.ofFloat(startF, endF).apply {
            duration = time
            interpolator = LinearInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.RESTART
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

    private fun getTimeText(value: String): String {
        // 使用最后一个空格后的内容，同时兼容纯时间和旧的完整日期时间输入。
        val parts = value
            .trim()
            .substringAfterLast(' ')
            .split(':')
        val hour = parts.getOrNull(0)?.toIntOrNull()
        val minute = parts.getOrNull(1)?.toIntOrNull()

        if (hour == null || minute == null ||
            hour !in 0..23 || minute !in 0..59
        ) {
            return "00:00"
        }

        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            hour,
            minute
        )
    }

    private fun getSunbg(intervalf: Float): Bitmap {
        val b = Bitmap.createBitmap(mWidth, mHeight - mWidth / 6, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.save()

        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.argb(255, 254, 219, 57)
        if (intervalf < 1f) {
            c.drawArc(getSunPathRect(), (180 + sunAngle).toFloat(), (180 - 2 * sunAngle) * intervalf, false, mPaint)
        }

        mPaint.style = Paint.Style.FILL
        val y = (Math.sin(Math.toRadians((sunAngle + (180 - 2 * sunAngle) * intervalf).toDouble())) * mWidth / 3).toFloat()
        val x = (Math.cos(Math.toRadians((sunAngle + (180 - 2 * sunAngle) * intervalf).toDouble())) * mWidth / 3).toFloat()
        c.drawBitmap(
            getSun(intervalf),
            mWidth / 2f - x - mWidth / 3f / 4f,
            (mHeight - mWidth / 6f - y - mWidth / 3f / 4f + Math.sin(Math.toRadians(sunAngle.toDouble())) * mWidth / 3f).toFloat(),
            mPaint
        )
        c.restore()
        return b
    }

    private fun getSun(intervalf: Float): Bitmap {
        val b = Bitmap.createBitmap(mWidth / 3 / 2, mWidth / 3 / 2, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.save()
        mPaint.style = Paint.Style.FILL
        c.rotate(intervalf * 180, mWidth / 3f / 4f, mWidth / 3f / 4f)
        if (intervalf in 0.3f..0.7f) {
            mPaint.color = Color.argb(255, 254, 219, 57)
        }
        if (intervalf < 0.3f) {
            val colorap = intervalf / 0.3f
            val suncolor = (230 + 25 * colorap).toInt()
            mPaint.color = Color.argb(suncolor, 254, 219, 57)
        } else if (intervalf > 0.7f) {
            val colorap = (1f - intervalf) / 0.3f
            val suncolor = (230 + 25 * colorap).toInt()
            mPaint.color = Color.argb(suncolor, 254, 219, 57)
        }

        when {
            intervalf > 0f && intervalf < 1f -> drawFullSun(c)
            intervalf == 0f -> drawHalfSun(c, true)
            intervalf == 1f -> drawHalfSun(c, false)
        }
        c.restore()
        return b
    }

    private fun getSunPathRect(): RectF =
        RectF(
            mWidth / 2f - mWidth / 3f,
            (mHeight - mWidth / 6f - mWidth / 3f + Math.sin(Math.toRadians(sunAngle.toDouble())) * mWidth / 3f).toFloat(),
            mWidth / 2f + mWidth / 3f,
            (mHeight - mWidth / 6f + mWidth / 3f + Math.sin(Math.toRadians(sunAngle.toDouble())) * mWidth / 3f).toFloat()
        )

    private fun drawFullSun(canvas: Canvas) {
        val center = mWidth / 3f / 4f
        val radius = mWidth / 3f / 10f
        val gap = radius / 5f
        val ray = radius / 2f
        canvas.drawCircle(center, center, radius, mPaint)
        mPaint.strokeWidth = 5f
        drawRay(canvas, center, center, -1f, 0f, radius, gap, ray)
        drawRay(canvas, center, center, 1f, 0f, radius, gap, ray)
        drawRay(canvas, center, center, 0f, -1f, radius, gap, ray)
        drawRay(canvas, center, center, 0f, 1f, radius, gap, ray)
        val d = (radius + gap) / Math.sqrt(2.0).toFloat()
        val d2 = (radius + gap + ray) / Math.sqrt(2.0).toFloat()
        canvas.drawLine(center - d, center - d, center - d2, center - d2, mPaint)
        canvas.drawLine(center + d, center + d, center + d2, center + d2, mPaint)
        canvas.drawLine(center - d, center + d, center - d2, center + d2, mPaint)
        canvas.drawLine(center + d, center - d, center + d2, center - d2, mPaint)
    }

    private fun drawHalfSun(canvas: Canvas, rise: Boolean) {
        val center = mWidth / 3f / 4f
        val radius = mWidth / 3f / 10f
        val oval = RectF(center - radius, center - radius, center + radius, center + radius)
        canvas.drawArc(oval, 180f, if (rise) 180f else -180f, true, mPaint)
        mPaint.strokeWidth = 5f
        val gap = radius / 5f
        val ray = radius / 2f
        drawRay(canvas, center, center, -1f, 0f, radius, gap, ray)
        drawRay(canvas, center, center, 1f, 0f, radius, gap, ray)
        drawRay(canvas, center, center, 0f, if (rise) -1f else 1f, radius, gap, ray)
        val d = (radius + gap) / Math.sqrt(2.0).toFloat()
        val d2 = (radius + gap + ray) / Math.sqrt(2.0).toFloat()
        if (rise) {
            canvas.drawLine(center + d, center - d, center + d2, center - d2, mPaint)
            canvas.drawLine(center - d, center - d, center - d2, center - d2, mPaint)
        } else {
            canvas.drawLine(center - d, center + d, center - d2, center + d2, mPaint)
            canvas.drawLine(center + d, center + d, center + d2, center + d2, mPaint)
        }
    }

    private fun drawRay(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        dx: Float,
        dy: Float,
        radius: Float,
        gap: Float,
        ray: Float
    ) {
        canvas.drawLine(
            cx + dx * (radius + gap),
            cy + dy * (radius + gap),
            cx + dx * (radius + gap + ray),
            cy + dy * (radius + gap + ray),
            mPaint
        )
    }

    override fun onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(null)
        stopAnim()
        super.onDetachedFromWindow()
    }

    private companion object {
        const val SUNSET_ANIMATION_DURATION_MS = 4_000L
    }
}
