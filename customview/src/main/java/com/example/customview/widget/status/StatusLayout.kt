package com.example.customview.widget.status

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
/**
 * 用于组织 Status 界面内容的布局类。
 */

class StatusLayout : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {

    }
}