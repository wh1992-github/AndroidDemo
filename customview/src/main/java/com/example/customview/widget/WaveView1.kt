package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.View
import kotlin.math.roundToInt

/**
 * 使用正弦函数绘制并填充波浪。
 *
 * 波形公式：y = amplitude × sin(omega × x + offsetX + π / 2) + offsetY。
 * [Choreographer] 在每次屏幕 VSYNC 到来时推进 [offsetX] 并请求重绘，
 * [onDraw] 只根据当前数据生成路径，避免普通重绘意外推进动画。
 */
class WaveView1 : View {

    /** 振幅，单位为 px；波峰到波谷的高度等于它的两倍。 */
    private val amplitude = 30

    /** 水平相位，单位为弧度；持续增大时波形会沿水平方向移动。 */
    private var offsetX = 0F

    /** 波浪中心线距离 View 顶部的位置，单位为 px。 */
    private val offsetY = 100

    /** 波浪填充色，格式为 ARGB。 */
    private val waveColor = 0xaaFF7E37.toInt()

    /**
     * 角频率，决定水平方向的波浪数量。
     * 当前值使一个屏幕宽度内包含两个完整周期。
     */
    private var omega = 2 * Math.PI / resources.displayMetrics.widthPixels * 2

    /** 绘制对象在所有帧之间复用，避免 onDraw() 中频繁创建对象。 */
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = waveColor
    }

    /** 标记帧回调是否已经提交，防止同一帧被重复更新。 */
    private var frameCallbackPosted = false

    companion object {
        private const val TAG = "WaveView1"
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // View 进入窗口后自动开始接收帧回调。
        postNextFrame()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // View 离开窗口后停止刷新，避免无意义计算及 View 引用泄漏。
        removeFrameCallback()
    }

    /** 请求下一次 VSYNC 帧回调，并防止重复提交同一个回调。 */
    private fun postNextFrame() {
        if (isAttachedToWindow && !frameCallbackPosted) {
            Choreographer.getInstance().postFrameCallback(mFrameCallback)
            frameCallbackPosted = true
        }
    }

    /** 移除尚未执行的帧回调。 */
    private fun removeFrameCallback() {
        if (frameCallbackPosted) {
            Choreographer.getInstance().removeFrameCallback(mFrameCallback)
            frameCallbackPosted = false
        }
    }

    /**
     * 系统准备绘制新一帧时执行：更新水平相位、请求重绘，再预约下一帧。
     * [frameTimeNanos] 是系统提供的帧时间戳；当前动画采用固定相位步长，因此暂不参与计算。
     */
    private val mFrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCallbackPosted = false
            if (!isAttachedToWindow) return

            // 每帧推进 0.02 弧度，再四舍五入到两位小数，避免 Float 连续累加产生明显尾差。
            offsetX += 0.02F
            offsetX = (offsetX * 100).roundToInt() / 100F
            // 一个完整正弦周期是 2π；完成一轮后归零，防止数值无限增大。
            if (offsetX >= 2 * Math.PI) {
                offsetX = 0F
            }
            Log.i(TAG, "offsetX = $offsetX")

            invalidate()
            postNextFrame()
        }
    }

    /** 根据当前相位生成波浪上边界，并封闭路径以填充波浪下方区域。 */
    override fun onDraw(canvas: Canvas) {
        path.reset()
        var x = 0f
        while (x <= width) {
            // π/2 让初始波形从波峰开始；每隔 20px 取一个波形采样点。
            val y = (amplitude * Math.sin(omega * x + offsetX + Math.PI / 2) + offsetY).toFloat()
            path.lineTo(x, y)
            x += 20f
        }

        // 连接到底部两个角并闭合路径，使波浪曲线以下的区域得到填充。
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}
