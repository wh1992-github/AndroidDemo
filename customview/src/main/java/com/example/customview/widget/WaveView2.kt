package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.Choreographer
import android.view.View
import kotlin.math.roundToInt

/**
 * 在标准正弦波的基础上增加细小波动和整体上下浮动。
 *
 * 最终波形由三部分相加：
 * 1. 主正弦波：决定主要的波峰和波谷；
 * 2. 次级正弦波：增加幅度较小、频率更高的连续波动；
 * 3. 整体浮动：让整条波浪沿垂直方向缓慢上下移动。
 *
 * 这里没有使用随机数，因此波形连续，不会出现锯齿或逐帧闪烁。
 */
open class WaveView2 : View {

    /** 主波振幅，单位为 px。 */
    private val amplitude = 30

    /** 次级波振幅，数值小于主波，避免波形过于杂乱。 */
    private val secondaryAmplitude = 8

    /** 整条波浪上下浮动的最大距离，单位为 px。 */
    private val verticalFloatAmplitude = 5

    /** 波浪中心线距离 View 顶部的位置，单位为 px。 */
    private val offsetY = 100

    /** 当前动画相位，单位为弧度。 */
    private var offsetX = 0F

    /** 波浪填充色，格式为 ARGB。 */
    private val waveColor = 0xaaFF7E37.toInt()

    /**
     * 主波角频率。当前值使一个屏幕宽度内出现两个完整的主波周期。
     * 次级波会在绘制时使用主波角频率的两倍。
     */
    private val omega = 2 * Math.PI / resources.displayMetrics.widthPixels * 2

    /** 绘制对象在所有帧之间复用，避免 onDraw() 中频繁创建对象。 */
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = waveColor
    }

    /** 标记帧回调是否已经提交，防止重复注册造成一帧更新多次。 */
    private var frameCallbackPosted = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postNextFrame()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeFrameCallback()
    }

    /** 请求下一次 VSYNC 回调。 */
    private fun postNextFrame() {
        if (!isAttachedToWindow || frameCallbackPosted) return

        Choreographer.getInstance().postFrameCallback(frameCallback)
        frameCallbackPosted = true
    }

    /** 移除尚未执行的回调，防止 View 离开窗口后继续刷新。 */
    private fun removeFrameCallback() {
        if (!frameCallbackPosted) return

        Choreographer.getInstance().removeFrameCallback(frameCallback)
        frameCallbackPosted = false
    }

    /**
     * 每次 VSYNC 到来时推进相位、请求重绘并预约下一帧。
     * 动画数据在帧回调中更新，onDraw() 只负责根据当前数据绘制。
     */
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCallbackPosted = false
            if (!isAttachedToWindow) return
            /** 每帧推进的相位，单位为弧度。 */
            offsetX += 0.02F
            offsetX = (offsetX * 100).roundToInt() / 100F
            /** 2π 约等于 6.28，一个完整正弦周期结束后相位归零。 */
            if (offsetX >= 2 * Math.PI) {
                offsetX = 0F
            }

            invalidate()
            postNextFrame()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.reset()

        var x = 0F
        while (x <= width) {
            // 主波：形成基础的大波峰和大波谷。
            val mainWave = amplitude * Math.sin(omega * x + offsetX + Math.PI / 2)

            // 次级波：频率是主波的两倍，并反向移动，产生柔和而连续的小波动。
            val secondaryWave = secondaryAmplitude * Math.sin(omega * 2 * x - offsetX * 2)

            // 所有采样点共用同一个偏移量，因此整条波浪会一起上下浮动。
            val verticalFloat = verticalFloatAmplitude * Math.sin(offsetX.toDouble())

            val y = (offsetY + mainWave + secondaryWave + verticalFloat).toFloat()
            path.lineTo(x, y)
            x += 20F
        }

        // 将波浪上边界与 View 底部连接，形成下方填充区域。
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0F, height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}
