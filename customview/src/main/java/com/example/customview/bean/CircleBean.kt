package com.example.customview.bean

import android.graphics.PointF

/**
 * Created by test on 2016/12/9.
 */
data class CircleBean(
    /**
     * 起点坐标
     */
    var p0: PointF?,
    /**
     * 进入动画的控制点坐标
     */
    var p1: PointF?,
    /**
     * 到达中心点做坐标
     */
    var p2: PointF?,
    /**
     * 飞出动画控制点的坐标
     */
    var p3: PointF?,
    /**
     * 结束位置坐标
     */
    var p4: PointF?,
    /**
     * 小球半径
     */
    var radius: Float,
    /**
     * 圆圈的透明度
     */
    var alpha: Int
) {
    /**
     * 真实轨迹坐标点
     */
    var p: PointF? = null
}
