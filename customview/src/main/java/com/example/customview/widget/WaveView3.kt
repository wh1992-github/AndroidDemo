package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.Choreographer
import android.view.View
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * 在原有复合正弦波的基础上，为波浪边沿上方增加逐渐透明的渐变。
 *
 * 上方渐变和下方填充先合成为一张透明度蒙版，颜色最后只绘制一次。
 * 这样可以避免两个半透明颜色层在波浪边沿重叠，保证下方填充不受渐变影响。
 */
class WaveView3 : View {

    /** 原有主波振幅，单位为 px。 */
    private val amplitude = 30

    /** 原有次级波振幅，单位为 px。 */
    private val secondaryAmplitude = 8

    /** 原有整体上下浮动幅度，单位为 px。 */
    private val verticalFloatAmplitude = 5

    /** 原有波浪中心线位置，单位为 px。 */
    private val offsetY = 160

    /** 原有动画相位，单位为弧度。 */
    private var offsetX = 0F

    /** 原有半透明橙红色填充。 */
    private val waveColor = 0xFFFF7E37.toInt()

    /** 下方正弦波填充使用不透明纯红色。 */
    private val bottomWaveColor = Color.RED

    /** 火焰渐变能够向上延伸的最大高度，单位为 px。 */
    private val maxFadeHeight = 220F

    /**
     * 实际波浪中心线。
     * 原来的 offsetY=100px 空间不足时，自动下移波浪，为最高火焰和最大波形起伏预留空间。
     */
    private val actualOffsetY = maxOf(
        offsetY.toFloat(),
        maxFadeHeight + amplitude + secondaryAmplitude + verticalFloatAmplitude
    )

    /** 原有角频率：一个屏幕宽度内包含两个完整主波周期。 */
    private val omega = 2 * Math.PI / resources.displayMetrics.widthPixels * 2

    /** 原有下方填充路径。现在该路径写入蒙版，不再直接绘制颜色。 */
    private val path = Path()

    /** 绘制统一透明度蒙版，白色表示保留，透明表示不显示。 */
    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    /** 最终颜色画笔。SRC_IN 保证颜色只出现在统一蒙版内部。 */
    private val colorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = waveColor
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    /** 下方纯红色画笔，后绘制以覆盖火焰蒙版的接缝重叠区。 */
    private val bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = bottomWaveColor
    }

    /** 上方渐变网格的坐标和透明度颜色缓存，跨帧复用。 */
    private var fadeVertices = FloatArray(0)
    private var fadeColors = IntArray(0)

    /** 防止同一个帧回调被重复提交。 */
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

    private fun postNextFrame() {
        if (!isAttachedToWindow || frameCallbackPosted) return

        Choreographer.getInstance().postFrameCallback(frameCallback)
        frameCallbackPosted = true
    }

    private fun removeFrameCallback() {
        if (!frameCallbackPosted) return

        Choreographer.getInstance().removeFrameCallback(frameCallback)
        frameCallbackPosted = false
    }

    /** 按原来的速度更新相位。 */
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCallbackPosted = false
            if (!isAttachedToWindow) return

            offsetX += 0.02F
            offsetX = (offsetX * 100).roundToInt() / 100F
            if (offsetX >= 2 * Math.PI) {
                offsetX = 0F
            }

            invalidate()
            postNextFrame()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width <= 0 || height <= 0) return

        /*
         * 统一蒙版必须在离屏图层中生成：
         * 先绘制上方渐变蒙版，再绘制下方不透明蒙版，最后只填充一次颜色。
         */
        val layer = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        try {
            drawUpperFadeMask(canvas)
            canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), colorPaint)
        } finally {
            canvas.restoreToCount(layer)
        }

        // 纯红色最后绘制，覆盖火焰向下延伸的接缝保护区，同时避免背景漏出。
        drawBottomFill(canvas)
    }

    /**
     * 保留原有波浪公式和 20px 采样间隔，生成波浪边沿以下的完整不透明蒙版。
     */
    private fun drawBottomFill(canvas: Canvas) {
        path.reset()

        val firstY = calculateWaveY(0F)
        path.moveTo(0F, firstY)

        var x = WAVE_SAMPLE_STEP
        while (x <= width) {
            path.lineTo(x, calculateWaveY(x))
            x += WAVE_SAMPLE_STEP
        }

        // 当 View 宽度不是采样间隔的整数倍时，补上最右侧采样点。
        if ((width.toFloat() % WAVE_SAMPLE_STEP) != 0F) {
            path.lineTo(width.toFloat(), calculateWaveY(width.toFloat()))
        }

        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0F, height.toFloat())
        path.close()
        canvas.drawPath(path, bottomPaint)
    }

    /**
     * 沿原有波浪边沿向上创建高度持续变化的透明度网格。
     * 顶部 alpha=0，波浪边沿 alpha=255；这里只写透明度，不直接绘制橙色。
     */
    private fun drawUpperFadeMask(canvas: Canvas) {
        val segmentCount = (width / WAVE_SAMPLE_STEP).toInt() + 1
        val vertexCapacity = segmentCount * FADE_LAYER_COUNT * VERTICES_PER_QUAD
        ensureFadeCapacity(vertexCapacity)

        var vertexIndex = 0
        for (segment in 0 until segmentCount) {
            val leftX = (segment * WAVE_SAMPLE_STEP).coerceAtMost(width.toFloat())
            val rightX = ((segment + 1) * WAVE_SAMPLE_STEP).coerceAtMost(width.toFloat())
            if (rightX <= leftX) continue

            val leftY = calculateWaveY(leftX)
            val rightY = calculateWaveY(rightX)
            val leftFadeHeight = calculateFlameHeight(leftX)
            val rightFadeHeight = calculateFlameHeight(rightX)

            /*
             * 将纵向渐变拆成多层网格。每一层使用 smootherstep 计算透明度，
             * 接近顶部和波浪边沿时透明度变化速度都会逐渐降为 0，避免形成分割线。
             */
            for (layer in 0 until FADE_LAYER_COUNT) {
                val topFraction = layer.toFloat() / FADE_LAYER_COUNT
                val bottomFraction = (layer + 1).toFloat() / FADE_LAYER_COUNT
                val topColor = createMaskColor(topFraction)
                val bottomColor = createMaskColor(bottomFraction)

                /*
                 * 渐变蒙版向波浪下方额外延伸 MASK_SEAM_OVERLAP，
                 * 用不透明蒙版覆盖 Path 抗锯齿产生的亚像素缝隙，防止底部背景色漏出。
                 */
                val leftRange = leftFadeHeight + MASK_SEAM_OVERLAP
                val rightRange = rightFadeHeight + MASK_SEAM_OVERLAP
                val leftTopY = leftY - leftFadeHeight + leftRange * topFraction
                val leftBottomY = leftY - leftFadeHeight + leftRange * bottomFraction
                val rightTopY = rightY - rightFadeHeight + rightRange * topFraction
                val rightBottomY = rightY - rightFadeHeight + rightRange * bottomFraction

                // 每层由两个三角形组成，并与相邻层共享完全相同的边界。
                vertexIndex = putFadeVertex(vertexIndex, leftX, leftTopY, topColor)
                vertexIndex = putFadeVertex(vertexIndex, leftX, leftBottomY, bottomColor)
                vertexIndex = putFadeVertex(vertexIndex, rightX, rightBottomY, bottomColor)

                vertexIndex = putFadeVertex(vertexIndex, leftX, leftTopY, topColor)
                vertexIndex = putFadeVertex(vertexIndex, rightX, rightBottomY, bottomColor)
                vertexIndex = putFadeVertex(vertexIndex, rightX, rightTopY, topColor)
            }
        }

        canvas.drawVertices(
            Canvas.VertexMode.TRIANGLES,
            vertexIndex * VALUES_PER_VERTEX,
            fadeVertices,
            0,
            null,
            0,
            fadeColors,
            0,
            null,
            0,
            0,
            maskPaint
        )
    }

    /**
     * 使用 smootherstep 曲线生成蒙版透明度。
     * 公式 6t^5 - 15t^4 + 10t^3 在 t=0 和 t=1 处的斜率均为 0。
     */
    private fun createMaskColor(fraction: Float): Int {
        val t = fraction.coerceIn(0F, 1F)
        val smoothAlpha = t * t * t * (t * (t * 6F - 15F) + 10F)
        return Color.argb((255F * smoothAlpha).roundToInt(), 255, 255, 255)
    }

    /**
     * 计算指定位置当前帧的火焰渐变高度。
     *
     * 三组连续信号分别控制宽火焰、局部细节和快速明灭。
     * 所有位置至少保留 [MIN_FADE_HEIGHT] 的火焰高度，不会出现完全没有渐变的空白段；
     * 高能量区域仍可接近 [maxFadeHeight]，形成全宽连续但高低飘动的效果。
     */
    private fun calculateFlameHeight(x: Float): Float {
        val broadFlame = normalizedSin(omega * 0.5 * x - offsetX)
        val flameDetail = normalizedSin(omega * 1.5 * x + offsetX * 2 + 1.2)
        val normalizedPosition = x.toDouble() / width.coerceAtLeast(1).toDouble()
        val flicker = normalizedSin(
            offsetX.toDouble() * 3.0 + normalizedPosition * Math.PI * 2.0
        )

        val energy = (broadFlame * 0.5 + flameDetail * 0.3 + flicker * 0.2)
            .coerceIn(0.0, 1.0)
        val fluctuatingHeight = energy.pow(FLAME_HEIGHT_POWER)
        return (MIN_FADE_HEIGHT +
            (maxFadeHeight - MIN_FADE_HEIGHT) * fluctuatingHeight).toFloat()
    }

    /** 将正弦函数的 -1～1 转换为便于表示火焰强度的 0～1。 */
    private fun normalizedSin(angle: Double): Double = (Math.sin(angle) + 1.0) / 2.0

    /** 与原有 onDraw() 相同的复合正弦波公式。 */
    private fun calculateWaveY(x: Float): Float {
        val mainWave = amplitude * Math.sin(omega * x + offsetX + Math.PI / 2)
        val secondaryWave = secondaryAmplitude * Math.sin(omega * 2 * x - offsetX * 2)
        val verticalFloat = verticalFloatAmplitude * Math.sin(offsetX.toDouble())
        return (actualOffsetY + mainWave + secondaryWave + verticalFloat).toFloat()
    }

    private fun putFadeVertex(index: Int, x: Float, y: Float, color: Int): Int {
        val valueIndex = index * VALUES_PER_VERTEX
        fadeVertices[valueIndex] = x
        fadeVertices[valueIndex + 1] = y
        fadeColors[index] = color
        return index + 1
    }

    private fun ensureFadeCapacity(vertexCount: Int) {
        val coordinateCount = vertexCount * VALUES_PER_VERTEX
        if (fadeVertices.size < coordinateCount) {
            fadeVertices = FloatArray(coordinateCount)
        }
        if (fadeColors.size < vertexCount) {
            fadeColors = IntArray(vertexCount)
        }
    }

    companion object {
        private const val VALUES_PER_VERTEX = 2
        private const val VERTICES_PER_QUAD = 6
        private const val WAVE_SAMPLE_STEP = 20F
        private const val FADE_LAYER_COUNT = 16
        /** 蒙版在波浪边沿下方的重叠距离，用于封住抗锯齿产生的背景缝隙。 */
        private const val MASK_SEAM_OVERLAP = 2F
        /** 所有位置至少保留 200px 高度，保证整个上边沿都有明显火焰。 */
        private const val MIN_FADE_HEIGHT = 200F
        private const val FLAME_HEIGHT_POWER = 1.7
    }
}
