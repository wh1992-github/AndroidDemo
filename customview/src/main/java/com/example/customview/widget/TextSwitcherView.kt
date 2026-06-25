package com.example.customview.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import java.util.ArrayDeque
import java.util.Queue

/**
 * @author Daisw
 */
class TextSwitcherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var offsetMove = 0
    private var preView: TextView
    private var currentView: TextView
    private var nextView: TextView
    private var tempView: TextView
    private val textQueue: Queue<String> = ArrayDeque()
    private var displayDuration = 200L
    private var shortAnimDuration = 400L
    private var longAnimDuration = 600L
    private var maxDisplayTime = 2000L
    private var textSize = 60f
    private val handler = Handler(Looper.getMainLooper())
    private var isShowing = false
    private val checkQueueRunnable = Runnable { checkAndDisplayNextText() }
    private var waitCount = 0
    private var animatorSet: AnimatorSet? = null
    private var currentTextBean: String? = null

    init {
        preView = createTextView()
        currentView = createTextView()
        nextView = createTextView()
        tempView = createTextView()
        initTextSwitcher()
    }

    private fun initTextSwitcher() {
        preView.translationY = -100f
        preView.alpha = 0f

        currentView.translationY = 0f
        currentView.alpha = 1f

        nextView.translationY = 100f
        nextView.alpha = 0f

        tempView.translationY = 200f
        tempView.alpha = 0f

        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        offsetMove = 120
        params.gravity = Gravity.CENTER
        addView(preView, params)
        addView(currentView, params)
        addView(nextView, params)
        addView(tempView, params)
    }

    private fun createTextView(): TextView =
        TextView(context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            setTextColor(Color.parseColor("#000000"))
            setSingleLine(true)
            ellipsize = TextUtils.TruncateAt.END
            setTypeface(null, Typeface.NORMAL)
            gravity = Gravity.CENTER
        }

    fun addText(displayNotify: String?) {
        if (displayNotify == null) {
            return
        }
        Log.d(TAG, "addText: $displayNotify, queue size: ${textQueue.size}")
        textQueue.offer(displayNotify)

        if (!isShowing && textQueue.isNotEmpty()) {
            isShowing = true
            Log.d(TAG, "Starting to show texts")
            handler.post(checkQueueRunnable)
        }
    }

    private fun checkAndDisplayNextText() {
        Log.d(TAG, "checkAndDisplayNextText, queue size: ${textQueue.size}")
        if (textQueue.isEmpty()) {
            waitCount++
            if (waitCount * displayDuration >= maxDisplayTime) {
                Log.d(TAG, "Max display time reached, stopping loop, calling onFinish")
                clear()
                return
            }

            Log.d(TAG, "Queue is empty, waiting 200ms")
            handler.postDelayed(checkQueueRunnable, displayDuration)
            return
        }

        val nextText = textQueue.poll() ?: run {
            handler.postDelayed(checkQueueRunnable, displayDuration)
            return
        }
        currentTextBean = nextText

        Log.d(TAG, "Animating text immediately: $nextText")
        waitCount = 0
        val animDuration = if (textQueue.isEmpty()) longAnimDuration else shortAnimDuration
        animateTextSwitch(nextText, animDuration)
    }

    private fun getMoveDistance(): Float = 100f

    private fun animateTextSwitch(text: String, animDuration: Long) {
        Log.d(TAG, "animateTextSwitch: $text, duration: $animDuration, remaining queue: ${textQueue.size}")

        tempView.text = text
        val moveDistance = getMoveDistance()
        tempView.translationY = moveDistance * 2
        tempView.alpha = 1f

        val preTranslateAnimator = ObjectAnimator.ofFloat(preView, "translationY", -moveDistance, -moveDistance * 2)
        val preAlphaAnimator = ObjectAnimator.ofFloat(preView, "alpha", 1f, 0f)
        val currentTranslateAnimator = ObjectAnimator.ofFloat(currentView, "translationY", 0f, -moveDistance)
        val currentAlphaAnimator = ObjectAnimator.ofFloat(currentView, "alpha", 1f, 0.5f)
        val nextTranslateAnimator = ObjectAnimator.ofFloat(nextView, "translationY", moveDistance, 0f)
        val nextAlphaAnimator = ObjectAnimator.ofFloat(nextView, "alpha", 0.5f, 1f)
        val tempTranslateAnimator = ObjectAnimator.ofFloat(tempView, "translationY", moveDistance * 2, moveDistance)
        val tempAlphaAnimator = ObjectAnimator.ofFloat(tempView, "alpha", 0f, 0.5f)

        animatorSet = AnimatorSet().apply {
            duration = animDuration
            playTogether(
                preTranslateAnimator,
                preAlphaAnimator,
                currentTranslateAnimator,
                currentAlphaAnimator,
                nextTranslateAnimator,
                nextAlphaAnimator,
                tempTranslateAnimator,
                tempAlphaAnimator
            )
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    Log.d(TAG, "Animation ended, switching views")
                    val temp = preView
                    preView = currentView
                    currentView = nextView
                    nextView = tempView
                    tempView = temp

                    tempView.translationY = moveDistance * 2
                    tempView.alpha = 0f
                    tempView.text = ""

                    handler.postDelayed(checkQueueRunnable, displayDuration)
                }
            })
            start()
        }
    }

    fun clear() {
        Log.d(TAG, "clear")
        animatorSet?.cancel()

        currentTextBean = null
        textQueue.clear()
        handler.removeCallbacks(checkQueueRunnable)
        isShowing = false
        waitCount = 0

        val moveDistance = getMoveDistance()
        preView.translationY = -moveDistance
        preView.alpha = 0.5f
        preView.text = ""

        currentView.translationY = 0f
        currentView.alpha = 1f
        currentView.text = ""

        nextView.translationY = moveDistance
        nextView.alpha = 0.5f
        nextView.text = ""

        tempView.translationY = moveDistance * 2
        tempView.alpha = 0f
        tempView.text = ""
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow")
        clear()
    }

    companion object {
        private const val TAG = "TextSwitcherView"
    }
}
