package com.example.hezi.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hezi.R;

public class SlideTopBar extends HorizontalScrollView {
    private static final String TAG = "SlideTopBar";
    private int mPreIndex = 0;
    private LinearLayout mLinearLayout;
    private ItemChangeListener mListener;
    private Context mContext;

    public SlideTopBar(Context context) {
        this(context, null);
    }

    public SlideTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView() {
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.removeAllViews();
        mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = 1; i <= 7; i++) {
            mLinearLayout.addView(getView(mContext, i));
        }
        addView(mLinearLayout);
    }

    private TextView getView(Context context, int i) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText("第" + i + "页");
        textView.setPadding(60, 0, 60, 0);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(getResources().getColorStateList(R.color.topbar_text_bg));
        textView.setSelected(i == mPreIndex);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.switchTo(((TextView) v).getText().toString());
            }
        });
        return textView;
    }

    //移动到中间位置
    public void smoothSlideBar(int index) {
        TextView preView = (TextView) mLinearLayout.getChildAt(mPreIndex);
        preView.setSelected(false);
        TextView currentView = (TextView) mLinearLayout.getChildAt(index);
        currentView.setSelected(true);
        mPreIndex = index;
        int width = getResources().getDisplayMetrics().widthPixels;
        int left = currentView.getLeft() + currentView.getWidth() / 2;
        LogUtil.d(TAG, "onClick: width = " + width + ", left = " + left);
        int smoothWidth = left - width / 2;
        LogUtil.i(TAG, "smoothSlideBar: smoothWidth = " + smoothWidth);
        smoothScrollTo(Math.max(smoothWidth, 0), 0);
    }

    public void registListener(ItemChangeListener listener) {
        mListener = listener;
    }

    public void unRegistListener() {
        mListener = null;
    }

    public interface ItemChangeListener {
        void switchTo(String title);
    }
}
