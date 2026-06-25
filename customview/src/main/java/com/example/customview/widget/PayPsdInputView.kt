package com.example.customview.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.EditText
import com.example.customview.R

/**
 * Created by test on 2017/5/7.
 */
@SuppressLint("AppCompatCustomView")
class PayPsdInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : EditText(context, attrs) {

    private var startX = 0f
    private var startY = 0f
    private var cX = 0f
    private var radius = 10
    private var heightValue = 0
    private var widthValue = 0
    private var textLength = 0
    private var bottomLineLength = 0
    private var maxCount = 6
    private var circleColor = Color.BLACK
    private var bottomLineColor = Color.GRAY
    private var borderColor = Color.GRAY
    private lateinit var borderPaint: Paint
    private var divideLineWStartX = 0
    private var divideLineWidth = 2
    private var divideLineColor = Color.GRAY
    private var focusedColor = Color.BLUE
    private val rectF = RectF()
    private val focusedRecF = RectF()
    private var psdType = 0
    private var rectAngle = 0
    private lateinit var divideLinePaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var bottomLinePaint: Paint
    private var mComparePassword: String? = null
    private var position = 0
    private var mListener: onPasswordListener? = null

    init {
        getAtt(context, attrs)
        initPaint()
        setBackgroundColor(Color.TRANSPARENT)
        isCursorVisible = false
        filters = arrayOf(InputFilter.LengthFilter(maxCount))
    }

    private fun getAtt(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PayPsdInputView)
        maxCount = typedArray.getInt(R.styleable.PayPsdInputView_maxCount, maxCount)
        circleColor = typedArray.getColor(R.styleable.PayPsdInputView_circleColor, circleColor)
        bottomLineColor = typedArray.getColor(R.styleable.PayPsdInputView_bottomLineColor, bottomLineColor)
        radius = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_radius, radius)
        divideLineWidth = typedArray.getDimensionPixelSize(R.styleable.PayPsdInputView_divideLineWidth, divideLineWidth)
        divideLineColor = typedArray.getColor(R.styleable.PayPsdInputView_divideLineColor, divideLineColor)
        psdType = typedArray.getInt(R.styleable.PayPsdInputView_psdType, psdType)
        rectAngle = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_rectAngle, rectAngle)
        focusedColor = typedArray.getColor(R.styleable.PayPsdInputView_focusedColor, focusedColor)
        typedArray.recycle()
    }

    private fun initPaint() {
        circlePaint = getPaint(5, Paint.Style.FILL, circleColor)
        bottomLinePaint = getPaint(2, Paint.Style.FILL, bottomLineColor)
        borderPaint = getPaint(3, Paint.Style.STROKE, borderColor)
        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor)
    }

    private fun getPaint(strokeWidth: Int, style: Paint.Style, color: Int): Paint =
        Paint(ANTI_ALIAS_FLAG).apply {
            this.strokeWidth = strokeWidth.toFloat()
            this.style = style
            this.color = color
            isAntiAlias = true
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        heightValue = h
        widthValue = w
        divideLineWStartX = w / maxCount
        startX = (w / maxCount / 2).toFloat()
        startY = h / 2f
        bottomLineLength = w / (maxCount + 2)
        rectF.set(0f, 0f, widthValue.toFloat(), heightValue.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        when (psdType) {
            PSD_TYPE_WECHAT -> {
                drawWeChatBorder(canvas)
                drawItemFocused(canvas, position)
            }
            PSD_TYPE_BOTTOM_LINE -> drawBottomBorder(canvas)
        }
        drawPsdCircle(canvas)
    }

    private fun drawWeChatBorder(canvas: Canvas) {
        canvas.drawRoundRect(rectF, rectAngle.toFloat(), rectAngle.toFloat(), borderPaint)
        for (i in 0 until maxCount - 1) {
            canvas.drawLine(
                ((i + 1) * divideLineWStartX).toFloat(),
                0f,
                ((i + 1) * divideLineWStartX).toFloat(),
                heightValue.toFloat(),
                divideLinePaint
            )
        }
    }

    private fun drawItemFocused(canvas: Canvas, position: Int) {
        if (position > maxCount - 1) {
            return
        }
        focusedRecF.set(
            (position * divideLineWStartX).toFloat(),
            0f,
            ((position + 1) * divideLineWStartX).toFloat(),
            heightValue.toFloat()
        )
        canvas.drawRoundRect(
            focusedRecF,
            rectAngle.toFloat(),
            rectAngle.toFloat(),
            getPaint(3, Paint.Style.STROKE, focusedColor)
        )
    }

    private fun drawBottomBorder(canvas: Canvas) {
        for (i in 0 until maxCount) {
            cX = startX + i * 2 * startX
            canvas.drawLine(
                cX - bottomLineLength / 2,
                heightValue.toFloat(),
                cX + bottomLineLength / 2,
                heightValue.toFloat(),
                bottomLinePaint
            )
        }
    }

    private fun drawPsdCircle(canvas: Canvas) {
        for (i in 0 until textLength) {
            canvas.drawCircle(startX + i * 2 * startX, startY, radius.toFloat(), circlePaint)
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        position = start + lengthAfter
        textLength = text.toString().length

        if (textLength == maxCount) {
            mListener?.let { listener ->
                if (TextUtils.isEmpty(mComparePassword)) {
                    listener.inputFinished(getPasswordString())
                } else if (TextUtils.equals(mComparePassword, getPasswordString())) {
                    listener.onEqual(getPasswordString())
                } else {
                    listener.onDifference(mComparePassword!!, getPasswordString())
                }
            }
        }
        invalidate()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selStart == selEnd) {
            setSelection(text.length)
        }
    }

    fun getPasswordString(): String = text.toString().trim()

    fun setComparePassword(comparePassword: String?, listener: onPasswordListener?) {
        mComparePassword = comparePassword
        mListener = listener
    }

    fun setComparePassword(listener: onPasswordListener?) {
        mListener = listener
    }

    fun setComparePassword(psd: String?) {
        mComparePassword = psd
    }

    fun cleanPsd() {
        setText("")
    }

    @Suppress("ClassName")
    interface onPasswordListener {
        fun onDifference(oldPsd: String, newPsd: String)

        fun onEqual(psd: String)

        fun inputFinished(inputPsd: String)
    }

    companion object {
        private const val PSD_TYPE_WECHAT = 0
        private const val PSD_TYPE_BOTTOM_LINE = 1
    }
}
