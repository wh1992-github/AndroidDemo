package com.example.hezi.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.hezi.R;
import com.example.hezi.viewpager.adapter.HomeAdapter;
import com.google.android.material.tabs.TabLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.reflect.Field;

public class ViewpagerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, SlideTopBar.ItemChangeListener {
    private static final String TAG = "MainActivity";
    private HorizontalScrollView mHorizontalScrollView;
    private MagicIndicator mMagicIndicator;
    private SlideTopBar mSlideTopBar;
    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;

    private CommonNavigator mCommonNavigator;
    private SimplePagerTitleView mSimplePagerTitleView;
    private LinePagerIndicator mLinePagerIndicator;

    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHorizontalScrollView = findViewById(R.id.magicscrollview);
        mMagicIndicator = findViewById(R.id.magicindicator);
        mSlideTopBar = findViewById(R.id.slidetopbar);
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.vp);
        mSlideTopBar.registListener(this);
        mSlideTopBar.smoothSlideBar(0);
        mViewPager.addOnPageChangeListener(this);
        mAdapter = new HomeAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtil.i(TAG, "onTabSelected: position = " + tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
                mSlideTopBar.smoothSlideBar(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtil.i(TAG, "onTabUnselected: position = " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogUtil.i(TAG, "onTabReselected: position = " + tab.getPosition());
            }
        });
        setIndicatorWidth(mTabLayout);
        setMagicIndicator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSlideTopBar.unRegistListener();
    }

    public void setIndicatorWidth(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {
                    //拿到tabLayout的slidingTabIndicator属性
                    Field slidingTabIndicatorField = tabLayout.getClass().getDeclaredField("slidingTabIndicator");
                    slidingTabIndicatorField.setAccessible(true);
                    LinearLayout mTabStrip = (LinearLayout) slidingTabIndicatorField.get(tabLayout);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的textView属性
                        Field textViewField = tabView.getClass().getDeclaredField("textView");
                        textViewField.setAccessible(true);
                        TextView textView = (TextView) textViewField.get(tabView);
                        //因为想要的效果是字多宽线就多宽，所以测量mTextView的宽度
                        int width = textView.getWidth();
                        LogUtil.i(TAG, "run: width = " + width);
                        if (width == 0) {
                            textView.measure(0, 0);
                            width = textView.getMeasuredWidth();
                        }

                        //设置tab左右间距,注意这里不能使用Padding,因为源码中线的宽度是根据tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width + 20;
                        params.leftMargin = 20;
                        params.rightMargin = 20;
                        LogUtil.i(TAG, "run: run: width = " + tabView.getPaddingLeft());
                        tabView.setPadding(0, 0, 0, 0);
                        tabView.setLayoutParams(params);
                        LogUtil.i(TAG, "run: run: run: width = " + tabView.getPaddingLeft());
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //设置头部标题的样式
    public void setMagicIndicator() {
        mCommonNavigator = new CommonNavigator(ViewpagerActivity.this);
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mAdapter.getCount();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                mSimplePagerTitleView = new ColorTransitionPagerTitleView(context);
                //字体未选中时颜色
                mSimplePagerTitleView.setNormalColor(Color.parseColor("#FFFFFFFF"));
                //字体选中时颜色
                mSimplePagerTitleView.setSelectedColor(Color.parseColor("#00C6D2"));
                mSimplePagerTitleView.setText(mAdapter.getPageTitle(index));
                mSimplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mSimplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                        smoothMagicIndicator(index);
                    }
                });
                return mSimplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                mLinePagerIndicator = new LinePagerIndicator(context);
                mLinePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                //设置导航条的高度
                mLinePagerIndicator.setLineHeight(UIUtil.dip2px(context, 4));
                mLinePagerIndicator.setLineWidth(UIUtil.dip2px(context, 95));
                //设置导航条的圆角
                mLinePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 3));
                mLinePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
                //导航条左右滑动的速度，越大越慢
                mLinePagerIndicator.setEndInterpolator(new DecelerateInterpolator(3.0f));
                //设置导航条的颜色
                mLinePagerIndicator.setColors(Color.parseColor("#00C6D2"));
                return mLinePagerIndicator;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    //移动到中间位置
    private void smoothMagicIndicator(int index) {
        //嵌套层级较多(⊙o⊙)
        LogUtil.i(TAG, "smoothMagicIndicator: size = "
                + ((LinearLayout) (((FrameLayout) (((CommonNavigator) (mMagicIndicator.getChildAt(0))).getChildAt(0))).getChildAt(1))).getChildCount());
        TextView currentView = (TextView) (((LinearLayout) (((FrameLayout) (((CommonNavigator) (mMagicIndicator.getChildAt(0))).getChildAt(0))).getChildAt(1))).getChildAt(index));
        int width = getResources().getDisplayMetrics().widthPixels;
        int left = currentView.getLeft() + currentView.getWidth() / 2;
        LogUtil.d(TAG, "smoothMagicIndicator: width = " + width + ", left = " + left);
        int smoothWidth = left - width / 2;
        LogUtil.i(TAG, "smoothMagicIndicator: smoothWidth = " + smoothWidth);
        mHorizontalScrollView.smoothScrollTo(Math.max(smoothWidth, 0), 0);
    }

    @Override
    public void switchTo(String title) {
        if (title.contains("1")) {
            smoothMagicIndicator(0);
            mViewPager.setCurrentItem(0, false);
        } else if (title.contains("2")) {
            smoothMagicIndicator(1);
            mViewPager.setCurrentItem(1, false);
        } else if (title.contains("3")) {
            smoothMagicIndicator(2);
            mViewPager.setCurrentItem(2, false);
        } else if (title.contains("4")) {
            smoothMagicIndicator(3);
            mViewPager.setCurrentItem(3, false);
        } else if (title.contains("5")) {
            smoothMagicIndicator(4);
            mViewPager.setCurrentItem(4, false);
        } else if (title.contains("6")) {
            smoothMagicIndicator(5);
            mViewPager.setCurrentItem(5, false);
        } else if (title.contains("7")) {
            smoothMagicIndicator(6);
            mViewPager.setCurrentItem(6, false);
        }
    }

    /******************OnPageChangeListener******************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //LogUtil.i(TAG, "onPageScrolled: position = " + position + ", positionOffset = " + positionOffset + ", positionOffsetPixels = " + positionOffsetPixels);
    }

    //如果滑了一下没有切换page，不会调用这个方法
    @Override
    public void onPageSelected(int position) {
        smoothMagicIndicator(position);
        mSlideTopBar.smoothSlideBar(position);
        LogUtil.i(TAG, "onPageSelected: position = " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING://1 手指按住滑动状态，只适用于触摸屏。
                //LogUtil.i(TAG, "onPageScrollStateChanged: SCROLL_STATE_DRAGGING");
                break;
            case ViewPager.SCROLL_STATE_IDLE://0 滑动完毕，停止状态,滑动成功会调用onPageSelected方法。
                //LogUtil.i(TAG, "onPageScrollStateChanged: SCROLL_STATE_IDLE");
                break;
            case ViewPager.SCROLL_STATE_SETTLING://2 自由滑动状态。
                //LogUtil.i(TAG, "onPageScrollStateChanged: SCROLL_STATE_SETTLING");
                break;
        }
    }
}
