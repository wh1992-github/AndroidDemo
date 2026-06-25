package com.example.customview.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.customview.R
import kotlin.math.max as maxFloat

/**
 * 在圆角卡片内部显示持续旋转的光环。
 *
 * 光环使用图片子 View 承载，而不是直接在当前 View 中绘制。子 View 的底部中心点
 * 被设置为旋转支点，父 View 再将图片裁剪到卡片范围内，并擦除中心区域，最终只
 * 保留类似边框的光环效果。
 */
class RotatingHaloView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        /** 初始旋转角度，同时决定动画开始时的相位。 */
        private const val START_ROTATION_DEGREES = -50f

        /** 临时调试开关：跳过裁剪，直接显示完整的源图片。 */
        private const val SHOW_FULL_IMAGE_DEBUG = false

        /** 使用独立常量控制图片缩放，便于调整视觉效果而不影响布局代码。 */
        private const val HALO_IMAGE_SCALE = 1f

        /** 图片旋转支点的额外垂直偏移量，单位为像素。 */
        private const val CONTENT_OFFSET_Y = 0f
    }

    /** 光环源图片，特意从无障碍遍历中排除。 */
    private val haloImageView = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        rotation = START_ROTATION_DEGREES
        scaleX = HALO_IMAGE_SCALE
        scaleY = HALO_IMAGE_SCALE
    }

    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // CLEAR 会擦除离屏图层中已经绘制的像素，使图片中心区域变为透明。
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    // 两个 Path 分别描述可见的圆角卡片区域和需要从中心擦除的区域。
    private val outerPath = Path()
    private val innerPath = Path()
    private val outerRect = RectF()
    private val innerRect = RectF()

    /** 可见光环的宽度。使用 dp，避免在高密度设备上显示过细。 */
    private val ringWidth = 8f.dp
    private val defaultCornerRadius = 24f.dp
    private var cornerRadius = defaultCornerRadius

    /** 延迟创建动画，避免未附着到窗口的 View 无意义地分配动画对象。 */
    private var rotationAnimator: ObjectAnimator? = null

    init {
        // dispatchDraw() 中包含自定义图层合成逻辑，因此必须允许框架调用绘制流程。
        setWillNotDraw(false)
        // CLEAR 混合模式依赖硬件图层，才能在不同 Android 设备上保持一致的擦除效果。
        setLayerType(LAYER_TYPE_HARDWARE, null)
        // 光环图片旋转时可能超出父 View 边界，因此关闭子 View 裁剪。
        clipChildren = false
        clipToPadding = false
        isClickable = false
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        addView(
            haloImageView,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.TOP or Gravity.START
            }
        )
        updateHaloImage()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ensureAnimator()
        updateAnimationState()
    }

    override fun onDetachedFromWindow() {
        // View 分离后取消动画，避免继续持有动画回调或消耗绘制帧。
        rotationAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        updateAnimationState()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        updateAnimationState()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) {
            return
        }

        // 使用当前 View 坐标系构建裁剪路径。内层圆角半径减去光环宽度，
        // 使中心擦除区域与外层裁剪区域保持一致的圆角轮廓。
        // 圆角不能超过 View 最短边的一半，否则小尺寸布局下会产生异常轮廓。
        cornerRadius = minOf(defaultCornerRadius, minOf(w, h) / 2f)
        outerRect.set(0f, 0f, w.toFloat(), h.toFloat())
        innerRect.set(ringWidth, ringWidth, w - ringWidth, h - ringWidth)
        outerPath.reset()
        outerPath.addRoundRect(outerRect, cornerRadius, cornerRadius, Path.Direction.CW)
        innerPath.reset()
        // 当 View 小于两个光环宽度时，内层区域为空，只保留外层裁剪结果。
        if (!innerRect.isEmpty) {
            innerPath.addRoundRect(
                innerRect,
                maxFloat(cornerRadius - ringWidth, 0f),
                maxFloat(cornerRadius - ringWidth, 0f),
                Path.Direction.CW
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (width > 0 && height > 0) {
            // 子 View 完成布局后才能可靠获取图片尺寸，因此在此更新图片几何位置。
            updateHaloGeometry(width, height)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (SHOW_FULL_IMAGE_DEBUG) {
            super.dispatchDraw(canvas)
            return
        }
        // 在离屏图层上操作：CLEAR 应擦除已经绘制的子 View，不能影响当前 View 下方的页面内容。
        val layer = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        try {
            val clipLayer = canvas.save()
            try {
                // 先将源图片限制在圆角卡片轮廓内。
                canvas.clipPath(outerPath)
                super.dispatchDraw(canvas)
            } finally {
                canvas.restoreToCount(clipLayer)
            }
            // 擦除中心区域，只保留裁剪后的外圈作为可见光环。
            if (!innerRect.isEmpty) {
                canvas.drawPath(innerPath, clearPaint)
            }
        } finally {
            canvas.restoreToCount(layer)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        ensureAnimator()
        updateAnimationState()
    }

    /** 根据 View、父容器和窗口状态统一决定是否运行动画。 */
    private fun updateAnimationState() {
        if (isAttachedToWindow && isShown) {
            ensureAnimator()
            rotationAnimator?.start()
        } else {
            rotationAnimator?.cancel()
        }
    }

    private fun ensureAnimator() {
        if (rotationAnimator != null) {
            return
        }
        rotationAnimator = ObjectAnimator.ofFloat(
            haloImageView,
            ROTATION,
            START_ROTATION_DEGREES,
            START_ROTATION_DEGREES + 360f
        ).apply {
            // 每 2.25 秒旋转一周，并使用匀速插值保持恒定角速度。
            duration = 2250L
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
    }

    private fun updateHaloImage() {
        haloImageView.setImageResource(R.drawable.llm_halo_bg)
        if (width > 0 && height > 0) {
            updateHaloGeometry(width, height)
        }
    }

    private fun updateHaloGeometry(stageWidth: Int, stageHeight: Int) {
        val drawable = haloImageView.drawable ?: return
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            return
        }
        // 使用图片的固有尺寸，避免 ImageView 测量过程对图片进行额外缩放。
        val layoutParams = haloImageView.layoutParams as LayoutParams
        if (layoutParams.width != drawableWidth || layoutParams.height != drawableHeight) {
            layoutParams.width = drawableWidth
            layoutParams.height = drawableHeight
            layoutParams.gravity = Gravity.TOP or Gravity.START
            haloImageView.layoutParams = layoutParams
        }
        val pivotX = drawableWidth / 2f
        // 将图片底部中心设置为旋转支点，使光环围绕卡片中心扫过。
        val pivotY = drawableHeight.toFloat()
        haloImageView.pivotX = pivotX
        haloImageView.pivotY = pivotY
        haloImageView.translationX = 0f
        haloImageView.translationY = 0f
        // 先将旋转支点放置到父 View 中心，再叠加可选的偏移量。
        haloImageView.x = stageWidth / 2f - pivotX
        haloImageView.y = stageHeight / 2f - pivotY + CONTENT_OFFSET_Y
    }

    //扩展函数
    private val Float.dp: Float
        get() = this * resources.displayMetrics.density
}
