package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 封装 Hover Item Decoration 相关逻辑的类。
 */
open class HoverItemDecoration (
    private val context: Context,
    private val bindItemTextCallback: BindItemTextCallback
) : RecyclerView.ItemDecoration() {

    private var width = context.resources.displayMetrics.widthPixels
    private var itemHeight = dp2px(30)
    private var itemDivideHeight = dp2px(1)
    private var itemTextPaddingLeft = dp2px(20)
    private var itemHoverPaintColor = 0xFFf4f4f4.toInt()
    private var textPaintColor = 0xFF999999.toInt()
    private val itemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = itemHoverPaintColor }
    private val itemHoverPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = itemHoverPaintColor }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textPaintColor
        textSize = sp2px(15).toFloat()
    }
    private val textRect = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count = parent.childCount
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            val itemTop = view.top - itemHeight
            val itemBottom = view.top
            val position = parent.getChildAdapterPosition(view)
            val text = bindItemTextCallback.getItemText(position)

            if (isFirstInGroup(position)) {
                c.drawRect(0f, itemTop.toFloat(), width.toFloat(), itemBottom.toFloat(), itemPaint)
                drawText(c, itemTop, itemBottom, text)
            } else {
                c.drawRect(0f, (view.top - itemDivideHeight).toFloat(), width.toFloat(), view.top.toFloat(), itemHoverPaint)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val count = parent.childCount
        if (count > 0) {
            val firstView = parent.getChildAt(0)
            val position = parent.getChildAdapterPosition(firstView)
            val text = bindItemTextCallback.getItemText(position)
            if (firstView.bottom <= itemHeight && isFirstInGroup(position + 1)) {
                c.drawRect(0f, 0f, width.toFloat(), firstView.bottom.toFloat(), itemHoverPaint)
                drawText(c, firstView.bottom - itemHeight, firstView.bottom, text)
            } else {
                c.drawRect(0f, 0f, width.toFloat(), itemHeight.toFloat(), itemHoverPaint)
                drawText(c, 0, itemHeight, text)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (isFirstInGroup(position)) itemHeight else itemDivideHeight
    }

    private fun drawText(canvas: Canvas, itemTop: Int, itemBottom: Int, textString: String?) {
        textRect.left = itemTextPaddingLeft
        textRect.top = itemTop
        textRect.right = textString!!.length
        textRect.bottom = itemBottom
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(textString, textRect.left.toFloat(), baseline.toFloat(), textPaint)
    }

    private fun isFirstInGroup(position: Int): Boolean {
        return if (position == 0) {
            true
        } else {
            val prevItemText = bindItemTextCallback.getItemText(position - 1)
            val currentItemText = bindItemTextCallback.getItemText(position)
            !prevItemText.equals(currentItemText)
        }
    }

    fun interface BindItemTextCallback {
        fun getItemText(position: Int): String?
    }

    protected fun dp2px(dpVal: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), context.resources.displayMetrics).toInt()

    protected fun sp2px(spVal: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal.toFloat(), context.resources.displayMetrics).toInt()
}
