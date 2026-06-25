package com.example.customview.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.customview.R

/**
 * 用于组织 Flow 界面内容的布局类。
 */
open class FlowLayout @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext, attrs, defStyleAttr) {

    private val mListView0 = mutableListOf<View>()
    private val mListView1 = mutableListOf<View>()
    private val mListView2 = mutableListOf<View>()
    private val mListView3 = mutableListOf<View>()
    private val mListView4 = mutableListOf<View>()

    init {
        addView()
    }

    fun addView() {
        removeAllViews()
        val data = context.resources.getStringArray(R.array.flow_data)
        if (data.isEmpty()) {
            visibility = GONE
            return
        }
        addHeaderView()
        data.forEach { word ->
            val textView = TextView(mContext).apply {
                setPadding(30, 18, 30, 18)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
                includeFontPadding = false
                text = word
                setTextColor(mContext.getColorStateList(R.color.flow_textview_selector))
                setOnClickListener { v ->
                    Log.i(TAG, "onClick: " + (v as TextView).text.toString())
                }
            }
            addView(textView)
        }
        visibility = VISIBLE
    }

    private fun addHeaderView() {
        val textView = TextView(mContext).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 40f)
            includeFontPadding = false
            text = "流式布局"
            setTextColor(Color.parseColor("#7f7f7f"))
        }
        addView(textView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = childCount
        var width: Int
        mListView0.clear()
        mListView1.clear()
        mListView2.clear()
        mListView3.clear()
        mListView4.clear()
        var index = 0
        var mTotalWidth: Int
        if (1 < count) {
            mListView0.add(getChildAt(0))
            mTotalWidth = 0
            for (i in 1 until count) {
                val view = getChildAt(i)
                width = view.measuredWidth + ITEM_GAPS
                mTotalWidth += width
                if (mTotalWidth < TOTAL_WIDTH) {
                    mListView1.add(view)
                } else {
                    index = i
                    break
                }
            }
        }
        if (mListView1.size + 1 < count) {
            mTotalWidth = 0
            for (i in index until count) {
                val view = getChildAt(i)
                width = view.measuredWidth + ITEM_GAPS
                mTotalWidth += width
                if (mTotalWidth < TOTAL_WIDTH) {
                    mListView2.add(view)
                } else {
                    index = i
                    break
                }
            }
        }
        if (mListView1.size + mListView2.size + 1 < count) {
            mTotalWidth = 0
            for (i in index until count) {
                val view = getChildAt(i)
                width = view.measuredWidth + ITEM_GAPS
                mTotalWidth += width
                if (mTotalWidth < TOTAL_WIDTH) {
                    mListView3.add(view)
                } else {
                    mListView4.add(view)
                }
            }
        }
        val measureWidth = measuredWidth
        if (mListView3.size > 0) {
            setMeasuredDimension(measureWidth, 300)
        } else if (mListView2.size > 0) {
            setMeasuredDimension(measureWidth, 236)
        } else if (mListView1.size > 0) {
            setMeasuredDimension(measureWidth, 172)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        var left = 30
        var top = 30
        var size = mListView0.size
        for (i in 0 until size) {
            val view = mListView0[i]
            val width = view.measuredWidth
            val height = view.measuredHeight
            view.layout(left, top, left + width, top + height)
            left = DELETE_BTN_LEFT
            top = 22
        }
        left = 0
        size = mListView1.size
        for (i in 0 until size) {
            val view = mListView1[i]
            val width = view.measuredWidth
            val height = view.measuredHeight
            view.layout(left, CONTENT_VIEW_TOP, left + width, CONTENT_VIEW_TOP + height)
            left += width + ITEM_GAPS
        }
        left = 0
        size = mListView2.size
        for (i in 0 until size) {
            val view = mListView2[i]
            val width = view.measuredWidth
            val height = view.measuredHeight
            view.layout(left, CONTENT_VIEW_TOP + LINE_HEIGHT, left + width, CONTENT_VIEW_TOP + LINE_HEIGHT + height)
            left += width + ITEM_GAPS
        }
        left = 0
        size = mListView3.size
        for (i in 0 until size) {
            val view = mListView3[i]
            val width = view.measuredWidth
            val height = view.measuredHeight
            view.layout(left, CONTENT_VIEW_TOP + LINE_HEIGHT * 2, left + width, CONTENT_VIEW_TOP + LINE_HEIGHT * 2 + height)
            left += width + ITEM_GAPS
        }
        size = mListView4.size
        for (i in 0 until size) {
            val view = mListView4[i]
            removeView(view)
        }
    }

    companion object {
        private const val TAG = "FlowLayout"
        private const val CONTENT_VIEW_TOP = 86
        private const val LINE_HEIGHT = 64
        private const val ITEM_GAPS = 10
        private const val TOTAL_WIDTH = 1080
        private const val DELETE_BTN_LEFT = 30
    }
}
