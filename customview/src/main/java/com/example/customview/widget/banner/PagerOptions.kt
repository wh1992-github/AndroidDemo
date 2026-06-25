package com.example.customview.widget.banner

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.customview.widget.banner.listener.OnPageChangeListener
import com.example.customview.widget.banner.listener.OnPageClickListener

/**
 * 封装 Pager Options 相关逻辑的类。
 */
class PagerOptions private constructor() {
    @JvmField var mPageMargin = 0
    @JvmField var mPrePagerWidth = 0
    @JvmField var mIndicatorVisibility = 0
    @JvmField var mIndicatorDrawable: Array<Drawable?> = arrayOfNulls(2)
    @JvmField var mIndicatorDistance = 0
    @JvmField var mLoopEnable = false
    @JvmField var mDelayedTime = 0
    @JvmField var mIndicatorAlign = 0
    @JvmField var mPageTransformer: ViewPager.PageTransformer? = null
    @JvmField var mScrollDuration = 0
    @JvmField var mIndicatorMarginBottom = 0
    @JvmField var mIndicatorSize = 0
    @JvmField var mOnPageClickListener: OnPageClickListener? = null
    @JvmField var mOnPageChangeListener: OnPageChangeListener? = null

    class Builder(private var mContext: Context?) {
        private var mPageMargin = 0
        private var mPrePagerWidth = 0
        private var mIndicatorAlign = 0
        private var mIndicatorVisibility = 0
        private var mIndicatorDrawable: Array<Drawable?> = arrayOfNulls(2)
        private var mIndicatorDistance = 8
        private var mLoopEnable = true
        private var mPageTransformer: ViewPager.PageTransformer? = null
        private var mDelayedTime = 3000
        private var mScrollDuration = 800
        private var mIndicatorMarginBottom = -1
        private var mIndicatorSize = -1
        private var mOnPageClickListener: OnPageClickListener? = null
        private var mOnPageChangeListener: OnPageChangeListener? = null

        /**
         * 设置每个 page 之间间隔
         *
         * @param px px value
         * @return Builder
         */
        fun setPageMargin(px: Int): Builder = apply { mPageMargin = px }

        /**
         * 左右两侧预显示宽度
         *
         * @param px px value
         * @return Builder
         */
        fun setPrePagerWidth(px: Int): Builder = apply { mPrePagerWidth = px }

        /**
         * 设置指示器间距
         *
         * @param distance px value
         * @return Builder
         */
        fun setIndicatorDistance(distance: Int): Builder = apply { mIndicatorDistance = distance }

        /**
         * 设置指示器距离底部间距
         *
         * @param marginBottom marginBottom
         * @return Builder
         */
        fun setIndicatorMarginBottom(marginBottom: Int): Builder = apply { mIndicatorMarginBottom = marginBottom }

        /**
         * 设置指示器位置
         *
         * @param align RelativeLayout.ALIGN_PARENT_LEFT || RelativeLayout.CENTER_IN_PARENT || RelativeLayout.ALIGN_PARENT_RIGHT
         * @return Builder
         */
        fun setIndicatorAlign(align: Int): Builder = apply { mIndicatorAlign = align }

        /**
         * 设置Indicator 是否可见
         *
         * @param visibility One of VISIBLE, INVISIBLE, GONE.
         * @return Builder
         */
        fun setIndicatorVisibility(visibility: Int): Builder = apply { mIndicatorVisibility = visibility }

        /**
         * 设置轮播切换效果
         *
         * @param transformer PageTransformer
         * @return Builder
         */
        fun setPageTransformer(transformer: ViewPager.PageTransformer?): Builder = apply { mPageTransformer = transformer }

        /**
         * 设置指示器
         *
         * @param unSelected 未选中
         * @param selected   选中
         * @return Builder
         */
        fun setIndicatorDrawable(@DrawableRes unSelected: Int, @DrawableRes selected: Int): Builder = apply {
            mIndicatorDrawable[0] = ContextCompat.getDrawable(mContext!!, unSelected)
            mIndicatorDrawable[1] = ContextCompat.getDrawable(mContext!!, selected)
        }

        /**
         * 设置指示器
         *
         * @param unSelected 未选中
         * @param selected   选中
         * @return Builder
         */
        fun setIndicatorColor(@ColorInt unSelected: Int, @ColorInt selected: Int): Builder = apply {
            mIndicatorDrawable[0] = createDrawable(unSelected)
            mIndicatorDrawable[1] = createDrawable(selected)
        }

        private fun createDrawable(@ColorInt color: Int): Drawable {
            val size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                mContext!!.resources.displayMetrics
            ).toInt()
            return GradientDrawable().apply {
                setColor(color)
                shape = GradientDrawable.OVAL
                setSize(size, size)
            }
        }

        /**
         * 设置指示器
         *
         * @param size px
         * @return Builder
         */
        fun setIndicatorSize(size: Int): Builder = apply { mIndicatorSize = size }

        /**
         * 设置可否循环
         *
         * @param loop loop
         * @return Builder
         */
        fun setLoopEnable(loop: Boolean): Builder = apply { mLoopEnable = loop }

        /**
         * 设置切换时间
         *
         * @param duration ms
         * @return Builder
         */
        fun setTurnDuration(duration: Int): Builder = apply { mDelayedTime = duration }

        /**
         * 设置ViewPager的滚动速度
         *
         * @param duration ms
         */
        fun setScrollDuration(duration: Int): Builder = apply { mScrollDuration = duration }

        fun setOnPageClickListener(onPageClickListener: OnPageClickListener?): Builder =
            apply { mOnPageClickListener = onPageClickListener }

        fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?): Builder =
            apply { mOnPageChangeListener = onPageChangeListener }

        fun build(): PagerOptions {
            return PagerOptions().apply {
                mPageMargin = this@Builder.mPageMargin
                mPrePagerWidth = this@Builder.mPrePagerWidth
                mIndicatorDistance = this@Builder.mIndicatorDistance
                mIndicatorDrawable = this@Builder.mIndicatorDrawable
                mIndicatorSize = this@Builder.mIndicatorSize
                mIndicatorAlign = this@Builder.mIndicatorAlign
                mIndicatorVisibility = this@Builder.mIndicatorVisibility
                mLoopEnable = this@Builder.mLoopEnable
                mPageTransformer = this@Builder.mPageTransformer
                mDelayedTime = this@Builder.mDelayedTime
                mScrollDuration = this@Builder.mScrollDuration
                mIndicatorMarginBottom = this@Builder.mIndicatorMarginBottom
                mOnPageClickListener = this@Builder.mOnPageClickListener
                mOnPageChangeListener = this@Builder.mOnPageChangeListener
            }.also {
                mContext = null
            }
        }
    }
}
