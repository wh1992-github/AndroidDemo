package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * Created by test on 2016/10/26.
 */
open class IndexView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr) {

    private var mShowTextDialog: TextView? = null
    private lateinit var mPaint: Paint
    private var mWidth = 0
    private var mHeight = 0
    private var mCellWidth = 0
    private var mCellHeight = 0
    private var mWordSize = sp2px(mContext, 12f)
    private var mWordColor = 0
    private var mChoose = -1
    private var mOnTouchingLetterChangedListener: OnTouchingLetterChangedListener? = null

    init {
        initPaint()
    }

    fun setShowTextDialog(textDialog: TextView?) {
        mShowTextDialog = textDialog
    }

    private fun initPaint() {
        mPaint = Paint().apply {
            color = DEFAULT_TEXT_COLOR
            isAntiAlias = true
            textSize = mWordSize.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        mCellHeight = mHeight / WORDS.size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in WORDS.indices) {
            val xPos = mWidth / 2 - mPaint.measureText(WORDS[i]) / 2
            val yPos = (mCellHeight * i + mCellHeight).toFloat()
            canvas.drawText(WORDS[i], xPos, yPos, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y
        val oldChoose = mChoose
        val c = (y / height * WORDS.size).toInt()

        when (action) {
            MotionEvent.ACTION_UP -> {
                setBackgroundColor(0x00000000)
                mChoose = -1
                invalidate()
                mShowTextDialog?.visibility = INVISIBLE
            }
            else -> {
                setBackgroundColor(GRAY)
                if (oldChoose != c && c >= 0 && c < WORDS.size) {
                    mOnTouchingLetterChangedListener?.onTouchingLetterChanged(WORDS[c])
                    mShowTextDialog?.apply {
                        text = WORDS[c]
                        visibility = VISIBLE
                    }
                    mChoose = c
                    invalidate()
                }
            }
        }
        return true
    }

    fun setOnTouchingLetterChangedListener(letterChangedListener: OnTouchingLetterChangedListener?) {
        mOnTouchingLetterChangedListener = letterChangedListener
    }

    fun interface OnTouchingLetterChangedListener {
        fun onTouchingLetterChanged(letter: String)
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (spValue * scale + 0.5f).toInt()
    }

    companion object {
        private val WORDS = arrayOf(
            "↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "#"
        )
        private val GRAY = 0xFFe8e8e8.toInt()
        private val DEFAULT_TEXT_COLOR = 0xFF999999.toInt()
    }
}
