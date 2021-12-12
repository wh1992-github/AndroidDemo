package com.example.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.customview.R;

public class RecyclerQQItemView extends HorizontalScrollView {
    private LinearLayout slide;//滑动弹出的按钮容器
    private int slideWidth; //滑动弹出这个控件的宽度
    private onSlidingButtonListener onSbl;//滑动按钮侦听器

    public RecyclerQQItemView(Context context) {
        this(context, null);
    }

    public RecyclerQQItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerQQItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        slide = findViewById(R.id.slide);
    }

    //通过布局获取按钮宽度
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            slideWidth = slide.getWidth();
        }
    }

    //触摸事件
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onSbl.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollerX();
                return true;
        }
        return super.onTouchEvent(ev);
    }

    //按滚动条被拖动距离判断关闭或打开菜单 (被拖动的距离有没有隐藏或显示控件的一半以上？)
    public void changeScrollerX() {
        //判断拖动的距离有没有超过删除按钮的一半
        if (getScrollX() >= (slideWidth / 2)) {
            //推动了一半以上就打开
            this.smoothScrollTo(slideWidth, 0);
            onSbl.onMenuIsOpen(this);
        } else {
            //没有一半以上就关上
            this.smoothScrollTo(0, 0);
        }
    }

    //关闭菜单
    public void closeMenu() {
        this.smoothScrollTo(0, 0);
    }

    //设置滑动按钮监听器
    public void setSlidingButtonListener(onSlidingButtonListener listener) {
        onSbl = listener;
    }

    //滑动按钮侦听器
    public interface onSlidingButtonListener {
        void onMenuIsOpen(View view);

        void onDownOrMove(RecyclerQQItemView recycler);
    }
}
