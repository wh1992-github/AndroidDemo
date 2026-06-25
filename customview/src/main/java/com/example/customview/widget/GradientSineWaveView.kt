package com.example.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.max
import kotlin.math.sin

/**
 * 一个横向移动、整体上下浮动，并在上边沿逐渐消失的彩色波浪 View。
 *
 * ## 一、整体实现思想
 *
 * 绘制过程分成两步：
 *
 * 1. 先绘制一张只有透明度信息的“波浪蒙版”。
 * 2. 再绘制红色到绿色的矩形，并通过 [PorterDuff.Mode.SRC_IN] 裁剪到蒙版内部。
 *
 * 这样，波浪形状、上方透明度渐变和实际颜色互相独立：蒙版只负责“哪里可见”，
 * [LinearGradient] 只负责“显示什么颜色”。颜色不需要分成多个 Path 重复绘制，
 * 因而不会因为颜色叠加产生高亮边沿。
 *
 * ## 二、波浪是怎样产生的
 *
 * 每隔 2px 计算一个波浪采样点，Y 坐标由三部分相加得到：
 *
 * 1. 主正弦波：形成主要的波峰和波谷，并随 phase 向水平方向移动。
 * 2. 次级正弦波：频率是主波的两倍，并反向移动，使波形不会像标准正弦曲线一样机械。
 * 3. 整体垂直偏移：让整条波浪同时缓慢地上下浮动。
 *
 * 当前主波振幅为 12dp，次级波振幅为主波的 28%，整体浮动振幅为 7dp。
 * 如果 View 的实际高度不足，振幅会按比例缩小，防止波峰或波谷超出 View。
 *
 * ## 三、上边沿透明渐变是怎样实现的
 *
 * 这里没有使用 BlurMaskFilter，也没有真正的毛玻璃或模糊效果。
 * 对于相邻的两个波浪采样点，会创建一组三角形网格：
 *
 * - 波浪边沿向上 80dp：拆成 20 层透明度网格。
 * - 最顶部 8% 的区域：透明度保持为 0，保证最上方的网格边界不可见。
 * - 剩余区域：使用 smootherstep 曲线从透明平滑过渡到完全可见。
 * - 波浪边沿以下：蒙版透明度始终为 100%，不做模糊或渐隐。
 *
 * smootherstep 曲线在渐变起点和终点处的变化速度都是 0，比普通线性渐变更柔和，
 * 能减弱顶部轮廓以及波浪边沿处的视觉分割线。上方渐变和下方填充使用同一张网格，
 * 并共享完全相同的波浪边沿顶点，因此二者之间没有重叠区域和拼接缝隙。
 *
 * ## 四、颜色和移动效果是怎样实现的
 *
 * 实际填充颜色使用水平方向的 [LinearGradient]：左侧为不透明红色，右侧为不透明绿色。
 * 动画过程中通过 [Matrix] 水平平移 Shader，使填充颜色也会随波浪左右移动；只移动
 * Shader，不交换起止颜色，所以整体始终保持从左到右“红色 → 绿色”的主题方向。
 *
 * [ValueAnimator] 在 3000ms 内让 phase 从 0 变化到 2π，并无限循环。phase 同时控制
 * 主波移动、次级波反向移动、整体上下浮动和 Shader 位移，使这些效果保持同步。
 *
 * ## 五、性能和生命周期处理
 *
 * - 网格顶点数组会复用，只在宽度增大、现有容量不足时重新分配。
 * - 20 层渐变的位置和颜色会提前计算，不在每个采样点中重复计算。
 * - 每一帧只计算一次 sin(phase)，供整体浮动和 Shader 位移共同使用。
 * - View 不可见、窗口不可见或从窗口移除时停止 Animator，重新显示时再启动。
 *
 * XML 使用 wrap_content 时默认高度为 200dp；宽度建议设置为 match_parent。
 * 所有效果参数都临时写在类中，不依赖自定义 XML 属性。
 *
 * @param context View 所在的 Android 上下文，用于获取屏幕密度等资源信息。
 * @param attrs 从 XML 布局传入的属性集合；当前不读取自定义属性，但保留它以支持 XML 创建。
 * @param defStyleAttr 当前 View 的默认样式属性；通常使用默认值0。
 */
class GradientSineWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // ==================== 尺寸与绘制工具 ====================

    /**
     * 波浪边沿向上的渐变高度，单位为 px。
     *
     * 配置常量使用 dp，初始化时转换一次，后续每帧绘制直接使用 px，避免重复换算。
     */
    private val edgeFadeHeight = dpToPx(EDGE_FADE_HEIGHT_DP)

    /**
     * 绘制最终红绿渐变色的画笔。
     * Shader 在 [onSizeChanged] 中根据 View 的实际宽度创建。
     */
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /**
     * 绘制白色透明度蒙版的画笔。
     *
     * 蒙版中 RGB 颜色不重要，真正参与最终效果的是每个网格顶点的 Alpha 值。
     */
    private val waveMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    /**
     * SRC_IN 混合模式：只保留“红绿渐变矩形”与“波浪蒙版”重合的像素。
     */
    private val sourceInXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    // ==================== 网格缓存 ====================

    /**
     * 三角形网格的坐标数组，数据顺序为 x0、y0、x1、y1……。
     *
     * 数组会被重复使用；只有 View 变宽且容量不足时才会创建更大的数组，
     * 避免动画过程中每一帧产生大量临时对象并触发 GC。
     */
    private var waveMaskVertices = FloatArray(0)

    /**
     * 与 [waveMaskVertices] 中每个顶点一一对应的颜色数组。
     * 这里主要利用颜色的 Alpha 通道描述该顶点的可见程度。
     */
    private var waveMaskColors = IntArray(0)

    /**
     * 预先计算好的20层渐变颜色：第0层完全透明，最后一层完全可见。
     * 中间层使用 smootherstep 曲线计算，避免每个横向采样点重复计算。
     */
    private val edgeFadeLayerColors = IntArray(EDGE_FADE_LAYER_COUNT + 1) { layer ->
        createEdgeFadeColor(layer.toFloat() / EDGE_FADE_LAYER_COUNT)
    }

    /**
     * 每一层相对于真实波浪边沿的 Y 轴偏移量，单位为 px。
     * 第0层是 -80dp，最后一层是 0，即真实波浪边沿。
     */
    private val edgeFadeLayerOffsets =
        FloatArray(EDGE_FADE_LAYER_COUNT + 1) { layer ->
            val fraction = layer.toFloat() / EDGE_FADE_LAYER_COUNT
            -edgeFadeHeight * (1f - fraction)
        }

    // ==================== 颜色渐变 ====================

    /** 用于每帧平移 [gradientShader] 的矩阵，复用对象以避免重复创建。 */
    private val gradientMatrix = Matrix()

    /**
     * 从左侧红色到右侧绿色的水平渐变 Shader。
     * View 尚未获得有效宽度时为 null。
     */
    private var gradientShader: LinearGradient? = null

    // ==================== 波浪参数 ====================

    // 当前只是临时效果，所有参数直接写在 View 内，不使用自定义 XML 属性。

    /** 主正弦波振幅，已经从 dp 转换为 px。 */
    private val amplitude = dpToPx(DEFAULT_AMPLITUDE_DP)

    /** 主正弦波的波长，已经从 dp 转换为 px。 */
    private val wavelength = dpToPx(DEFAULT_WAVELENGTH_DP)

    /** 整条波浪上下浮动的最大距离，已经从 dp 转换为 px。 */
    private val verticalFloatAmplitude = dpToPx(DEFAULT_VERTICAL_FLOAT_DP)

    /** 次级波振幅与主波振幅的比例，用于打破过于标准的正弦形状。 */
    private val secondaryWaveRatio = DEFAULT_SECONDARY_WAVE_RATIO

    /** 静止状态下波浪中心线位于内容高度中的比例。 */
    private val centerRatio = DEFAULT_CENTER_RATIO

    /** 红绿渐变 Shader 水平往返移动的最大距离，单位为 px。 */
    private val gradientMoveDistance = dpToPx(GRADIENT_MOVE_DISTANCE_DP)

    /**
     * 当前动画相位，范围为 0 到 2π。
     * 改变相位会同步影响主波、次级波、整体上下浮动和颜色移动。
     */
    private var phase = 0f

    /** 当前正在运行的动画；为 null 表示动画尚未启动或已经停止。 */
    private var animator: ValueAnimator? = null

    /**
     * View 尺寸发生变化时调用，例如首次布局或横竖屏切换。
     *
     * 这里根据新的内容宽度重新创建水平颜色渐变。颜色范围排除了左右 padding，
     * 因此内容区域最左侧是纯红色，最右侧是纯绿色。
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 填充区域从最左侧的不透明红色，线性过渡到最右侧的不透明绿色。
        val gradientStartX = paddingLeft.toFloat()
        val gradientEndX = (w - paddingRight).toFloat()
        gradientShader = if (gradientEndX > gradientStartX) {
            LinearGradient(
                gradientStartX,
                0f,
                gradientEndX,
                0f,
                GRADIENT_START_COLOR,
                GRADIENT_END_COLOR,
                Shader.TileMode.CLAMP
            )
        } else {
            null
        }
        wavePaint.shader = gradientShader
    }

    /**
     * 计算 View 最终尺寸。
     *
     * - 宽度：父布局给出 EXACTLY 或 AT_MOST 时使用父布局提供的宽度，以满足铺满需求。
     * - 高度：wrap_content 时期望为 [DEFAULT_HEIGHT_DP]，并加上上下 padding。
     * - 精确高度：通过 [resolveSize] 遵守父布局传入的测量限制。
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 父布局只要给出了宽度上限，就铺满这段可用宽度。
        val desiredWidth = max(
            suggestedMinimumWidth,
            paddingLeft + paddingRight
        )
        val desiredHeight = dpToPx(DEFAULT_HEIGHT_DP).toInt() +
            paddingTop +
            paddingBottom
        val measuredWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY,
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(widthMeasureSpec)

            else -> desiredWidth
        }

        setMeasuredDimension(
            measuredWidth,
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    /**
     * 每一帧的核心绘制入口。
     *
     * 执行顺序：
     * 1. 计算安全振幅、中心线和当前相位对应的波浪位置。
     * 2. 从左到右采样波浪，生成上方渐变和下方填充共用的三角形网格。
     * 3. 将网格绘制为 Alpha 蒙版。
     * 4. 使用 SRC_IN 把移动的红绿渐变裁剪进蒙版。
     *
     * @param canvas Android 提供的当前绘图画布。
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentLeft = paddingLeft.toFloat()
        val contentRight = (width - paddingRight).toFloat()
        val contentTop = paddingTop.toFloat()
        val contentBottom = (height - paddingBottom).toFloat()
        val contentWidth = contentRight - contentLeft
        val contentHeight = contentBottom - contentTop

        if (contentWidth <= 0f || contentHeight <= 0f) return

        /*
         * 最大纵向偏移由“主波 + 次级波 + 整体浮动”共同决定。
         * 根据当前高度压缩振幅，避免波峰或波谷跑出 View。
         */
        val requestedWaveExcursion = amplitude * (1f + secondaryWaveRatio)
        val requestedTotalExcursion =
            requestedWaveExcursion + verticalFloatAmplitude
        val excursionScale = if (requestedTotalExcursion > contentHeight / 2f) {
            (contentHeight / 2f) / requestedTotalExcursion
        } else {
            1f
        }
        val safeAmplitude = amplitude * excursionScale
        val safeVerticalFloatAmplitude =
            verticalFloatAmplitude * excursionScale
        val maxExcursion =
            safeAmplitude * (1f + secondaryWaveRatio) +
                safeVerticalFloatAmplitude
        val centerY = contentTop + contentHeight * centerRatio

        /*
         * 开启上方渐变时，除了波峰振幅，还要为 80dp 渐隐区域预留空间。
         * 当默认中心线过高时主动下移，优先保证透明渐变完整显示，
         * 相应压缩下方填充区域；关闭渐变时不再预留这段空间。
         */
        val reservedFadeHeight = if (SHOW_EDGE_FADE) edgeFadeHeight else 0f
        val minimumCenterY =
            contentTop + maxExcursion + reservedFadeHeight
        val maximumCenterY = contentBottom - maxExcursion
        val safeCenterY = if (minimumCenterY <= maximumCenterY) {
            centerY.coerceIn(minimumCenterY, maximumCenterY)
        } else {
            // 极小高度下无法同时满足上下空间时，仍优先保留上方渐隐区域。
            maximumCenterY.coerceAtLeast(contentTop + maxExcursion)
        }
        val safeWavelength = max(wavelength, MIN_WAVELENGTH_PX)
        val angularFrequency = TWO_PI / safeWavelength
        // 这两个值整帧不变，只计算一次，避免每个横向采样点重复执行 sin(phase)。
        val phaseSine = sin(phase)
        val verticalOffset = safeVerticalFloatAmplitude * phaseSine

        val segmentCapacity = (contentWidth / DRAW_STEP_PX).toInt() + 1
        ensureEdgeFadeCapacity(segmentCapacity)
        var meshVertexCount = 0

        // 从左侧第一个波形点开始。
        var x = contentLeft
        var y = calculateY(
            x = 0f,
            centerY = safeCenterY,
            amplitude = safeAmplitude,
            verticalOffset = verticalOffset,
            angularFrequency = angularFrequency
        )

        // 步长越小曲线越平滑。2px 能兼顾平滑度和每帧绘制开销。
        while (x < contentRight) {
            val nextX = (x + DRAW_STEP_PX).coerceAtMost(contentRight)
            val nextY = calculateY(
                x = nextX - contentLeft,
                centerY = safeCenterY,
                amplitude = safeAmplitude,
                verticalOffset = verticalOffset,
                angularFrequency = angularFrequency
            )

            if (SHOW_EDGE_FADE) {
                /*
                 * 上半部分拆成多段透明度网格，并使用 smootherstep 曲线。
                 * 曲线在顶部和波浪边沿处的变化斜率都为 0，不会形成可见轮廓。
                 * 将 SHOW_EDGE_FADE 改为 false，可以临时跳过整个上方渐变网格。
                 */
                for (layer in 0 until EDGE_FADE_LAYER_COUNT) {
                    val startOffset = edgeFadeLayerOffsets[layer]
                    val endOffset = edgeFadeLayerOffsets[layer + 1]
                    val leftStartY = y + startOffset
                    val leftEndY = y + endOffset
                    val rightStartY = nextY + startOffset
                    val rightEndY = nextY + endOffset
                    val startColor = edgeFadeLayerColors[layer]
                    val endColor = edgeFadeLayerColors[layer + 1]

                    meshVertexCount = putMaskVertex(
                        meshVertexCount, x, leftStartY, startColor
                    )
                    meshVertexCount = putMaskVertex(
                        meshVertexCount, x, leftEndY, endColor
                    )
                    meshVertexCount = putMaskVertex(
                        meshVertexCount, nextX, rightStartY, startColor
                    )
                    meshVertexCount = putMaskVertex(
                        meshVertexCount, nextX, rightStartY, startColor
                    )
                    meshVertexCount = putMaskVertex(
                        meshVertexCount, x, leftEndY, endColor
                    )
                    meshVertexCount = putMaskVertex(
                        meshVertexCount, nextX, rightEndY, endColor
                    )
                }
            }

            // 下半部分：从波浪边沿开始保持完全不透明，不做任何模糊或渐隐。
            meshVertexCount = putMaskVertex(meshVertexCount, x, y, Color.WHITE)
            meshVertexCount = putMaskVertex(
                meshVertexCount, x, contentBottom, Color.WHITE
            )
            meshVertexCount = putMaskVertex(meshVertexCount, nextX, nextY, Color.WHITE)
            meshVertexCount = putMaskVertex(meshVertexCount, nextX, nextY, Color.WHITE)
            meshVertexCount = putMaskVertex(
                meshVertexCount, x, contentBottom, Color.WHITE
            )
            meshVertexCount = putMaskVertex(
                meshVertexCount, nextX, contentBottom, Color.WHITE
            )

            x = nextX
            y = nextY
        }

        /*
         * 渐变跟随动画相位做水平往返移动。
         * 这里只平移 Shader，不交换颜色，因此始终保持左红、右绿的方向。
         */
        val gradientOffset = gradientMoveDistance * phaseSine
        gradientMatrix.reset()
        gradientMatrix.setTranslate(gradientOffset, 0f)
        gradientShader?.setLocalMatrix(gradientMatrix)

        /*
         * 上方渐变和下方填充处于同一张三角形网格中，共享完全相同的边沿顶点。
         * 两个区域没有重叠、没有间隙，也不会因为透明度叠加形成高亮线。
         */
        val maskLayer = canvas.saveLayer(
            contentLeft,
            contentTop,
            contentRight,
            contentBottom,
            null
        )
        try {
            canvas.drawVertices(
                Canvas.VertexMode.TRIANGLES,
                meshVertexCount * VALUES_PER_VERTEX,
                waveMaskVertices,
                0,
                null,
                0,
                waveMaskColors,
                0,
                null,
                0,
                0,
                waveMaskPaint
            )

            wavePaint.xfermode = sourceInXfermode
            canvas.drawRect(
                contentLeft,
                contentTop,
                contentRight,
                contentBottom,
                wavePaint
            )
        } finally {
            // 避免混合模式或离屏图层状态影响后续绘制。
            wavePaint.xfermode = null
            canvas.restoreToCount(maskLayer)
        }
    }

    /**
     * 确保网格坐标数组和颜色数组能够容纳当前宽度需要的全部三角形。
     *
     * 该方法只扩容、不缩容。这样 View 宽度偶尔变小时不会反复释放和重新申请数组。
     *
     * @param segmentCount 横向网格段数量。一段对应相邻的两个波浪采样点。
     */
    private fun ensureEdgeFadeCapacity(segmentCount: Int) {
        val requiredVertices = segmentCount * VERTICES_PER_SEGMENT
        val requiredVertexValues = requiredVertices * VALUES_PER_VERTEX
        if (waveMaskVertices.size < requiredVertexValues) {
            waveMaskVertices = FloatArray(requiredVertexValues)
        }

        if (waveMaskColors.size < requiredVertices) {
            waveMaskColors = IntArray(requiredVertices)
        }
    }

    /**
     * 向网格缓存中写入一个顶点。
     *
     * 一个顶点包含两个坐标值和一个颜色值。调用方使用返回的新索引继续写入，
     * 避免在循环中创建 Point、Pair 或其他临时对象。
     *
     * @param vertexIndex 当前要写入的顶点下标。
     * @param x 顶点的 X 坐标，单位为 px。
     * @param y 顶点的 Y 坐标，单位为 px。
     * @param color 顶点颜色；蒙版主要使用其中的 Alpha 通道。
     * @return 下一个可写入的顶点下标。
     */
    private fun putMaskVertex(
        vertexIndex: Int,
        x: Float,
        y: Float,
        color: Int
    ): Int {
        val valueIndex = vertexIndex * VALUES_PER_VERTEX
        waveMaskVertices[valueIndex] = x
        waveMaskVertices[valueIndex + 1] = y
        waveMaskColors[vertexIndex] = color
        return vertexIndex + 1
    }

    /**
     * 根据渐变进度生成白色蒙版颜色。
     *
     * 前8%的区域固定返回 alpha=0；剩余部分使用 smootherstep 曲线：
     * `6t^5 - 15t^4 + 10t^3`。该曲线在 t=0 和 t=1 时斜率都为0，
     * 所以透明区域、渐变区域和完整填充区域之间不会突然改变明暗速度。
     *
     * @param fraction 原始渐变进度，范围为0到1；0表示最顶部，1表示波浪边沿。
     * @return RGB 为白色、Alpha 按平滑曲线计算的 ARGB 颜色值。
     */
    private fun createEdgeFadeColor(fraction: Float): Int {
        /*
         * 最上方先保持一小段 alpha=0，保证网格的物理边界完全不可见。
         * 随后使用 smootherstep：6t^5 - 15t^4 + 10t^3。
         */
        val t = ((fraction - EDGE_TRANSPARENT_HOLD_RATIO) /
            (1f - EDGE_TRANSPARENT_HOLD_RATIO)).coerceIn(0f, 1f)
        val smoothAlpha =
            t * t * t * (t * (t * 6f - 15f) + 10f)
        val alpha = (MAX_MASK_ALPHA * smoothAlpha + 0.5f).toInt()
        return Color.argb(alpha, 255, 255, 255)
    }

    /**
     * 计算指定 X 坐标处的最终波浪 Y 坐标。
     *
     * 公式由“中心线 + 整体垂直偏移 + 主波 + 次级波”组成。主波使用正相位，
     * 次级波使用两倍频率和反向相位，因此两条波的相对形状会持续变化。
     *
     * @param x 相对于内容区域左侧的横向距离，单位为 px。
     * @param centerY 波浪中心线的绝对 Y 坐标。
     * @param amplitude 经过高度安全缩放后的主波振幅。
     * @param verticalOffset 当前帧整条波浪的垂直偏移量。
     * @param angularFrequency 角频率，计算公式为 2π/波长。
     * @return 当前 X 坐标对应的绝对 Y 坐标。
     */
    private fun calculateY(
        x: Float,
        centerY: Float,
        amplitude: Float,
        verticalOffset: Float,
        angularFrequency: Float
    ): Float {
        // 主波向一个方向移动。
        val primaryWave = amplitude * sin(angularFrequency * x + phase)

        // 次级波频率更高并反向移动，让波形不再是机械的单一正弦。
        val secondaryWave =
            amplitude *
                secondaryWaveRatio *
                sin(angularFrequency * 2f * x - phase * 2f)

        return centerY + verticalOffset + primaryWave + secondaryWave
    }

    /**
     * 开始移动动画。
     *
     * 预览模式下不启动动画；如果已有 Animator，也不会重复创建。
     * 该方法是 public，除了生命周期自动调用外，也可以由外部主动调用。
     */
    fun startAnimation() {
        if (isInEditMode || animator != null) return

        animator = createAnimator().also {
            it.start()
        }
    }

    /**
     * 停止动画并释放 Animator。
     *
     * cancel 后立即置为 null，View 下次显示或外部再次调用 [startAnimation] 时，
     * 会创建一个全新的 Animator，不会复用已经取消的实例。
     */
    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }

    /**
     * 创建控制相位的无限循环动画。
     *
     * 使用线性插值保证波浪水平移动速度均匀。每次回调将动画值写入 [phase]，
     * 再通过 [postInvalidateOnAnimation] 请求在下一次屏幕刷新时重绘。
     */
    private fun createAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, TWO_PI).apply {
            duration = DEFAULT_DURATION_MS
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                phase = animation.animatedValue as Float
                postInvalidateOnAnimation()
            }
        }
    }

    /** View 加入窗口后，根据当前可见状态决定是否启动动画。 */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateAnimationState()
    }

    /** Activity 切到后台或窗口重新可见时，同步暂停或恢复动画。 */
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        updateAnimationState()
    }

    /** View 自身或父级可见性变化时，同步动画状态，避免不可见时继续耗电。 */
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        // View 构造阶段也可能收到回调；挂载后再访问动画状态更安全。
        if (isAttachedToWindow) {
            updateAnimationState()
        }
    }

    /** View 离开窗口时一定取消 Animator，避免 Animator 持有 View。 */
    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    /**
     * 统一维护动画启停条件。
     *
     * 只有 View 已挂载、当前确实显示，并且所在窗口可见时才运行动画；
     * 其他情况全部停止。集中判断可以避免多个生命周期回调中的逻辑不一致。
     */
    private fun updateAnimationState() {
        if (isAttachedToWindow && isShown && windowVisibility == VISIBLE) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    /**
     * 将 dp 转换为当前设备对应的物理像素值。
     *
     * @param dp 与屏幕密度无关的尺寸。
     * @return 根据 density 换算后的 px 尺寸。
     */
    private fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private companion object {
        /**
         * 上方透明渐变的临时开关。
         *
         * true：显示80dp上方渐变。
         * false：只显示边沿清晰的下方正弦波填充。
         * 这是代码内调试开关，不属于自定义 XML 属性。
         */
        const val SHOW_EDGE_FADE = true

        /** wrap_content 对应的默认高度，单位为 dp。 */
        const val DEFAULT_HEIGHT_DP = 200f

        /** 主正弦波振幅，单位为 dp。 */
        const val DEFAULT_AMPLITUDE_DP = 12f

        /** 主正弦波完成一个周期所需的水平距离，单位为 dp。 */
        const val DEFAULT_WAVELENGTH_DP = 240f

        /** 整条波浪上下浮动的振幅，单位为 dp。 */
        const val DEFAULT_VERTICAL_FLOAT_DP = 7f

        /** 次级波振幅占主波振幅的28%。 */
        const val DEFAULT_SECONDARY_WAVE_RATIO = 0.28f

        /** 默认波浪中心线位于内容区域高度的42%处。 */
        const val DEFAULT_CENTER_RATIO = 0.42f

        /** 颜色 Shader 左右移动的最大距离，单位为 dp。 */
        const val GRADIENT_MOVE_DISTANCE_DP = 36f

        /** 波浪边沿向上的透明度渐变高度，单位为 dp。 */
        const val EDGE_FADE_HEIGHT_DP = 80f

        /** 将80dp透明度渐变切分成20层，用于拟合平滑曲线。 */
        const val EDGE_FADE_LAYER_COUNT = 20

        /** 渐变顶部保持完全透明的比例；0.08表示8%，当前约为6.4dp。 */
        const val EDGE_TRANSPARENT_HOLD_RATIO = 0.08f

        /** 一个矩形渐变网格需要两个三角形，共6个顶点。 */
        const val FADE_VERTICES_PER_LAYER = 6

        /** 每个横向段的下方填充区域同样使用两个三角形，共6个顶点。 */
        const val FILL_VERTICES_PER_SEGMENT = 6

        /** 一个横向段包含全部渐变层以及一个下方填充区域所需的顶点总数。 */
        const val VERTICES_PER_SEGMENT =
            EDGE_FADE_LAYER_COUNT * FADE_VERTICES_PER_LAYER +
                FILL_VERTICES_PER_SEGMENT

        /** 每个顶点包含 X、Y 两个 Float 坐标值。 */
        const val VALUES_PER_VERTEX = 2

        /** Android Paint Alpha 的最大值，即完全不透明。 */
        const val MAX_MASK_ALPHA = 255

        /** phase 从0运行到2π所需的时间，单位为毫秒。 */
        const val DEFAULT_DURATION_MS = 3_000L

        /** 防止波长为0导致角频率除零的最小像素值。 */
        const val MIN_WAVELENGTH_PX = 1f

        /** 水平方向波浪采样间隔，单位为物理像素。 */
        const val DRAW_STEP_PX = 2f

        /** 2π 的 Float 近似值，表示一个完整正弦周期。 */
        const val TWO_PI = 6.2831855f

        /** 水平颜色渐变的起始色：完全不透明的红色。 */
        val GRADIENT_START_COLOR = Color.rgb(255, 0, 0)

        /** 水平颜色渐变的结束色：完全不透明的绿色。 */
        val GRADIENT_END_COLOR = Color.rgb(0, 255, 0)
    }
}
