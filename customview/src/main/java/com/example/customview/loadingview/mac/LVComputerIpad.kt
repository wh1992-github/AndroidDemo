package com.example.customview.loadingview.mac

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.example.customview.R

/**
 * Created by test on 16/7/9.
 */
open class LVComputerIpad @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVComputer(context, attrs, defStyleAttr) {

    @JvmField
    var colorHome = Color.rgb(125, 130, 135)

    init {
        initPaint()
    }

    private fun drawScreen(canvas: Canvas) {
        rectBg.top = 1f
        rectBg.left = 1f
        rectBg.right = mWidth - 1
        rectBg.bottom = mHigh - 1
        rectScreen.top = rectBg.top
        rectScreen.left = rectBg.left
        rectScreen.right = rectBg.right
        rectScreen.bottom = rectBg.bottom
        mPaint.color = Color.rgb(165, 165, 165)
        canvas.drawRoundRect(rectScreen, rectScreen.width() / 12f, rectScreen.width() / 12f, mPaint)
    }

    private fun drawScreenWithin(canvas: Canvas) {
        rectScreenWithin.top = rectScreen.top + 1
        rectScreenWithin.left = rectScreen.left + 1
        rectScreenWithin.right = rectScreen.right - 1
        rectScreenWithin.bottom = rectScreen.bottom - 1
        mPaint.color = colorScreenWithin
        canvas.drawRoundRect(rectScreenWithin, rectScreen.width() / 12f - 1, rectScreen.width() / 12f - 1, mPaint)
    }

    private fun drawContent(canvas: Canvas) {
        mPaint.color = Color.WHITE
        ios = setBitmapSize(R.drawable.apple, (rectScreen.width() / 5).toInt())
        canvas.drawBitmap(ios, rectScreenShow.centerX() - ios.width - 5, rectScreenShow.centerY() - ios.height / 2, mPaint)
        android = setBitmapSize(R.drawable.android, (rectScreenShow.width() / 5).toInt())
        canvas.drawBitmap(android, rectScreenShow.centerX() + 5, rectScreenShow.centerY() - android.height / 2, mPaint)
    }

    private fun drawCamera(canvas: Canvas) {
        mPaint.color = colorCamera
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2, 2f, mPaint)
        mPaint.color = colorCameraCenter
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2, 1f, mPaint)
        mPaint.color = colorHome
        canvas.drawCircle(
            rectScreen.centerX(),
            rectScreenShow.bottom + (rectScreen.width() / 12f - 1) * 1.5f / 2f,
            (rectScreen.width() / 12f - 1) / 2.5f,
            mPaint
        )
        mPaint.color = colorScreenWithin
        canvas.drawCircle(
            rectScreen.centerX(),
            rectScreenShow.bottom + (rectScreen.width() / 12f - 1) * 1.5f / 2f,
            (rectScreen.width() / 12f - 1) / 2.5f - 0.6f,
            mPaint
        )
    }

    private fun drawScreenShow(canvas: Canvas) {
        rectScreenShow.top = rectScreenWithin.top + (rectScreen.width() / 12f - 1) * 1.5f
        rectScreenShow.bottom = rectScreenWithin.bottom - (rectScreen.width() / 12f - 1) * 1.5f
        rectScreenShow.right = rectScreenWithin.right - (rectScreen.width() / 12f - 1) * 1f
        rectScreenShow.left = rectScreenWithin.left + (rectScreen.width() / 12f - 1) * 1f

        mPaint.color = colorScreenShow
        canvas.drawRect(rectScreenShow, mPaint)
    }

    private fun drawScreenReflective(canvas: Canvas) {
        pathScreenReflective.reset()
        pathScreenReflective.moveTo(rectScreen.left + rectScreen.width() / 10f * 5f, rectScreen.top)
        pathScreenReflective.lineTo(rectScreen.right - rectScreen.width() / 10f * 2.5f, rectScreen.bottom)
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.bottom)
        pathScreenReflective.lineTo(rectScreen.right, rectScreen.top)
        pathScreenReflective.close()
        mPaint.color = colorScreenReflective
        canvas.drawPath(pathScreenReflective, mPaint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        if (mAnimatedValue >= 0) {
            drawScreen(canvas)
        }
        if (mAnimatedValue >= 1.0f / 6 * 1) {
            drawScreenWithin(canvas)
        }
        if (mAnimatedValue >= 1.0f / 6 * 2) {
            drawScreenShow(canvas)
        }
        if (mAnimatedValue >= 1.0f / 6 * 3) {
            drawCamera(canvas)
        }
        if (mAnimatedValue >= 1.0f / 6 * 4) {
            drawScreenReflective(canvas)
        }
        if (mAnimatedValue >= 1.0f / 6 * 5 && mAnimatedValue <= 1.0f) {
            drawContent(canvas)
        }
        canvas.restore()
    }

    override fun initPaint() {
        super.initPaint()
    }
}
