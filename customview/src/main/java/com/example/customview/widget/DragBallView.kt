package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator

/**
 * Created by test on 2017/9/7.
 */
class DragBallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var circlePaint: Paint
    private lateinit var textPaint: Paint
    private var circleColor = Color.RED
    private var radiusStart = 0f
    private var radiusEnd = 0f
    private lateinit var path: Path
    private var startX = 0
    private var startY = 0
    private var mIsCanDrag = false
    private var isOutOfRang = false
    private var disappear = false
    private var maxDistance = 0f
    private lateinit var pointA: PointF
    private lateinit var pointB: PointF
    private lateinit var pointC: PointF
    private lateinit var pointD: PointF
    private lateinit var pointO: PointF
    private lateinit var pointStart: PointF
    private lateinit var pointEnd: PointF
    private var currentRadiusStart = 0f
    private var currentRadiusEnd = 0f
    private val textRect = Rect()
    private var msgCount = 0
    private var onDragBallListener: OnDragBallListener? = null

    init {
        initPaint()
        initPoint()
    }

    private fun initPoint() {
        pointStart = PointF(startX.toFloat(), startY.toFloat())
        pointEnd = PointF(startX.toFloat(), startY.toFloat())
        pointA = PointF()
        pointB = PointF()
        pointC = PointF()
        pointD = PointF()
        pointO = PointF()
    }

    private fun initPaint() {
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = circleColor
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }
        path = Path()
        initTextPaint()
    }

    private fun initTextPaint() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = sp2px(13).toFloat()
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startX = w / 2
        startY = h / 2
        maxDistance = dp2px(100).toFloat()
        radiusStart = dp2px(15).toFloat()
        radiusEnd = dp2px(15).toFloat()
        currentRadiusEnd = radiusEnd
        currentRadiusStart = radiusStart
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        pointStart.set(startX.toFloat(), startY.toFloat())
        if (isOutOfRang) {
            if (!disappear) {
                drawEndBall(canvas, pointEnd, currentRadiusEnd)
            }
        } else {
            drawStartBall(canvas, pointStart, currentRadiusStart)
            if (mIsCanDrag) {
                drawEndBall(canvas, pointEnd, currentRadiusEnd)
                drawBezier(canvas)
            }
        }

        if (!disappear && msgCount > 0) {
            if (pointEnd.x == 0f || pointEnd.y == 0f) {
                drawText(canvas, msgCount, pointStart)
            } else {
                drawText(canvas, msgCount, pointEnd)
            }
        }
    }

    private fun drawText(canvas: Canvas, msgCount: Int, point: PointF) {
        textRect.left = (point.x - radiusStart).toInt()
        textRect.top = (point.y - radiusStart).toInt()
        textRect.right = (point.x + radiusStart).toInt()
        textRect.bottom = (point.y + radiusStart).toInt()
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(if (msgCount > 99) "99+" else "$msgCount", textRect.centerX().toFloat(), baseline.toFloat(), textPaint)
    }

    private fun drawStartBall(canvas: Canvas, pointF: PointF, radius: Float) {
        canvas.drawCircle(pointF.x, pointF.y, radius, circlePaint)
    }

    private fun drawEndBall(canvas: Canvas, pointF: PointF, radius: Float) {
        canvas.drawCircle(pointF.x, pointF.y, radius, circlePaint)
    }

    private fun drawBezier(canvas: Canvas) {
        path.reset()
        path.moveTo(pointA.x, pointA.y)
        path.quadTo(pointO.x, pointO.y, pointB.x, pointB.y)
        path.lineTo(pointC.x, pointC.y)
        path.quadTo(pointO.x, pointO.y, pointD.x, pointD.y)
        path.lineTo(pointA.x, pointA.y)
        path.close()
        canvas.drawPath(path, circlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var currentX: Float
        var currentY: Float
        when (event.action) {
            MotionEvent.ACTION_DOWN -> setIsCanDrag(event)
            MotionEvent.ACTION_MOVE -> {
                if (mIsCanDrag) {
                    currentX = event.x
                    currentY = event.y
                    pointEnd.set(currentX, currentY)
                    if (!isOutOfRang) {
                        setCurrentRadius()
                        setABCDOPoint()
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mIsCanDrag) {
                    if (isOutOfRang) {
                        disappear = true
                        onDragBallListener?.onDisappear()
                        invalidate()
                    } else {
                        disappear = false
                        val a = (pointEnd.y - pointStart.y) / (pointEnd.x - pointStart.x)
                        ValueAnimator.ofFloat(pointEnd.x, pointStart.x).apply {
                            duration = 500
                            interpolator = BounceInterpolator()
                            addUpdateListener { animation ->
                                val x = animation.animatedValue as Float
                                val y = pointStart.y + a * (x - pointStart.x)
                                pointEnd.set(x, y)
                                setCurrentRadius()
                                setABCDOPoint()
                                invalidate()
                            }
                            start()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun setCurrentRadius() {
        val distance = Math.sqrt(
            Math.pow((pointStart.x - pointEnd.x).toDouble(), 2.0) +
                Math.pow((pointStart.y - pointEnd.y).toDouble(), 2.0)
        ).toFloat()

        if (distance <= maxDistance) {
            val percent = distance / maxDistance
            currentRadiusStart = (1 - percent * 0.6f) * radiusStart
            currentRadiusEnd = (1 + percent * 0.2f) * radiusEnd
            isOutOfRang = false
        } else {
            isOutOfRang = true
            currentRadiusStart = radiusStart
            currentRadiusEnd = radiusEnd
        }
    }

    private fun setIsCanDrag(event: MotionEvent) {
        val rect = Rect().apply {
            left = (startX - radiusStart).toInt()
            top = (startY - radiusStart).toInt()
            right = (startX + radiusStart).toInt()
            bottom = (startY + radiusStart).toInt()
        }
        mIsCanDrag = rect.contains(event.x.toInt(), event.y.toInt())
    }

    private fun setABCDOPoint() {
        pointO.set((pointStart.x + pointEnd.x) / 2.0f, (pointStart.y + pointEnd.y) / 2.0f)

        val x = pointEnd.x - pointStart.x
        val y = pointEnd.y - pointStart.y
        val rate = x / y
        val angle = Math.atan(rate.toDouble()).toFloat()

        pointA.x = (pointStart.x + Math.cos(angle.toDouble()) * currentRadiusStart).toFloat()
        pointA.y = (pointStart.y - Math.sin(angle.toDouble()) * currentRadiusStart).toFloat()
        pointB.x = (pointEnd.x + Math.cos(angle.toDouble()) * currentRadiusEnd).toFloat()
        pointB.y = (pointEnd.y - Math.sin(angle.toDouble()) * currentRadiusEnd).toFloat()
        pointC.x = (pointEnd.x - Math.cos(angle.toDouble()) * currentRadiusEnd).toFloat()
        pointC.y = (pointEnd.y + Math.sin(angle.toDouble()) * currentRadiusEnd).toFloat()
        pointD.x = (pointStart.x - Math.cos(angle.toDouble()) * currentRadiusStart).toFloat()
        pointD.y = (pointStart.y + Math.sin(angle.toDouble()) * currentRadiusStart).toFloat()
    }

    fun setMsgCount(count: Int) {
        msgCount = count
        invalidate()
    }

    fun reset() {
        msgCount = 0
        mIsCanDrag = false
        isOutOfRang = false
        disappear = false
        pointStart.set(startX.toFloat(), startY.toFloat())
        pointEnd.set(startX.toFloat(), startY.toFloat())
        setABCDOPoint()
        invalidate()
    }

    fun setOnDragBallListener(onDragBallListener: OnDragBallListener?) {
        this.onDragBallListener = onDragBallListener
    }

    fun interface OnDragBallListener {
        fun onDisappear()
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
}
