package com.example.customview.widget.banner.listener

/**
 * 用于约束 On Page Change 回调能力的接口。
 */
fun interface OnPageChangeListener {
    /**
     * item 选中事件
     *
     * @param position position
     */
    fun onPageSelected(position: Int)
}
