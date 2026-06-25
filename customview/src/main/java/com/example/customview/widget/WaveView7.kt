package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.Choreographer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * 使用正弦函数绘制并填充波浪。
 *
 * 波形公式：y = amplitude × sin(omega × x + offsetX + π / 2) + offsetY。
 * [Choreographer] 在每次屏幕 VSYNC 到来时推进 [offsetX] 并请求重绘，
 * [onDraw] 只根据当前数据生成路径，避免普通重绘意外推进动画。
 */
class WaveView7 : View {

    private var eventX = 0f
    private var eventY = 0f
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // 颜色渐变
    private val LEFT_GRADIENT_COLORS = intArrayOf(
        Color.parseColor("#FF00FF00"),Color.parseColor("#FF00FF00"),Color.parseColor("#FF00FF00"),Color.parseColor("#FF00FF00"),Color.parseColor("#FF00FF00"),
//        Color.parseColor("#FF448DD4"),
//        Color.parseColor("#FF104392"),
//        Color.parseColor("#FF4F61A1"),
//        Color.parseColor("#FFFF765C"),
//        Color.parseColor("#FF8B7186")
    )

    private val RIGHT_GRADIENT_COLORS = intArrayOf(
        Color.parseColor("#FF0000FF"),Color.parseColor("#FF0000FF"),Color.parseColor("#FF0000FF"),Color.parseColor("#FF0000FF"),Color.parseColor("#FF0000FF"),
//        Color.parseColor("#FF8B7186"),
//        Color.parseColor("#FFFF765C"),
//        Color.parseColor("#FF4F61A1"),
//        Color.parseColor("#FF104392"),
//        Color.parseColor("#FF448DD4")
    )

    // 与日/夜渐变色数组一一对应的颜色停靠点
    private val GRADIENT_POSITIONS = floatArrayOf(0f, 0.20f, 0.50f, 0.83f, 1f)

    /** 横向渐变 shader。View 初始化阶段 width 还没有经过测量，不能在此时创建。 */
    private var leftShader: LinearGradient? = null
    private var rightShader: LinearGradient? = null

    private var mMatrix = Matrix();

    private val leftPath = Path()
    private val rightPath = Path()

    /** 标记帧回调是否已经提交，防止同一帧被重复更新。 */
    private var frameCallbackPosted = false

    companion object {
        private const val TAG = "WaveView7"

        // 波浪光效的固定模糊半径
        private const val WAKE_UP_BLUR_RADIUS = 140F
        // 发话态模糊效果的 Y 轴移动距离
        private const val SPEAKING_BLUR_OFFSET_Y = 40f
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
        drawBlurEffect()
//        startAnimator()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // View 离开窗口后停止刷新，避免无意义计算及 View 引用泄漏。
        removeFrameCallback()
        stopAnimator()
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
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startX = 0f
        startY = 0f
        endX = w * 0.6f
        endY = 0f
        eventX = endX * 0.50f
        eventY = 120f

        // 使用测量后的实际宽度创建横向渐变；尺寸变化时同步重建。
        leftShader = if (w > 0) {
            LinearGradient(
                0f,
                0f,
                endX,
                0f,
                LEFT_GRADIENT_COLORS,
                GRADIENT_POSITIONS,
                Shader.TileMode.MIRROR
            )
        } else null
        rightShader = if (w > 0) {
            LinearGradient(
                w * 0.4f,
                0f,
                w.toFloat(),
                0f,
                RIGHT_GRADIENT_COLORS,
                GRADIENT_POSITIONS,
                Shader.TileMode.MIRROR
            )
        } else null
    }

    private var leftFlag = false

    private val mFrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCallbackPosted = false
            if (!isAttachedToWindow) return

            invalidate()
            postNextFrame()
        }
    }

    /** 根据当前相位生成波浪上边界，并封闭路径以填充波浪下方区域。 */
    override fun onDraw(canvas: Canvas) {
        val viewWidth = width.toFloat()
        if (viewWidth <= 0f) return

        val leftStartX = 0f
        val leftEndX = viewWidth * 0.6f
        val rightStartX = viewWidth * 0.4f
        val rightEndX = viewWidth
        val leftMinEventX = leftEndX * 0.50f
        val leftMaxEventX = leftEndX * 0.75f

        if (eventX >= leftMaxEventX && leftFlag) {
            leftFlag = false
        } else if (eventX <= leftMinEventX && !leftFlag) {
            leftFlag = true
        }

        if (leftFlag) {
            eventX += 1f
        } else {
            eventX -= 1f
        }

        val rightEventX = rightStartX + (leftEndX - eventX)
        //画贝塞尔曲线
        paint.setStyle(Paint.Style.FILL)
        drawWave(canvas, leftPath, leftShader, leftStartX, startY, leftEndX, endY, eventX, eventY)
        drawWave(canvas, rightPath, rightShader, rightStartX, startY, rightEndX, endY, rightEventX, eventY)

        //画参照点
        paint.shader = null
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
//        drawDebugPoints(canvas, leftStartX, leftEndX, eventX, startY, endY, eventY)
        paint.shader = null
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
//        drawDebugPoints(canvas, rightStartX, rightEndX, rightEventX, startY, endY, eventY)
    }

    private fun drawWave(
        canvas: Canvas,
        path: Path,
        shader: LinearGradient?,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        eventX: Float,
        eventY: Float
    ) {
        val leftLength = eventX - startX
        val rightLength = endX - eventX
        val leftHandle = leftLength * 0.6f
        val rightHandle = rightLength * 0.6f
        val joinTangent = 120f

        path.reset()
        path.moveTo(startX, startY)
        path.cubicTo(
            startX + leftHandle,
            startY,
            eventX - joinTangent,
            eventY,
            eventX,
            eventY
        )
        path.cubicTo(
            eventX + joinTangent,
            eventY,
            endX - rightHandle,
            endY,
            endX,
            endY
        )
        path.lineTo(startX, startY)
        path.close()
        mMatrix.setTranslate(gradientOffset.toFloat(), 0f)
        shader?.setLocalMatrix(mMatrix)
        paint.shader = shader
        canvas.drawPath(path, paint)
    }

    private fun drawDebugPoints(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        eventX: Float,
        startY: Float,
        endY: Float,
        eventY: Float
    ) {
        val leftLength = eventX - startX
        val rightLength = endX - eventX
        val leftHandle = leftLength * 0.6f
        val rightHandle = rightLength * 0.6f
        val joinTangent = 120f

        canvas.drawCircle(startX, startY, 10f, paint)
        canvas.drawCircle(startX + leftHandle, startY, 10f, paint)
        canvas.drawCircle(eventX - joinTangent, eventY, 10f, paint)
        canvas.drawCircle(eventX, eventY, 10f, paint)
        canvas.drawCircle(eventX + joinTangent, eventY, 10f, paint)
        canvas.drawCircle(endX - rightHandle, endY, 10f, paint)
        canvas.drawCircle(endX, endY, 10f, paint)
    }

    var mWakeUpBlurEffect: RenderEffect? = null

    private fun drawBlurEffect() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return
        }
        if (mWakeUpBlurEffect == null) {
            val blurEffect: RenderEffect = RenderEffect.createBlurEffect(WAKE_UP_BLUR_RADIUS, WAKE_UP_BLUR_RADIUS, Shader.TileMode.CLAMP)
            val offsetEffect: RenderEffect = RenderEffect.createOffsetEffect(0f, SPEAKING_BLUR_OFFSET_Y)

            //核心区别: 在理想无限画布中两者几乎一样; 在实际 View 边界处效果不同
            //先向下移动, 再模糊
            mWakeUpBlurEffect = RenderEffect.createChainEffect(blurEffect, offsetEffect)
            //先模糊, 再向下移动
            //mWakeUpBlurEffect = RenderEffect.createChainEffect(offsetEffect, wakeUpBlurEffect)
            setRenderEffect(mWakeUpBlurEffect)
        }
    }

    var mGradientOffsetAnimator: ValueAnimator? = null;
    var gradientOffset = 0

    //负责渐变色的横向往返流动，波形刷新由 Choreographer 驱动。
    private fun startAnimator() {
        mGradientOffsetAnimator = ValueAnimator.ofInt(-20, 20).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }
        mGradientOffsetAnimator?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                gradientOffset = animation.getAnimatedValue() as Int
            }
        })
        mGradientOffsetAnimator?.start()
    }

    private fun stopAnimator() {
        mGradientOffsetAnimator?.cancel()
        mGradientOffsetAnimator = null
    }
}
