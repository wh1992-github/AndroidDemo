package com.example.customview.loadingview

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import kotlin.math.abs

/**
 * Created by test on 16/6/24.
 */
class LVChromeLogo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var mPaintRed: Paint
    private lateinit var mPaintYellow: Paint
    private lateinit var mPaintGreen: Paint
    private lateinit var mPaintBlue: Paint
    private lateinit var mPaintWhite: Paint
    private lateinit var mPaintLine: Paint
    private var mWidth = 0f
    private var mPadding = 0f
    private lateinit var evaluator: ArgbEvaluator
    private var endColor = Color.rgb(0, 0, 0)
    private var startYellowColor = Color.argb(100, 253, 197, 53)
    private var startGreenColor = Color.argb(100, 27, 147, 76)
    private var startRedColor = Color.argb(100, 211, 57, 53)
    private var lineColor = 0
    private lateinit var mProgressRotateAnim: RotateAnimation

    init {
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) {
            measuredHeight.toFloat()
        } else {
            measuredWidth.toFloat()
        }
        mPadding = dip2px(1f).toFloat()
    }

    private fun drawSector(canvas: Canvas) {
        val rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        canvas.drawArc(rectF, -30f, 120f, true, mPaintYellow)
        canvas.drawArc(rectF, 90f, 120f, true, mPaintGreen)
        canvas.drawArc(rectF, 210f, 120f, true, mPaintRed)
    }

    private fun drawTriangle(canvas: Canvas) {
        val point1 = getPoint((mWidth / 2 - mPadding) / 2, 90f)
        val point2 = getPoint(mWidth / 2 - mPadding, 150f)
        val point3 = getPoint((mWidth / 2 - mPadding) / 2, 210f)
        val point4 = getPoint(mWidth / 2 - mPadding, 270f)
        val point5 = getPoint((mWidth / 2 - mPadding) / 2, 330f)
        val point6 = getPoint(mWidth / 2 - mPadding, 30f)

        val pathYellow = Path().apply {
            moveTo(mWidth / 2 - point1.x, mWidth / 2 - point1.y)
            lineTo(mWidth / 2 - point2.x, mWidth / 2 - point2.y)
            lineTo(mWidth / 2 - point3.x, mWidth / 2 - point3.y)
            close()
        }
        val pathGreen = Path().apply {
            moveTo(mWidth / 2 - point3.x, mWidth / 2 - point3.y)
            lineTo(mWidth / 2 - point4.x, mWidth / 2 - point4.y)
            lineTo(mWidth / 2 - point5.x, mWidth / 2 - point5.y)
            close()
        }
        val pathRed = Path().apply {
            moveTo(mWidth / 2 - point5.x, mWidth / 2 - point5.y)
            lineTo(mWidth / 2 - point6.x, mWidth / 2 - point6.y)
            lineTo(mWidth / 2 - point1.x, mWidth / 2 - point1.y)
            close()
        }
        canvas.drawPath(pathGreen, mPaintGreen)
        canvas.drawPath(pathRed, mPaintRed)
        canvas.drawPath(pathYellow, mPaintYellow)

        var i = 0
        while (i < abs(mWidth / 2 - point2.y) / 2f) {
            val fraction = 35 - i
            mPaintLine.color = if (fraction > 0) {
                lineColor = evaluator.evaluate(fraction / 100f, startYellowColor, endColor) as Int
                lineColor
            } else {
                Color.argb(0, 0, 0, 0)
            }
            canvas.drawLine(mWidth / 2, point2.y + i, mWidth / 2 - point2.x * 8f / 10f, mWidth / 2 - point2.y, mPaintLine)
            i++
        }

        i = 0
        while (i < abs(point3.x) / 2f) {
            val fraction = 35 - i
            mPaintLine.color = if (fraction > 0) {
                lineColor = evaluator.evaluate(fraction / 100f, startGreenColor, endColor) as Int
                lineColor
            } else {
                Color.argb(0, 0, 0, 0)
            }
            canvas.drawLine(mWidth / 2 - point3.x - i, mWidth / 2 - point3.y, mWidth / 2 - point4.x, mWidth / 2 - point4.y, mPaintLine)
            i++
        }

        i = 0
        while (i < abs(mWidth / 2 - point5.x) / 2f) {
            val fraction = 30 - i
            mPaintLine.color = if (fraction > 0) {
                lineColor = evaluator.evaluate(fraction / 100f, startRedColor, endColor) as Int
                lineColor
            } else {
                Color.argb(0, 0, 0, 0)
            }
            canvas.drawLine(mWidth / 2 - point5.x + i, mWidth / 2 - point5.y, mWidth / 2 - point6.x, mWidth / 2 - point6.y, mPaintLine)
            i++
        }
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, (mWidth / 2 - mPadding) / 2, mPaintWhite)
        canvas.drawCircle(mWidth / 2, mWidth / 2, (mWidth / 2 - mPadding) / 2 / 6 * 5, mPaintBlue)
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        canvas.save()
        drawSector(canvas)
        drawTriangle(canvas)
        drawCircle(canvas)
        canvas.restore()
    }

    fun startAnim() {
        stopAnim()
        mProgressRotateAnim.duration = 1500
        startAnimation(mProgressRotateAnim)
    }

    fun startAnim(time: Int) {
        stopAnim()
        mProgressRotateAnim.duration = time.toLong()
        startAnimation(mProgressRotateAnim)
    }

    fun stopAnim() {
        clearAnimation()
    }

    private class Point(val x: Float, val y: Float)

    private fun getPoint(radius: Float, angle: Float): Point {
        val x = (radius * Math.cos(angle * Math.PI / 180f)).toFloat()
        val y = (radius * Math.sin(angle * Math.PI / 180f)).toFloat()
        return Point(x, y)
    }

    private fun initPaint() {
        evaluator = ArgbEvaluator()
        mPaintRed = fillPaint(Color.rgb(211, 57, 53))
        mPaintYellow = fillPaint(Color.rgb(253, 197, 53))
        mPaintGreen = fillPaint(Color.rgb(27, 147, 76))
        mPaintBlue = fillPaint(Color.rgb(61, 117, 242))
        mPaintWhite = fillPaint(Color.WHITE)
        mPaintLine = fillPaint(Color.argb(30, 0, 0, 0))
        mProgressRotateAnim = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            repeatCount = -1
            interpolator = LinearInterpolator()
            fillAfter = true
        }
    }

    private fun fillPaint(color: Int): Paint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            this.color = color
        }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
