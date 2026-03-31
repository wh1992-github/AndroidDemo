package com.example.customview.anim

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewPropertyAnimator
/**
 * 封装 Rotate Y Item Animation 相关逻辑的类。
 */

class RotateYItemAnimation(animDuration: Long = 500) : SuperItemAnimation(animDuration) {

    override fun setAddItemAnimInit(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationY = -90f
    }

    override fun setAddItemAnim(holder: RecyclerView.ViewHolder?, animator: ViewPropertyAnimator?) {
        animator?.rotationY(0f)
    }

    override fun setAddItemAnimCancel(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationY = 0f
    }

    override fun setRemoveItemAnim(
        holder: RecyclerView.ViewHolder?,
        animator: ViewPropertyAnimator?
    ) {
        animator?.rotationY(-90f)
    }

    override fun setRemoveItemAnimEnd(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationY = 0f
    }

}