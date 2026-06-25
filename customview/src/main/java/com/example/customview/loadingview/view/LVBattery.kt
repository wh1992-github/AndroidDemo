package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Created by test on 16/6/27.
 */
class LVBattery @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHigh = 0f
    private var mPadding = 0f
    private var mBodyCorner = 0f
    private var mBatterySpace = 0f
    private lateinit var mPaint: Paint
    private lateinit var mPaintHead: Paint
    private lateinit var mPaintValue: Paint
    private lateinit var mPaintBattery: Paint
    private var mBatteryOrientation = BatteryOrientation.VERTICAL
    private lateinit var rectFBody: RectF
    private lateinit var rectHead: RectF
    private var mShowNum = false
    private var mAnimatedValue = 0f

    enum class BatteryOrientation {
        VERTICAL,
        HORIZONTAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > height) {
            mWidth = measuredHeight.toFloat()
            mHigh = measuredHeight * 0.8f
        } else {
            mWidth = measuredWidth.toFloat()
            mHigh = measuredWidth * 0.8f
        }
    }

    private fun drawHead(canvas: Canvas) {
        val mHeadWidth = mHigh / 6
        rectHead = RectF(
            mWidth - mPadding - mHeadWidth,
            mWidth / 2 - mHeadWidth / 2,
            mWidth - mPadding,
            mWidth / 2 + mHeadWidth / 2
        )
        canvas.drawArc(rectHead, -70f, 140f, false, mPaintHead)
    }

    private fun drawBody(canvas: Canvas) {
        val mHeadWidth = mHigh / 6
        val x = ((mHeadWidth / 2) * Math.cos(-70 * Math.PI / 180f)).toFloat()
        rectFBody = RectF().apply {
            top = mWidth / 2 - mHigh / 4 + mPadding
            bottom = mWidth / 2 + mHigh / 4 - mPadding
            left = mPadding
            right = mWidth - mPadding - x - x - mBatterySpace
        }
        canvas.drawRoundRect(rectFBody, mBodyCorner, mBodyCorner, mPaint)
    }

    private fun drawValue(canvas: Canvas) {
        val rectFBatteryValueFill = RectF().apply {
            top = rectFBody.top + mBatterySpace
            bottom = rectFBody.bottom - mBatterySpace
            left = mPadding + mBatterySpace
            right = rectFBody.right - mBatterySpace
        }
        val rectFBatteryValue = RectF().apply {
            top = rectFBatteryValueFill.top
            bottom = rectFBatteryValueFill.bottom
            left = rectFBatteryValueFill.left
            right = rectFBatteryValueFill.right * mAnimatedValue
        }

        canvas.drawRoundRect(rectFBatteryValue, 0f, 0f, mPaintValue)

        val battery = "${Math.round(100 * rectFBatteryValue.right / rectFBody.right)}%"
        canvas.drawText(battery, mWidth - 60, mHigh / 2, mPaintBattery)
    }

    private fun drawLogo(canvas: Canvas) {
        mPaintHead.textSize = mHigh / 6
        if (!mShowNum) {
            val path = Path().apply {
                moveTo(mWidth / 2 - mHigh / 6, mWidth / 2 - dip2px(1.5f))
                lineTo(mWidth / 2 + dip2px(2f), mWidth / 2 + mHigh / 12)
                lineTo(mWidth / 2 + dip2px(1f), mWidth / 2)
                close()
            }
            canvas.drawPath(path, mPaintHead)

            val path2 = Path().apply {
                moveTo(mWidth / 2 - dip2px(2f), mWidth / 2 - mHigh / 12)
                lineTo(mWidth / 2 + mHigh / 6, mWidth / 2 + dip2px(1.5f))
                lineTo(mWidth / 2 - dip2px(1f), mWidth / 2)
                close()
            }
            canvas.drawPath(path2, mPaintHead)
        } else {
            val text = "${(mAnimatedValue * 100).toInt()}%"
            if (mBatteryOrientation == BatteryOrientation.VERTICAL) {
                val p = Path().apply {
                    moveTo(mWidth / 2, 0f)
                    lineTo(mWidth / 2, mWidth)
                }
                canvas.drawTextOnPath(
                    text,
                    p,
                    mWidth / 2 - getFontLength(mPaintHead, text) / 2,
                    mWidth / 2 - mHigh / 2 - getFontHeight(mPaintHead, text) / 2,
                    mPaintHead
                )
            } else {
                canvas.drawText(
                    text,
                    mWidth / 2 - getFontLength(mPaintHead, text) / 2,
                    mWidth / 2 + getFontHeight(mPaintHead, text) / 2,
                    mPaintHead
                )
            }
        }
    }

    @Suppress("unused")
    private fun drawBattery() {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mBatteryOrientation == BatteryOrientation.VERTICAL) {
            canvas.rotate(0f, mWidth / 2, mWidth / 2)
        } else {
            canvas.rotate(90f, mWidth / 2, mWidth / 2)
        }
        canvas.save()
        drawHead(canvas)
        drawBody(canvas)
        drawValue(canvas)
        drawLogo(canvas)
        canvas.restore()
    }

    private fun initPaint() {
        mPadding = dip2px(2f).toFloat()
        mBodyCorner = dip2px(1f).toFloat()
        mBatterySpace = dip2px(1f).toFloat()
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
        mPaintHead = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        mPaintValue = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(67, 213, 81)
        }
        mPaintBattery = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 20f
            color = Color.RED
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        mPaintHead.color = color
        postInvalidate()
    }

    fun setCellColor(color: Int) {
        mPaintValue.color = color
        postInvalidate()
    }

    fun setValue(value: Int) {
        mAnimatedValue = value * 1f / 100
        invalidate()
    }

    fun setShowNum(show: Boolean) {
        mShowNum = show
        invalidate()
    }

    fun setBatteryOrientation(batteryOrientation: BatteryOrientation) {
        mBatteryOrientation = batteryOrientation
        invalidate()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0f
        postInvalidate()
        return 1
    }

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART
}
