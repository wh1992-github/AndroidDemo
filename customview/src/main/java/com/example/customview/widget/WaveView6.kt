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
import android.util.Log
import android.view.Choreographer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 使用正弦函数绘制并填充波浪。
 *
 * 波形公式：y = amplitude × sin(omega × x + offsetX + π / 2) + offsetY。
 * [Choreographer] 在每次屏幕 VSYNC 到来时推进 [offsetX] 并请求重绘，
 * [onDraw] 只根据当前数据生成路径，避免普通重绘意外推进动画。
 */
class WaveView6 : View {

    private var eventX = 0f
    private var eventY = 0f
    private var startX = 0
    private var startY = 0
    private var endX = 0
    private var endY = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
//        color = waveColor
    }

    // 夜间颜色渐变
    private val NIGHT_GRADIENT_COLORS = intArrayOf(
        Color.parseColor("#FF448DD4"),
        Color.parseColor("#FF448DD4"), Color.parseColor("#FF448DD4"),Color.parseColor("#FF448DD4"),Color.parseColor("#FF448DD4"),
//        Color.parseColor("#FF104392"),
//        Color.parseColor("#FF4F61A1"),
//        Color.parseColor("#FFFF765C"),
//        Color.parseColor("#FF8B7186")
    )

    // 与日/夜渐变色数组一一对应的颜色停靠点
    private val GRADIENT_POSITIONS = floatArrayOf(0f, 0.20f, 0.50f, 0.83f, 1f)


    /** 横向渐变 shader。View 初始化阶段 width 还没有经过测量，不能在此时创建。 */
    private var mShader: LinearGradient? = null

    private var mMatrix = Matrix();

    private val mPath = Path()

    /**
     * 角频率，决定水平方向的波浪数量。
     * 当前值使一个屏幕宽度内包含两个完整周期。
     */
    private val omega = 2 * Math.PI / resources.displayMetrics.widthPixels * 2

    /** 绘制对象在所有帧之间复用，避免 onDraw() 中频繁创建对象。 */

    /** 标记帧回调是否已经提交，防止同一帧被重复更新。 */
    private var frameCallbackPosted = false

    companion object {
        private const val TAG = "WaveView6"

        // 波浪光效的固定模糊半径
        private const val WAKE_UP_BLUR_RADIUS = 40F
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
//        drawBlurEffect()
        startAnimator()
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
        startX = 0
        startY = 0
        endX = w
        endY = 0
        eventX = w / 4f
        eventY = 200f

        // 使用测量后的实际宽度创建横向渐变；尺寸变化时同步重建。
        mShader = if (w > 0) {
            LinearGradient(0f, 0f, w.toFloat(), 0f, NIGHT_GRADIENT_COLORS, GRADIENT_POSITIONS, Shader.TileMode.MIRROR)
        } else {
            null
        }
    }

    private var flag = false

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

        if (eventX >= viewWidth * 3f / 4f && flag) {
            flag = false
        } else if (eventX <= viewWidth / 4f && !flag) {
            flag = true
        }

        if (flag) {
            eventX += 2f
        } else {
            eventX -= 2f
        }

        paint.shader = null
        paint.setColor(Color.RED)
//        //画3个点
//        paint.setStyle(Paint.Style.FILL)
//        canvas.drawCircle(startX.toFloat(), startY.toFloat(), 10f, paint)
//        canvas.drawCircle(endX.toFloat(), endY.toFloat(), 10f, paint)
//        canvas.drawCircle(eventX, eventY, 10f, paint)

//        //绘制连线
//        paint.setStrokeWidth(3f)
//        paint.setStyle(Paint.Style.FILL)
//        canvas.drawLine(startX.toFloat(), startY.toFloat(), eventX, eventY, paint)
//        canvas.drawLine(eventX, eventY, endX.toFloat(), endY.toFloat(), paint)

        //画二阶贝塞尔曲线
        mPath.reset()
        mPath.moveTo(startX.toFloat(), startY.toFloat())
        val leftLength = eventX - startX
        val rightLength = endX - eventX
        val maxLength = max(leftLength, rightLength)
        val leftHandle = leftLength * 0.6f
        val rightHandle = rightLength * 0.6f
        // 左右两侧长度差，范围约为 0～1：
        // 越靠近左右边缘，左右长度差越大，该值越接近 1。
        val lengthDifference = abs(leftLength - rightLength) / maxLength
        // 较长一侧的控制点向曲线内部偏移。
        // 长度差越大，向内偏移越明显；在中间位置时偏移量接近 0，
        // 从而保证控制点移动过程中的弧度变化是渐变的。
        val inwardOffset = min(leftLength, rightLength) * 0.12f * lengthDifference

        Log.i(TAG, "onDraw: lengthDifference = $lengthDifference, inwardOffset = $inwardOffset")

        // 判断哪一侧是较长的一侧，用于只对较长侧进行向内收拢。
        val leftIsLonger = leftLength > rightLength
        val rightIsLonger = rightLength > leftLength

        // 连接点两侧使用同一个基础控制柄，保证两段曲线在
        // (eventX, eventY) 处的切线方向一致，避免出现明显拐角。
        // 中间位置使用较小比例，避免底部过于圆润；越靠近两侧，
        // 比例越大，用于弥补短边变短后产生的尖角。
        // 中间位置降低控制柄比例，使底部收拢更快、更接近水滴尖端；
        // 靠近两侧时逐渐增加到 0.65，继续保持底部过渡平滑。
        val joinHandleRatio = 0.25f + 0.40f * lengthDifference
        val joinHandle = max(min(leftLength, rightLength) * joinHandleRatio, 1.0f)

        // 连接点实际使用的切线长度。
        // 需要扣除较长侧的向内偏移量，同时保证控制柄至少为 1px。
        val joinTangent = max(joinHandle - inwardOffset, 1.0f)

        val leftStartX = startX + leftHandle +
                if (leftIsLonger) inwardOffset else 0.0f
        val rightEndX = endX - rightHandle -
                if (rightIsLonger) inwardOffset else 0.0f

        // 每侧使用一段三次贝塞尔曲线，并让远端控制点明显低于最低点，
        // 使曲率只反转一次，形成一个完整的 S 形。
        // 两侧靠近最低点的控制点必须与最低点同高，保证连接处是水平切线，
        // 避免左、右曲线连接时产生拐角。
        val sControlYNearEnd = eventY * 0.12f

        mPath.cubicTo(
            leftStartX,
            startY + sControlYNearEnd,
            eventX - joinTangent,
            eventY,
            eventX,
            eventY
        )
        mPath.cubicTo(
            eventX + joinTangent,
            eventY,
            rightEndX,
            endY + sControlYNearEnd,
            endX.toFloat(),
            endY.toFloat()
        )
        mPath.lineTo(0f, 0f)
        mPath.close()
        mMatrix.setTranslate(gradientOffset.toFloat(), 0f)
        mShader?.setLocalMatrix(mMatrix)
        paint.shader = mShader
        paint.setStyle(Paint.Style.FILL)
        canvas.drawPath(mPath, paint)

        paint.shader = null
        paint.setColor(Color.RED)
//        //画3个点
        paint.setStyle(Paint.Style.FILL)
        canvas.drawCircle(startX.toFloat(),startY.toFloat(), 10f, paint)
        canvas.drawCircle(leftStartX, startY + sControlYNearEnd, 10f, paint)
        canvas.drawCircle(eventX - joinTangent, eventY, 10f, paint)
        canvas.drawCircle(eventX, eventY, 10f, paint)
        canvas.drawCircle(eventX + joinTangent, eventY, 10f, paint)
        canvas.drawCircle(rightEndX, endY + sControlYNearEnd, 10f, paint)
        canvas.drawCircle(endX.toFloat(), endY.toFloat(), 10f, paint)


    }

    var mWakeUpBlurEffect: RenderEffect? = null

    private fun drawBlurEffect() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return
        }
        if (mWakeUpBlurEffect == null) {
            val wakeUpBlurEffect: RenderEffect = RenderEffect.createBlurEffect(
                WAKE_UP_BLUR_RADIUS,
                WAKE_UP_BLUR_RADIUS,
                Shader.TileMode.CLAMP
            )
            val offsetEffect: RenderEffect =
                RenderEffect.createOffsetEffect(0f, WAKE_UP_BLUR_RADIUS)

            //核心区别: 在理想无限画布中两者几乎一样; 在实际 View 边界处效果不同
            //先向下移动, 再模糊
            mWakeUpBlurEffect = RenderEffect.createChainEffect(wakeUpBlurEffect, offsetEffect)
            //先模糊, 再向下移动
            //mWakeUpBlurEffect = RenderEffect.createChainEffect(offsetEffect, wakeUpBlurEffect)
            setRenderEffect(mWakeUpBlurEffect)
        }
    }

    var mGradientOffsetAnimator: ValueAnimator? = null;
    var gradientOffset = 0

    //负责渐变色的横向往返流动，波形刷新由 Choreographer 驱动。
    private fun startAnimator() {
        mGradientOffsetAnimator = ValueAnimator.ofInt(-200, 200).apply {
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
