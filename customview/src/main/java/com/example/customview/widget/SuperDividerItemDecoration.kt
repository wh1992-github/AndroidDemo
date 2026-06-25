package com.example.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * 封装 Super Divider Item Decoration 相关逻辑的类。
 */
class SuperDividerItemDecoration(builder: Builder) : RecyclerView.ItemDecoration() {

    private var dividerDefaultColor = 0xFFE1E5E8.toInt()
    private var dividerColor: Int
    private var dividerWidth: Int
    private var dividerPadding: Int
    private var dividerPaddingLeft: Int
    private var dividerPaddingRight: Int
    private var dividerPaddingTop: Int
    private var dividerPaddingBottom: Int
    private var dividerIsShowLastDivide: Boolean
    private var dividerPaint: Paint
    private var dividerFromPosition = 0
    private var orientation: Int

    init {
        context = builder.context
        dividerColor = if (builder.dividerColor == 0) dividerDefaultColor else builder.dividerColor
        dividerPadding = dp2px(builder.dividerPadding.toFloat())
        dividerPaddingLeft = dp2px(builder.dividerPaddingLeft.toFloat())
        dividerPaddingRight = dp2px(builder.dividerPaddingRight.toFloat())
        dividerPaddingTop = dp2px(builder.dividerPaddingTop.toFloat())
        dividerPaddingBottom = dp2px(builder.dividerPaddingBottom.toFloat())
        dividerWidth = if (builder.dividerWidth == 0) dp2px(0.5f) else dp2px(builder.dividerWidth.toFloat())
        dividerFromPosition = builder.dividerFromPosition
        dividerIsShowLastDivide = builder.dividerIsShowLastDivide
        orientation = builder.orientation
        dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = dividerColor
        }

        if (dividerPadding != 0) {
            dividerPaddingLeft = dividerPadding
            dividerPaddingRight = dividerPadding
            dividerPaddingTop = dividerPadding
            dividerPaddingBottom = dividerPadding
        }
    }

    class Builder(val context: Context) {
        var dividerColor = 0
            private set
        var dividerWidth = 0
            private set
        var dividerPadding = 0
            private set
        var dividerPaddingLeft = 0
            private set
        var dividerPaddingRight = 0
            private set
        var dividerPaddingTop = 0
            private set
        var dividerPaddingBottom = 0
            private set
        var dividerFromPosition = 0
            private set
        var dividerIsShowLastDivide = false
            private set
        var orientation = VERTICAL
            private set

        fun setDividerColor(dividerColor: Int): Builder {
            this.dividerColor = dividerColor
            return this
        }

        fun setDividerWidth(dividerWidth: Int): Builder {
            this.dividerWidth = dividerWidth
            return this
        }

        fun setDividerPadding(dividerPadding: Int): Builder {
            this.dividerPadding = dividerPadding
            return this
        }

        fun setDividerPaddingLeft(dividerPaddingLeft: Int): Builder {
            this.dividerPaddingLeft = dividerPaddingLeft
            return this
        }

        fun setDividerPaddingRight(dividerPaddingRight: Int): Builder {
            this.dividerPaddingRight = dividerPaddingRight
            return this
        }

        fun setDividerPaddingTop(dividerPaddingTop: Int): Builder {
            this.dividerPaddingTop = dividerPaddingTop
            return this
        }

        fun setDividerPaddingBottom(dividerPaddingBottom: Int): Builder {
            this.dividerPaddingBottom = dividerPaddingBottom
            return this
        }

        fun setDividerFromPosition(dividerFromPosition: Int): Builder {
            this.dividerFromPosition = dividerFromPosition
            return this
        }

        fun setIsShowLastDivide(dividerIsShowLastDivide: Boolean): Builder {
            this.dividerIsShowLastDivide = dividerIsShowLastDivide
            return this
        }

        fun setOrientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        fun build(): SuperDividerItemDecoration = SuperDividerItemDecoration(this)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (orientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val count = parent.childCount
        if (count > 0) {
            val showCount = if (dividerIsShowLastDivide) count else count - 1
            for (i in dividerFromPosition until showCount) {
                val view = parent.getChildAt(i)
                val itemBottom = view.bottom
                c.drawRect(
                    (parent.paddingLeft + dividerPaddingLeft).toFloat(),
                    itemBottom.toFloat(),
                    (parent.width - parent.paddingRight - dividerPaddingRight).toFloat(),
                    (itemBottom + dividerWidth).toFloat(),
                    dividerPaint
                )
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val count = parent.childCount
        if (count > 0) {
            val showCount = if (dividerIsShowLastDivide) count else count - 1
            for (i in dividerFromPosition until showCount) {
                val view = parent.getChildAt(i)
                val itemRight = view.right
                c.drawRect(
                    itemRight.toFloat(),
                    (parent.paddingTop + dividerPaddingTop).toFloat(),
                    (itemRight + dividerWidth).toFloat(),
                    (parent.height - parent.paddingBottom - dividerPaddingBottom).toFloat(),
                    dividerPaint
                )
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (orientation == VERTICAL) {
            outRect.bottom = dividerWidth
        } else {
            outRect.right = dividerWidth
        }
    }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        private lateinit var context: Context

        @JvmStatic
        fun dp2px(dpVal: Float): Int =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                context.resources.displayMetrics
            ).toInt()
    }
}
