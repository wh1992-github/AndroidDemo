package com.example.customview.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.customview.R
import com.example.customview.listener.OnDragTouchListener
import com.example.customview.utils.DisplayUtils

object DragViewHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun addDragView(context: Context,
                    parent: ViewGroup,
                    imgUrl: String,
                    defaultImgResId: Int = R.mipmap.ic_launcher,
                    dragViewSize: Float = 60f,
                    dragViewOriginalMarginRight: Float = 20f,
                    dragViewOriginalMarginBottom: Float = 20f,
                    autoPullToBorder: Boolean = true,
                    onClick: (() -> Unit)? = null): ImageView {
        val dragView = ImageView(context)
        parent.post {
            val onDragTouchListener = OnDragTouchListener()
            onDragTouchListener.clickListener = {
                onClick?.invoke()
            }
            onDragTouchListener.mMaxWidth = parent.width
            onDragTouchListener.mMaxHeight = parent.height
            onDragTouchListener.mBorderMargin = DisplayUtils.dip2px(context, 15f).toFloat()
            onDragTouchListener.mIsAutoToBorder = autoPullToBorder
            dragView.scaleType = ImageView.ScaleType.CENTER_CROP
            dragView.setOnTouchListener(onDragTouchListener)
            val layoutParams = RelativeLayout.LayoutParams(DisplayUtils.dip2px(context, dragViewSize), DisplayUtils.dip2px(context, dragViewSize))
            layoutParams.leftMargin = parent.width - DisplayUtils.dip2px(context, dragViewSize + dragViewOriginalMarginRight)
            layoutParams.topMargin = parent.height - DisplayUtils.dip2px(context, dragViewSize + dragViewOriginalMarginBottom)
//           GlideApp.with(context).load(imgUrl).into(dragView)
            dragView.setBackgroundResource(defaultImgResId)
            parent.addView(dragView, layoutParams)
        }

        return dragView
    }

    fun removeDragView(parent: ViewGroup, view: View?) {
        if (view != null) {
            parent.removeView(view)
        }
    }

    fun updateDragView(context: Context, dragView: ImageView?, imgUrl: String) {
        if (dragView != null) {
//           GlideApp.with(context).load(imgUrl).into(dragView)
        }
    }
}