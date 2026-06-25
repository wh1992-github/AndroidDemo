package com.example.customview.widget

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.example.customview.R
import com.example.customview.bean.CircleBean
import com.example.customview.utils.BezierUtil

/**
 * Created by test on 2016/12/9.
 */
class BubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val colors = IntArray(2)
    private val positions = FloatArray(2)
    private var amplitude = 20
    private lateinit var paint: Paint
    private var mCircleBeen: MutableList<CircleBean> = ArrayList()
    private val pathMeasures: MutableList<PathMeasure> = ArrayList()
    private lateinit var animatorSet: AnimatorSet
    private var centerImg: View? = null
    private var onBubbleAnimationListener: OnBubbleAnimationListener? = null

    init {
        init()
    }

    fun getCenterImg(): View? = centerImg

    fun setCenterImg(centerImg: View?) {
        this.centerImg = centerImg
    }

    fun getCircleBeen(): MutableList<CircleBean> = mCircleBeen

    fun setCircleBeen(circleBeen: MutableList<CircleBean>) {
        mCircleBeen = circleBeen
    }

    fun setOnBubbleAnimationListener(onBubbleAnimationListener: OnBubbleAnimationListener?) {
        this.onBubbleAnimationListener = onBubbleAnimationListener
    }

    abstract class OnBubbleAnimationListener {
        abstract fun onCompletedAnimationListener()
    }

    private fun init() {
        animatorSet = AnimatorSet()
        initPaint()
        initShader()
    }

    private fun initShader() {
        colors[0] = resources.getColor(R.color.circle_start)
        colors[1] = resources.getColor(R.color.circle_end)
        positions[0] = 0f
        positions[1] = 1f
    }

    private fun initPaint() {
        paint = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.FILL
            alpha = 60
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (animatorSet.isStarted) {
            for (circleBean in getCircleBeen()) {
                paint.shader = getColorShader(circleBean)
                paint.alpha = circleBean.alpha
                canvas.drawCircle(circleBean.p!!.x, circleBean.p!!.y, circleBean.radius, paint)
            }
        }
    }

    private fun getColorShader(circleBean: CircleBean): Shader {
        val x0 = circleBean.p!!.x - circleBean.radius
        val y0 = circleBean.p!!.y
        val x1 = circleBean.p!!.x + circleBean.radius
        val y1 = circleBean.p!!.y
        return LinearGradient(x0, y0, x1, y1, colors, positions, Shader.TileMode.MIRROR)
    }

    fun openAnimation() {
        if (!animatorSet.isRunning) {
            animatorSet.play(floatAnimation()).after(inAnimation()).before(outAnimation())
            animatorSet.start()
        }
    }

    fun stopAnimation() {
        if (animatorSet.isRunning) {
            animatorSet.cancel()
        }
    }

    fun inAnimation(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                val t = valueAnimator.animatedValue as Float
                for (i in mCircleBeen.indices) {
                    val c = mCircleBeen[i]
                    val pointF = BezierUtil.CalculateBezierPointForQuadratic(t, c.p0!!, c.p1!!, c.p2!!)
                    mCircleBeen[i].p = pointF
                    c.alpha = (t * 100).toInt()
                    if (t > 0.5f) {
                        setCenterViewAlpha(t)
                    } else {
                        setCenterViewAlpha(0f)
                    }
                }
                invalidate()
            }
        }
    }

    private fun floatAnimation(): ValueAnimator {
        val pos = FloatArray(2)
        val tan = FloatArray(2)

        for (i in mCircleBeen.indices) {
            val path = Path().apply {
                val direction = if (i % 2 == 0) Path.Direction.CCW else Path.Direction.CW
                addCircle(mCircleBeen[i].p2!!.x - amplitude, mCircleBeen[i].p2!!.y, amplitude.toFloat(), direction)
            }
            pathMeasures.add(PathMeasure().apply { setPath(path, true) })
        }

        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                val t = valueAnimator.animatedValue as Float
                for (i in mCircleBeen.indices) {
                    pathMeasures[i].getPosTan(pathMeasures[i].length * t, pos, tan)
                    mCircleBeen[i].p = PointF(pos[0], pos[1])
                }

                if (t > 0.3f) {
                    setCenterViewAlpha((1 - t) + 0.3f)
                }
                invalidate()
            }
        }
    }

    private fun outAnimation(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 600
            interpolator = DecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                setCenterViewAlpha(0f)
                val t = valueAnimator.animatedValue as Float
                for (i in mCircleBeen.indices) {
                    val c = mCircleBeen[i]
                    val pointF = BezierUtil.CalculateBezierPointForQuadratic(t, c.p2!!, c.p3!!, c.p4!!)
                    mCircleBeen[i].p = pointF
                    c.alpha = ((1 - t) * 100).toInt()
                }
                invalidate()
                if (1f == t) {
                    onBubbleAnimationListener?.onCompletedAnimationListener()
                }
            }
        }
    }

    private fun setCenterViewAlpha(alpha: Float) {
        getCenterImg()?.alpha = alpha
    }

    @Suppress("unused")
    private fun setAmplitude(amplitude: Int): BubbleView {
        this.amplitude = amplitude
        return this
    }

    companion object {
        private const val TAG = "BubbleView"
    }
}
