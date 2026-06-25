package com.example.customview.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.customview.databinding.BannerViewLayoutBinding
import com.example.customview.widget.banner.adapter.BannerViewPagerAdapter
import com.example.customview.widget.banner.holder.BannerViewHolderCreator

/**
 * 用于展示 Banner 效果的自定义 View。
 */
open class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
    var viewPager: ViewPager? = null
        private set

    private var mIndicatorContainer: LinearLayout? = null
    private var mAdapter: BannerViewPagerAdapter<*>? = null
    private var mPagerOptions: PagerOptions? = null
    private var mCurrentIndicator: ImageView? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        val binding = BannerViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        viewPager = binding.bannerViewPager
        mIndicatorContainer = binding.bannerIndicatorContainer

        mPagerOptions = PagerOptions.Builder(context).build()

        viewPager!!.offscreenPageLimit = 4
        viewPager!!.addOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) {
        setIndicatorSelected(position)
        setOnPageSelectedListener(position)
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    private fun setOnPageSelectedListener(position: Int) {
        mPagerOptions!!.mOnPageChangeListener?.onPageSelected(position)
    }

    /**
     * 设置 PagerOptions
     *
     * @param options options
     * @return BannerPager<T>
     */
    fun setPagerOptions(options: PagerOptions?): BannerView {
        mPagerOptions = options
        return this
    }

    /**
     * 设置 page data
     *
     * @param data    List<T>
     * @param creator BannerViewHolderCreator
     */
    fun setPages(data: List<*>?, creator: BannerViewHolderCreator<*>?) {
        if (data == null || creator == null) {
            return
        }
        mAdapter = BannerViewPagerAdapter(data, creator)

        initIndicator()
        handlePagerOptions()

        viewPager!!.adapter = mAdapter
    }

    private fun initIndicator() {
        val count = mAdapter!!.getRealCount()

        mIndicatorContainer!!.removeAllViews()
        for (i in 0 until count) {
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10, 0, 10, 0)
            }
            val indicator = ImageView(context).apply {
                setImageDrawable(mPagerOptions!!.mIndicatorDrawable[0])
                this.layoutParams = layoutParams
            }
            mIndicatorContainer!!.addView(indicator)
        }
        setIndicatorSelected(viewPager!!.currentItem)
    }

    private fun setIndicatorSelected(currentItem: Int) {
        mCurrentIndicator?.apply {
            setImageDrawable(mPagerOptions!!.mIndicatorDrawable[0])
            isSelected = false
        }
        if (mIndicatorContainer!!.childCount > 0) {
            val indicator = mIndicatorContainer!!.getChildAt(currentItem) as ImageView
            indicator.isSelected = true
            indicator.setImageDrawable(mPagerOptions!!.mIndicatorDrawable[1])
            mCurrentIndicator = indicator
        }
    }

    private fun handlePagerOptions() {
        viewPager!!.pageMargin = mPagerOptions!!.mPageMargin
        val mp = viewPager!!.layoutParams as MarginLayoutParams
        mp.leftMargin = mPagerOptions!!.mPrePagerWidth
        mp.rightMargin = mPagerOptions!!.mPrePagerWidth
        viewPager!!.layoutParams = mp

        viewPager!!.setPageTransformer(true, mPagerOptions!!.mPageTransformer)
        mAdapter!!.setPageClickListener(mPagerOptions!!.mOnPageClickListener)
    }

    companion object {
        private const val TAG = "BannerView"
    }
}
