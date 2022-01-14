package com.example.customview.anim

import android.support.v7.widget.RecyclerView
import android.view.ViewPropertyAnimator

class RotateXItemAnimation(animDuration: Long = 500) : SuperItemAnimation(animDuration) {

    init {
        addDuration = animDuration
        removeDuration = animDuration
    }

    override fun setAddItemAnimInit(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationX = -90f
    }

    override fun setAddItemAnim(holder: RecyclerView.ViewHolder?, animator: ViewPropertyAnimator?) {
        animator?.rotationX(0f)
    }

    override fun setAddItemAnimCancel(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationX = 0f
    }

    override fun setRemoveItemAnim(
        holder: RecyclerView.ViewHolder?,
        animator: ViewPropertyAnimator?
    ) {
        animator?.rotationX(-90f)
    }

    override fun setRemoveItemAnimEnd(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.rotationX = 0f
    }

}