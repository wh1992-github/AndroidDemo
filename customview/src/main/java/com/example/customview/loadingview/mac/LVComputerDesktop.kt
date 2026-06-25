package com.example.customview.loadingview.mac

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import com.example.customview.R

/**
 * Created by test on 16/7/8.
 */
open class LVComputerDesktop @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVComputer(context, attrs, defStyleAttr) {

    @JvmField
    var rectHost = RectF()

    @JvmField
    var rectSupport = RectF()

    @JvmField
    var rectBottom = RectF()

    @JvmField
    var pathSupport = Path()

    init {
        initPaint()
    }

    private fun drawScreen(canvas: Canvas) {
        rectBg.top = mPadding
        rectBg.left = mPadding
        rectBg.right = mWidth - mPadding
        rectBg.bottom = mHigh - mHigh / 5
        rectScreen.top = rectBg.top
        rectScreen.left = rectBg.left + rectBg.width() / 6f
        rectScreen.right = rectBg.right - rectBg.width() / 6f
        rectScreen.bottom = rectBg.bottom
        mPaint.color = colorScreenWithin
        canvas.drawRoundRect(rectScreen, rectBg.width() / 6f / 6f, rectBg.width() / 6f / 6f, mPaint)
    }

    private fun drawHost(canvas: Canvas) {
        rectHost.top = rectScreen.height() / 6 * 4 + 2
        rectHost.bottom = rectScreen.bottom + 2
        rectHost.left = rectScreen.left
        rectHost.right = rectScreen.right

        mShader = LinearGradient(
            rectHost.left,
            rectHost.bottom,
            rectHost.right,
            rectHost.bottom,
            intArrayOf(Color.rgb(176, 177, 177), Color.rgb(226, 227, 229), Color.rgb(226, 227, 229)),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        mPaint.color = Color.rgb(176, 177, 177)
        mPaint.shader = mShader
        canvas.drawRoundRect(rectHost, rectBg.width() / 6 / 6f, rectBg.width() / 6 / 6f, mPaint)
        mPaint.shader = null

        rectScreenWithin.top = rectHost.top
        rectScreenWithin.left = rectHost.left
        rectScreenWithin.right = rectHost.right
        rectScreenWithin.bottom = rectHost.top + rectHost.height() / 2f
        mPaint.color = Color.rgb(0, 0, 0)
        canvas.drawRect(rectScreenWithin, mPaint)
    }

    private fun drawLogo(canvas: Canvas) {
        ios = setBitmapSize(R.drawable.apple_dark, (rectHost.height() / 4f).toInt())
        canvas.drawBitmap(ios, rectScreenWithin.centerX() - ios.width / 2f, rectScreenWithin.bottom + rectHost.height() / 4f - ios.height / 2, mPaint)
    }

    private fun drawScreenShow(canvas: Canvas) {
        rectScreenShow.top = rectScreen.top + rectBg.width() / 6 / 6f
        rectScreenShow.left = rectScreen.left + rectBg.width() / 6 / 6f
        rectScreenShow.right = rectScreen.right - rectBg.width() / 6 / 6f
        rectScreenShow.bottom = rectScreenWithin.bottom - rectBg.width() / 6 / 6f
        mPaint.color = colorScreenShow
        canvas.drawRect(rectScreenShow, mPaint)
    }

    private fun drawCamera(canvas: Canvas) {
        mPaint.color = colorCamera
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 4f, mPaint)
        mPaint.color = colorCameraCenter
        canvas.drawCircle(rectScreen.centerX(), rectScreenShow.top / 2 + 4, 2f, mPaint)
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

    private fun drawSupport(canvas: Canvas) {
        pathSupport.reset()
        rectSupport.top = rectScreen.bottom
        rectSupport.left = rectScreen.centerX() - rectScreen.width() / 9f
        rectSupport.right = rectScreen.centerX() + rectScreen.width() / 9f
        rectSupport.bottom = rectScreen.bottom + mHigh / 5 * 0.8f
        pathSupport.moveTo(rectSupport.left + 10, rectSupport.top)
        pathSupport.cubicTo(
            rectSupport.left + rectSupport.width() / 18f,
            rectSupport.top + rectSupport.height() * 2.5f / 3f,
            rectSupport.left + rectSupport.width() / 16f,
            rectSupport.top + rectSupport.height() * 2f / 3f,
            rectSupport.left - 30,
            rectSupport.bottom - 20
        )
        pathSupport.lineTo(rectSupport.right + 30, rectSupport.bottom - 20)
        pathSupport.cubicTo(
            rectSupport.right - rectSupport.width() / 16f,
            rectSupport.top + rectSupport.height() * 2f / 3f,
            rectSupport.right - rectSupport.width() / 18f,
            rectSupport.top + rectSupport.height() * 2.5f / 3f,
            rectSupport.right - 10,
            rectSupport.top
        )

        rectBottom.top = rectSupport.bottom - 20
        rectBottom.bottom = rectSupport.bottom - 20 + mHigh / 5 * 0.8f / 15f
        rectBottom.left = rectSupport.left - 30
        rectBottom.right = rectSupport.right + 30
        pathSupport.close()
        mShader = LinearGradient(
            rectSupport.centerX(),
            rectSupport.top,
            rectSupport.centerX(),
            rectSupport.bottom,
            intArrayOf(Color.rgb(190, 190, 190), Color.rgb(245, 245, 245), Color.rgb(245, 245, 245), Color.rgb(190, 190, 190), Color.rgb(245, 245, 245)),
            floatArrayOf(0f, 0.5f, 0.55f, 0.65f, 1f),
            Shader.TileMode.CLAMP
        )

        mPaint.color = Color.rgb(249, 249, 249)
        mPaint.shader = mShader
        canvas.drawPath(pathSupport, mPaint)
        mPaint.shader = null
    }

    private fun drawSupportBottom(canvas: Canvas) {
        pathSupport.reset()
        pathSupport.moveTo(rectBottom.left, rectBottom.top - 1)
        pathSupport.quadTo(rectBottom.left, rectBottom.bottom, rectBottom.left + 10, rectBottom.bottom)
        pathSupport.lineTo(rectBottom.right - 10, rectBottom.bottom)
        pathSupport.quadTo(rectBottom.right, rectBottom.bottom, rectBottom.right, rectBottom.top - 1)
        mShader = LinearGradient(
            rectBottom.centerX(),
            rectBottom.top,
            rectBottom.centerX(),
            rectBottom.bottom,
            intArrayOf(Color.rgb(230, 230, 230), Color.rgb(220, 220, 220), Color.rgb(209, 210, 211)),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        mPaint.color = Color.rgb(230, 230, 230)
        mPaint.shader = mShader
        canvas.drawPath(pathSupport, mPaint)
        mPaint.shader = null
    }

    private fun drawComputerShadow(canvas: Canvas) {
        pathComputerShadow.reset()
        pathComputerShadow.moveTo(rectBottom.left + 10, rectBottom.bottom)
        pathComputerShadow.quadTo(rectBottom.left + 30, rectBottom.bottom, rectBottom.left + 30, mHigh)
        pathComputerShadow.lineTo(rectBottom.right - 30, mHigh)
        pathComputerShadow.quadTo(rectBottom.right - 30, rectBottom.bottom, rectBottom.right - 10, rectBottom.bottom)

        mShader = LinearGradient(
            rectBottom.centerX(),
            rectBottom.bottom,
            rectBottom.centerX(),
            mHigh,
            intArrayOf(Color.rgb(235, 235, 235), Color.rgb(255, 255, 255)),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )

        mPaint.color = Color.rgb(245, 245, 245)
        mPaint.shader = mShader
        canvas.drawPath(pathComputerShadow, mPaint)
        mPaint.shader = null
    }

    private fun drawContent(canvas: Canvas) {
        ios = setBitmapSize(R.drawable.apple, (rectScreen.width() / 10).toInt())
        canvas.drawBitmap(ios, rectScreenShow.centerX() - ios.width - 5, rectScreenShow.centerY() - ios.height / 2, mPaint)
        android = setBitmapSize(R.drawable.android, (rectScreenShow.width() / 11).toInt())
        canvas.drawBitmap(android, rectScreenShow.centerX() + 5, rectScreenShow.centerY() - android.height / 2, mPaint)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        if (mAnimatedValue >= 0) {
            drawScreen(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10) {
            drawHost(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 2) {
            drawLogo(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 3) {
            drawScreenShow(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 4) {
            drawCamera(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 5) {
            drawScreenReflective(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 6) {
            drawSupport(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 7) {
            drawSupportBottom(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 8) {
            drawComputerShadow(canvas)
        }
        if (mAnimatedValue >= 1.0f / 10 * 9 && mAnimatedValue <= 1.0f) {
            drawContent(canvas)
        }
        canvas.restore()
    }

    override fun initPaint() {
        super.initPaint()
        mPadding = dip2px(2f).toFloat()
    }
}
