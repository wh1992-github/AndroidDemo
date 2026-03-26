package com.example.customview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Daisw
 */
public class TextSwitcherView extends FrameLayout {
    private static final String TAG = "TextSwitcherView";
    private final Context mContext;
    private int OFFSET_MOVE;

    // 当前显示的TextView和下一个要显示的TextView
    private TextView preView, currentView, nextView, tempView;

    // 双端队列存储待显示的文本
    private final Queue<String> textQueue = new ArrayDeque<>();

    // 默认属性值
    private long displayDuration = 200;     // 每条文本基础显示时长
    private long shortAnimDuration = 400;   // 短动画时长
    private long longAnimDuration = 600;    // 长动画时长
    private long maxDisplayTime = 2000;     // 默认最大显示时长 5s
    private float textSize = 60f;           // 默认字体大小 16sp
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isShowing = false;

    private final Runnable checkQueueRunnable = this::checkAndDisplayNextText;

    private int waitCount = 0;

    private AnimatorSet animatorSet;

    private String currentTextBean;

    public TextSwitcherView(Context context) {
        this(context, null);
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSwitcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initTextSwitcher();
    }

    /**
     * 初始化TextSwitcher
     */
    private void initTextSwitcher() {
        // 创建TextView
        preView = createTextView();
        currentView = createTextView();
        nextView = createTextView();
        tempView = createTextView();

        preView.setTranslationY(-100);
        preView.setAlpha(0f);

        currentView.setTranslationY(0);
        currentView.setAlpha(1f);

        nextView.setTranslationY(100);
        nextView.setAlpha(0f);

        tempView.setTranslationY(200);
        tempView.setAlpha(0f);

        // 添加到布局中
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        OFFSET_MOVE = 120;
        params.gravity = Gravity.CENTER;
        addView(preView, params);
        addView(currentView, params);
        addView(nextView, params);
        addView(tempView, params);
    }

    /**
     * 创建TextView
     */
    private TextView createTextView() {
        TextView textView = new TextView(mContext);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 添加文本到队列
     */
    public void addText(String displayNotify) {
        if (displayNotify == null) {
            return;
        }
        Log.d(TAG, "addText: " + displayNotify + ", queue size: " + textQueue.size());
        textQueue.offer(displayNotify);

        // 如果当前没有在显示，则开始显示
        if (!isShowing && !textQueue.isEmpty()) {
            isShowing = true;
            Log.d(TAG, "Starting to show texts");
            handler.post(checkQueueRunnable);
        }
    }

    /**
     * 检查队列并显示下一个文本
     */
    private void checkAndDisplayNextText() {
        Log.d(TAG, "checkAndDisplayNextText, queue size: " + textQueue.size());
        if (textQueue.isEmpty()) {
            waitCount++;
            // 如果已触发最大时间回调，则停止循环
            if (waitCount * displayDuration >= maxDisplayTime) {
                Log.d(TAG, "Max display time reached, stopping loop, calling onFinish");
                clear();
                return;
            }

            // 队列为空，等待200ms后再次检查
            Log.d(TAG, "Queue is empty, waiting 200ms");
            handler.postDelayed(checkQueueRunnable, displayDuration);
            return;
        }

        // 队列有数据，出队一个元素
        String nextText = textQueue.poll();
        currentTextBean = nextText;

        // 直接显示第一个文本（无动画）
//        if (TextUtils.isEmpty(currentView.getText())) {
//            Log.d(TAG, "First text, showing directly: " + nextText);
//            currentView.setText(nextText);
//            handler.postDelayed(checkQueueRunnable, displayDuration);
//            return;
//        }

        // 对于后续文本，立即执行动画切换
        Log.d(TAG, "Animating text immediately: " + nextText);
        waitCount = 0;
        // 根据队列剩余数量决定动画时长
        long animDuration = textQueue.isEmpty() ? longAnimDuration : shortAnimDuration;
        animateTextSwitch(nextText, animDuration);
    }

    /**
     * 获取动画移动距离
     */
    private float getMoveDistance() {
        // 如果高度为0，使用默认值
        return 100;
    }

    /**
     * 执行文本切换动画
     */
    private void animateTextSwitch(String text, long animDuration) {
        Log.d(TAG, "animateTextSwitch: " + text + ", duration: " + animDuration + ", remaining queue: " + textQueue.size());

        // 设置下一个TextView的文本
        tempView.setText(text);

        // 重置tempView的位置，为下一次动画做准备
        float moveDistance = getMoveDistance();
        tempView.setTranslationY(moveDistance * 2);
        tempView.setAlpha(1f);

        ObjectAnimator preTranslateAnimator = ObjectAnimator.ofFloat(
                preView, "translationY", -moveDistance, -moveDistance * 2);
        ObjectAnimator preAlphaAnimator = ObjectAnimator.ofFloat(
                preView, "alpha", 1f, 0f);

        ObjectAnimator currentTranslateAnimator = ObjectAnimator.ofFloat(
                currentView, "translationY", 0, -moveDistance);
        ObjectAnimator currentAlphaAnimator = ObjectAnimator.ofFloat(
                currentView, "alpha", 1f, 0.5f);

        ObjectAnimator nextTranslateAnimator = ObjectAnimator.ofFloat(
                nextView, "translationY", moveDistance, 0);
        ObjectAnimator nextAlphaAnimator = ObjectAnimator.ofFloat(
                nextView, "alpha", 0.5f, 1f);

        ObjectAnimator tempTranslateAnimator = ObjectAnimator.ofFloat(
                tempView, "translationY", moveDistance * 2, moveDistance);
        ObjectAnimator tempAlphaAnimator = ObjectAnimator.ofFloat(
                tempView, "alpha", 0f, 0.5f);

        // 创建动画集合
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(animDuration);
        animatorSet.playTogether(preTranslateAnimator, preAlphaAnimator, currentTranslateAnimator, currentAlphaAnimator, nextTranslateAnimator, nextAlphaAnimator, tempTranslateAnimator, tempAlphaAnimator);
        // 设置动画监听器
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "Animation ended, switching views");
                // 交换View
                TextView temp = preView;
                preView = currentView;
                currentView = nextView;
                nextView = tempView;
                tempView = temp;

                // 重置tempView的位置，为下一次动画做准备
                tempView.setTranslationY(moveDistance * 2);
                tempView.setAlpha(0f);
                tempView.setText("");

                handler.postDelayed(checkQueueRunnable, displayDuration);
            }
        });
        animatorSet.start();
    }

    /**
     * 清空队列并停止显示
     */
    public void clear() {
        Log.d(TAG, "clear");
        if (animatorSet != null) {
            animatorSet.cancel();
        }

        currentTextBean = null;
        textQueue.clear();
        handler.removeCallbacks(checkQueueRunnable);
        isShowing = false;
        waitCount = 0;

        float moveDistance = getMoveDistance();
        // 重置视图状态
        if (preView != null) {
            preView.setTranslationY(-moveDistance);
            preView.setAlpha(0.5f);
            preView.setText("");
        }
        if (currentView != null) {
            currentView.setTranslationY(0);
            currentView.setAlpha(1f);
            currentView.setText("");
        }
        if (nextView != null) {
            nextView.setTranslationY(moveDistance);
            nextView.setAlpha(0.5f);
            nextView.setText("");
        }
        if (tempView != null) {
            tempView.setTranslationY(moveDistance * 2);
            tempView.setAlpha(0f);
            tempView.setText("");
        }
    }

    //    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        Log.d(TAG, "onAttachedToWindow");
//        // 界面可见时自动开始检查
//        if (!isShowing && !textQueue.isEmpty()) {
//            isShowing = true;
//        }
//    }
//
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        // 界面移除时清空队列，避免内存泄漏
        clear();
    }

}
