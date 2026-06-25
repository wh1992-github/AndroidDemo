package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Created by test on 16/6/24.
 */
class LVFinePoiStar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mPadding = 0f
    private lateinit var mPaintLine: Paint
    private lateinit var mPaintCircle: Paint
    private var hornCount = 5
    private val listPoint: MutableList<Point> = ArrayList()
    private var isDrawPath = true
    private var rectF = RectF()
    private var mAnimatedValue = 0.75f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) {
            measuredHeight.toFloat()
        } else {
            measuredWidth.toFloat()
        }
        mPadding = dip2px(1f).toFloat()
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        listPoint.clear()
        for (i in 0 until hornCount) {
            val p = getPoint(mWidth / 2 - mPadding, (90 - 360 / hornCount + 360 / hornCount * i).toFloat())
            listPoint.add(p)
        }

        val currenttime = mAnimatedValue * 10 - (mAnimatedValue * 10).toInt()
        if (mAnimatedValue >= 0 && mAnimatedValue <= 0.1f) {
            val cp = drawOneEdge(currenttime, 1f, listPoint[0], listPoint[2])
            if (isDrawPath) {
                drawPathEdge(canvas, listPoint[0], cp)
            } else {
                canvas.drawCircle(mWidth / 2 - cp.x, mWidth / 2 - cp.y, mPadding, mPaintLine)
            }
        } else if (mAnimatedValue > 0.1f && mAnimatedValue <= 0.2f) {
            val cp = drawOneEdge(currenttime, 1f, listPoint[2], listPoint[4])
            if (isDrawPath) {
                drawEdge(canvas, 1)
                drawPathEdge(canvas, listPoint[2], cp)
            } else {
                canvas.drawCircle(mWidth / 2 - cp.x, mWidth / 2 - cp.y, mPadding, mPaintLine)
            }
        } else if (mAnimatedValue > 0.2f && mAnimatedValue <= 0.3f) {
            val cp = drawOneEdge(currenttime, 1f, listPoint[4], listPoint[1])
            if (isDrawPath) {
                drawEdge(canvas, 2)
                drawPathEdge(canvas, listPoint[4], cp)
            } else {
                canvas.drawCircle(mWidth / 2 - cp.x, mWidth / 2 - cp.y, mPadding, mPaintLine)
            }
        } else if (mAnimatedValue > 0.3f && mAnimatedValue <= 0.4f) {
            val cp = drawOneEdge(currenttime, 1f, listPoint[1], listPoint[3])
            if (isDrawPath) {
                drawEdge(canvas, 3)
                drawPathEdge(canvas, listPoint[1], cp)
            } else {
                canvas.drawCircle(mWidth / 2 - cp.x, mWidth / 2 - cp.y, mPadding, mPaintLine)
            }
        } else if (mAnimatedValue > 0.4f && mAnimatedValue <= 0.5f) {
            val cp = drawOneEdge(currenttime, 1f, listPoint[3], listPoint[0])
            if (isDrawPath) {
                drawEdge(canvas, 4)
                drawPathEdge(canvas, listPoint[3], cp)
            } else {
                canvas.drawCircle(mWidth / 2 - cp.x, mWidth / 2 - cp.y, mPadding, mPaintLine)
            }
        } else if (mAnimatedValue > 0.5f && mAnimatedValue <= 0.75f) {
            drawEdge(canvas, 5)
            rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
            canvas.drawArc(
                rectF,
                (-180 + (90 - 360 / hornCount)).toFloat(),
                360 / 0.25f * (mAnimatedValue - 0.5f),
                false,
                mPaintCircle
            )
        } else {
            mPaintCircle.strokeWidth = dip2px(1.5f).toFloat()
            mPaintLine.setShadowLayer(1f, 1f, 1f, Color.WHITE)
            drawEdge(canvas, 5)
            rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
            canvas.drawArc(rectF, (-180 + (90 - 360 / hornCount)).toFloat(), 360f, false, mPaintCircle)
        }
        mPaintCircle.strokeWidth = dip2px(1.0f).toFloat()
        mPaintLine.setShadowLayer(0f, 1f, 1f, Color.WHITE)
    }

    private fun drawOneEdge(currenttime: Float, alltime: Float, startP: Point, endP: Point): Point {
        val x = startP.x - (startP.x - endP.x) / alltime * currenttime
        val y = startP.y - (startP.y - endP.y) / alltime * currenttime
        return Point(x, y)
    }

    private fun drawFirstEdge(canvas: Canvas) {
        canvas.drawLine(mWidth / 2 - listPoint[0].x, mWidth / 2 - listPoint[0].y, mWidth / 2 - listPoint[2].x, mWidth / 2 - listPoint[2].y, mPaintLine)
    }

    private fun drawSecondEdge(canvas: Canvas) {
        canvas.drawLine(mWidth / 2 - listPoint[2].x, mWidth / 2 - listPoint[2].y, mWidth / 2 - listPoint[4].x, mWidth / 2 - listPoint[4].y, mPaintLine)
    }

    private fun drawThirdEdge(canvas: Canvas) {
        canvas.drawLine(mWidth / 2 - listPoint[4].x, mWidth / 2 - listPoint[4].y, mWidth / 2 - listPoint[1].x, mWidth / 2 - listPoint[1].y, mPaintLine)
    }

    private fun drawFourthEdge(canvas: Canvas) {
        canvas.drawLine(mWidth / 2 - listPoint[1].x, mWidth / 2 - listPoint[1].y, mWidth / 2 - listPoint[3].x, mWidth / 2 - listPoint[3].y, mPaintLine)
    }

    private fun drawFifthEdge(canvas: Canvas) {
        canvas.drawLine(mWidth / 2 - listPoint[3].x, mWidth / 2 - listPoint[3].y, mWidth / 2 - listPoint[0].x, mWidth / 2 - listPoint[0].y, mPaintLine)
    }

    private fun drawPathEdge(canvas: Canvas, start: Point, end: Point) {
        canvas.drawLine(mWidth / 2 - start.x, mWidth / 2 - start.y, mWidth / 2 - end.x, mWidth / 2 - end.y, mPaintLine)
    }

    private fun drawEdge(canvas: Canvas, edgeCount: Int) {
        when (edgeCount) {
            1 -> drawFirstEdge(canvas)
            2 -> {
                drawFirstEdge(canvas)
                drawSecondEdge(canvas)
            }
            3 -> {
                drawFirstEdge(canvas)
                drawSecondEdge(canvas)
                drawThirdEdge(canvas)
            }
            4 -> {
                drawFirstEdge(canvas)
                drawSecondEdge(canvas)
                drawThirdEdge(canvas)
                drawFourthEdge(canvas)
            }
            5 -> {
                drawFirstEdge(canvas)
                drawSecondEdge(canvas)
                drawThirdEdge(canvas)
                drawFourthEdge(canvas)
                drawFifthEdge(canvas)
            }
        }
    }

    private fun initPaint() {
        mPaintLine = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
            strokeWidth = dip2px(1f).toFloat()
        }
        mPaintCircle = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = dip2px(1f).toFloat()
        }
    }

    fun setViewColor(color: Int) {
        mPaintLine.color = color
        postInvalidate()
    }

    fun setCircleColor(color: Int) {
        mPaintCircle.color = color
        postInvalidate()
    }

    private fun getPoint(radius: Float, angle: Float): Point {
        val x = (radius * Math.cos(angle * Math.PI / 180f)).toFloat()
        val y = (radius * Math.sin(angle * Math.PI / 180f)).toFloat()
        return Point(x, y)
    }

    private class Point(val x: Float, val y: Float)

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0.75f
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    fun setDrawPath(isDrawPath: Boolean) {
        this.isDrawPath = isDrawPath
    }

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
