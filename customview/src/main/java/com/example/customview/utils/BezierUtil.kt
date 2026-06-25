package com.example.customview.utils

import android.graphics.PointF
import kotlin.math.pow

/**
 * Created by test on 2016/12/15.
 * 贝塞尔曲线计算工具类
 */
object BezierUtil {
    /**
     * 二阶贝塞尔曲线
     * B(t) = Po*(1-t)^2 + 2*p1*t*(1-t)+t^2*p2
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    @JvmStatic
    fun CalculateBezierPointForQuadratic(t: Float, p0: PointF, p1: PointF, p2: PointF): PointF {
        val point = PointF()
        val temp = 1 - t
        point.x = (temp.toDouble().pow(2.0) * p0.x + 2 * t * temp * p1.x + t.toDouble().pow(2.0) * p2.x).toFloat()
        point.y = (temp.toDouble().pow(2.0) * p0.y + 2 * t * temp * p1.y + t.toDouble().pow(2.0) * p2.y).toFloat()
        return point
    }

    /**
     * 三
     * 阶贝塞尔曲线
     * B(t) = Po*(1-t)^3 + 3*p1*t*(1-t)^2+3*p2*t^2*(1-t)+p3*t^3,
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    @JvmStatic
    fun CalculateBezierPointForCubic(t: Float, p0: PointF, p1: PointF, p2: PointF, p3: PointF): PointF {
        val point = PointF()
        val temp = 1 - t
        point.x = (p0.x * temp.toDouble().pow(3.0) + 3 * p1.x * t * temp.toDouble().pow(2.0) + 3 * p2.x * t.toDouble().pow(2.0) * temp + p3.x * t.toDouble().pow(3.0)).toFloat()
        point.y = (p0.y * temp.toDouble().pow(3.0) + 3 * p1.y * t * temp.toDouble().pow(2.0) + 3 * p2.y * t.toDouble().pow(2.0) * temp + p3.y * t.toDouble().pow(3.0)).toFloat()
        return point
    }
}
