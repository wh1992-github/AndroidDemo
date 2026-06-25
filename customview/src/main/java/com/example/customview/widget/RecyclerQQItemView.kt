package com.example.customview.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.example.customview.databinding.ItemRecyclerQqBinding

/**
 * 用于展示 Recycler QQ Item 效果的自定义 View。
 */
open class RecyclerQQItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private lateinit var slide: LinearLayout
    private var slideWidth = 0
    private var onSbl: onSlidingButtonListener? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!::slide.isInitialized) {
            slide = ItemRecyclerQqBinding.bind(this).slide
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            scrollTo(0, 0)
            slideWidth = slide.width
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> onSbl!!.onDownOrMove(this)
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                changeScrollerX()
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    fun changeScrollerX() {
        if (scrollX >= slideWidth / 2) {
            smoothScrollTo(slideWidth, 0)
            onSbl!!.onMenuIsOpen(this)
        } else {
            smoothScrollTo(0, 0)
        }
    }

    fun closeMenu() {
        smoothScrollTo(0, 0)
    }

    fun setSlidingButtonListener(listener: onSlidingButtonListener?) {
        onSbl = listener
    }

    interface onSlidingButtonListener {
        fun onMenuIsOpen(view: View)
        fun onDownOrMove(recycler: RecyclerQQItemView)
    }
}
