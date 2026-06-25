package com.example.customview.widget.banner.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.customview.widget.banner.holder.BannerViewHolder
import com.example.customview.widget.banner.holder.BannerViewHolderCreator
import com.example.customview.widget.banner.listener.OnPageClickListener

/**
 * 用于适配 Banner View Pager 数据的适配器。
 */
open class BannerViewPagerAdapter<T>(
    private var mData: List<T>?,
    private var mCreator: BannerViewHolderCreator<*>?
) : PagerAdapter() {
    private var mPageClickListener: OnPageClickListener? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getView(position, container)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    /**
     * 获取真实的Count
     *
     * @return
     */
    fun getRealCount(): Int {
        return mData?.size ?: 0
    }

    override fun getCount(): Int {
        return getRealCount()
    }

    /**
     * @param position
     * @param container
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    private fun getView(position: Int, container: ViewGroup): View {
        val realPosition = position % getRealCount()
        val holder = mCreator!!.createViewHolder() as? BannerViewHolder<T>
            ?: throw RuntimeException("can not return a null holder")

        val view = holder.createView(container.context)

        if (mData != null && mData!!.isNotEmpty()) {
            holder.onBind(container.context, realPosition, mData!![realPosition])
        }

        view.setOnClickListener { v ->
            mPageClickListener?.onPageClick(v, realPosition)
        }
        return view
    }

    fun getData(): List<T>? {
        return mData
    }

    fun setData(mData: List<T>?) {
        this.mData = mData
    }

    fun setPageClickListener(pageClickListener: OnPageClickListener?) {
        mPageClickListener = pageClickListener
    }
}
