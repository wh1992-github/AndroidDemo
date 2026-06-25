package com.example.customview.loadingview.view

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Created by test on 16/6/24.
 */
class LVBlazeWood @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LVBase(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private lateinit var mPaintBg: Paint
    private lateinit var mPaintWood: Paint
    private lateinit var mPaintFire: Paint
    private lateinit var rectFBg: RectF
    private lateinit var rectFWood: RectF
    private var mPadding = 0
    private var woodWidth = 0
    private var woodLength = 0
    private var wood: Bitmap? = null
    private val rectFire0 = RectF()
    private val rectFire1 = RectF()
    private val rectFire2 = RectF()
    private val rectFire3 = RectF()
    private lateinit var evaluator: ArgbEvaluator
    private var mAnimatedValue = 0.5f

    private fun initPaint() {
        evaluator = ArgbEvaluator()
        mPadding = dip2px(1f)
        mPaintBg = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.BLACK
        }
        mPaintWood = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(122, 57, 47)
        }
        mPaintFire = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.rgb(232, 132, 40)
        }
    }

    private fun getWood(): Bitmap {
        wood?.let { return it }

        wood = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(wood!!)
        canvas.rotate(-18f, rectFWood.centerX(), rectFWood.centerY())
        mPaintWood.color = Color.rgb(97, 46, 37)
        canvas.drawRoundRect(rectFWood, woodWidth / 5f, woodWidth / 5f, mPaintWood)
        canvas.rotate(36f, rectFWood.centerX(), rectFWood.centerY())
        mPaintWood.color = Color.rgb(102, 46, 37)
        canvas.drawRoundRect(rectFWood, woodWidth / 5f, woodWidth / 5f, mPaintWood)
        return wood!!
    }

    private fun drawFire0(canvas: Canvas) {
        val color = evaluator.evaluate(mAnimatedValue, Color.rgb(255, 220, 1), Color.rgb(240, 169, 47)) as Int
        mPaintFire.color = color

        val pathFire = Path()
        val rectFire0 = RectF()
        rectFire0.top = this.rectFire0.centerY() -
            (rectFire1.height() / 2 - this.rectFire0.height() / 2) * mAnimatedValue -
            (this.rectFire0.centerY() - rectFire1.centerY()) * mAnimatedValue
        rectFire0.bottom = this.rectFire0.centerY() +
            (rectFire1.height() / 2 - this.rectFire0.height() / 2) * mAnimatedValue -
            (this.rectFire0.centerY() - rectFire1.centerY()) * mAnimatedValue
        rectFire0.left = this.rectFire0.centerX() -
            (rectFire1.width() / 2 - this.rectFire0.width() / 2) * mAnimatedValue -
            rectFire1.width() / 5 * mAnimatedValue
        rectFire0.right = this.rectFire0.centerX() +
            (rectFire1.width() / 2 - this.rectFire0.width() / 2) * mAnimatedValue -
            rectFire1.width() / 5 * mAnimatedValue

        pathFire.moveTo(rectFire0.centerX(), rectFire0.top)
        pathFire.lineTo(rectFire0.right, rectFire0.centerY())
        pathFire.lineTo(rectFire0.centerX(), rectFire0.bottom)
        pathFire.lineTo(rectFire0.left, rectFire0.centerY())
        pathFire.close()
        canvas.drawPath(pathFire, mPaintFire)
    }

    private fun drawFire1(canvas: Canvas) {
        val color = evaluator.evaluate(mAnimatedValue, Color.rgb(240, 169, 47), Color.rgb(232, 132, 40)) as Int
        mPaintFire.color = color

        val pathFire = Path()
        val rectFire1 = RectF()
        rectFire1.top = this.rectFire1.centerY() - this.rectFire1.height() / 2 -
            (rectFire2.height() / 2 - this.rectFire1.height() / 2) * mAnimatedValue -
            (this.rectFire1.centerY() - rectFire2.centerY()) * mAnimatedValue
        rectFire1.bottom = this.rectFire1.centerY() + this.rectFire1.height() / 2 +
            (rectFire2.height() / 2 - this.rectFire1.height() / 2) * mAnimatedValue -
            (this.rectFire1.centerY() - rectFire2.centerY()) * mAnimatedValue
        rectFire1.left = this.rectFire1.centerX() - this.rectFire1.width() / 2 -
            (rectFire2.width() / 2 - this.rectFire1.width() / 2) * mAnimatedValue +
            this.rectFire1.width() / 5 * mAnimatedValue
        rectFire1.right = this.rectFire1.centerX() + this.rectFire1.width() / 2 +
            (rectFire2.width() / 2 - this.rectFire1.width() / 2) * mAnimatedValue +
            this.rectFire1.width() / 5 * mAnimatedValue

        pathFire.moveTo(rectFire1.centerX(), rectFire1.top)
        pathFire.lineTo(rectFire1.right, rectFire1.centerY())
        pathFire.lineTo(rectFire1.centerX(), rectFire1.bottom)
        pathFire.lineTo(rectFire1.left, rectFire1.centerY())
        pathFire.close()
        canvas.drawPath(pathFire, mPaintFire)
    }

    private fun drawFire2(canvas: Canvas) {
        val color = evaluator.evaluate(mAnimatedValue, Color.rgb(232, 132, 40), Color.rgb(223, 86, 33)) as Int
        mPaintFire.color = color

        val rectFire2 = RectF()
        rectFire2.bottom = this.rectFire2.centerY() +
            rectFire3.height() / 2 +
            (this.rectFire2.height() / 2 - rectFire3.height() / 2) * (1 - mAnimatedValue) -
            (this.rectFire2.centerY() - rectFire3.centerY()) * mAnimatedValue
        rectFire2.top = this.rectFire2.centerY() -
            rectFire3.height() / 2 -
            (this.rectFire2.height() / 2 - rectFire3.height() / 2) * (1 - mAnimatedValue) -
            (this.rectFire2.centerY() - rectFire3.centerY()) * mAnimatedValue
        rectFire2.left = this.rectFire2.centerX() -
            rectFire3.width() / 2 -
            (this.rectFire2.height() / 2 - rectFire3.width() / 2) * (1 - mAnimatedValue) +
            rectFire3.width() / 3 * mAnimatedValue
        rectFire2.right = this.rectFire2.centerX() +
            rectFire3.width() / 2 +
            (this.rectFire2.height() / 2 - rectFire3.width() / 2) * (1 - mAnimatedValue) +
            rectFire3.width() / 3 * mAnimatedValue

        val pathFire = Path()
        pathFire.moveTo(rectFire2.centerX(), rectFire2.top)
        pathFire.lineTo(rectFire2.right, rectFire2.centerY())
        pathFire.lineTo(rectFire2.centerX(), rectFire2.bottom)
        pathFire.lineTo(rectFire2.left, rectFire2.centerY())
        pathFire.close()
        canvas.drawPath(pathFire, mPaintFire)
    }

    private fun drawFire3(canvas: Canvas) {
        mPaintFire.color = Color.rgb(223, 86, 33)

        val rectFire3 = RectF()
        rectFire3.bottom = this.rectFire3.centerY() +
            this.rectFire3.height() / 2 * (1 - mAnimatedValue) -
            this.rectFire3.height() * 0.75f * mAnimatedValue
        rectFire3.top = this.rectFire3.centerY() -
            this.rectFire3.height() / 2 * (1 - mAnimatedValue) -
            this.rectFire3.height() * 0.75f * mAnimatedValue
        rectFire3.left = this.rectFire3.centerX() -
            this.rectFire3.height() / 2 * (1 - mAnimatedValue) -
            this.rectFire3.width() / 3 * mAnimatedValue
        rectFire3.right = this.rectFire3.centerX() +
            this.rectFire3.height() / 2 * (1 - mAnimatedValue) -
            this.rectFire3.width() / 3 * mAnimatedValue

        val pathFire = Path()
        pathFire.moveTo(rectFire3.centerX(), rectFire3.top)
        pathFire.lineTo(rectFire3.right, rectFire3.centerY())
        pathFire.lineTo(rectFire3.centerX(), rectFire3.bottom)
        pathFire.lineTo(rectFire3.left, rectFire3.centerY())
        pathFire.close()
        canvas.drawPath(pathFire, mPaintFire)
    }

    private fun initFire() {
        rectFire3.bottom = rectFBg.centerY() + woodLength / 5 - woodLength / 4
        rectFire3.top = rectFBg.centerY() - woodLength / 5 - woodLength / 4
        rectFire3.left = rectFBg.centerX() - woodLength / 5
        rectFire3.right = rectFBg.centerX() + woodLength / 5
        rectFire3.left += rectFire3.width() / 3
        rectFire3.right += rectFire3.width() / 3

        rectFire2.bottom = rectFBg.centerY() + woodLength / 3
        rectFire2.top = rectFBg.centerY() - woodLength / 3
        rectFire2.left = rectFBg.centerX() - woodLength / 3
        rectFire2.right = rectFBg.centerX() + woodLength / 3

        rectFire1.bottom = rectFBg.centerY() + woodLength / 4 + woodLength / 4
        rectFire1.top = rectFBg.centerY() - woodLength / 4 + woodLength / 4
        rectFire1.left = rectFBg.centerX() - woodLength / 4
        rectFire1.right = rectFBg.centerX() + woodLength / 4
        rectFire1.left -= rectFire1.width() / 5
        rectFire1.right -= rectFire1.width() / 5

        rectFire0.bottom = rectFWood.centerY() + rectFWood.height() / 2
        rectFire0.top = rectFWood.centerY() - rectFWood.height() / 2
        rectFire0.left = rectFWood.centerX() - rectFWood.height() / 2
        rectFire0.right = rectFWood.centerX() + rectFWood.height() / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        rectFBg = RectF(
            measuredWidth / 2 - mWidth / 2f + mPadding,
            measuredHeight / 2 - mWidth / 2f + mPadding,
            measuredWidth / 2 + mWidth / 2f - mPadding,
            measuredHeight / 2 + mWidth / 2f - mPadding
        )
        woodWidth = (rectFBg.height() / 12f).toInt()
        woodLength = (rectFBg.width() / 3 * 2).toInt()
        rectFWood = RectF()
        rectFWood.bottom = rectFBg.bottom - woodWidth * 2
        rectFWood.top = rectFBg.bottom - woodWidth * 3
        rectFWood.left = rectFBg.centerX() - woodLength / 2f
        rectFWood.right = rectFBg.centerX() + woodLength / 2f

        initFire()
        if (valueAnimator != null) {
            drawFire3(canvas)
            drawFire2(canvas)
            drawFire1(canvas)
            drawFire0(canvas)
        }
        canvas.drawBitmap(getWood(), 0f, 0f, mPaintBg)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dip2px(30f), dip2px(30f))
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(heightSpecSize, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, widthSpecSize)
        }
        mWidth = if (measuredWidth > height) measuredHeight else measuredWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = if (w > h) h else w
    }

    override fun getFontLength(paint: Paint, str: String): Float = paint.measureText(str)

    override fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    override fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        invalidate()
    }

    override fun OnAnimationRepeat(animation: Animator) {
    }

    override fun OnStopAnim(): Int {
        mAnimatedValue = 0.25f
        valueAnimator = null
        postInvalidate()
        return 1
    }

    override fun SetAnimRepeatMode(): Int = ValueAnimator.RESTART

    fun isAnimatorRunning(): Boolean {
        return valueAnimator?.isRunning == true
    }

    override fun AnimIsRunning() {
    }

    override fun SetAnimRepeatCount(): Int = ValueAnimator.INFINITE
}
