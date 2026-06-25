package com.example.customview.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityPregressBarBinding
import com.example.customview.widget.CircleProgressBarView
import com.example.customview.widget.HorizontalProgressBar
import com.example.customview.widget.LoadingLineView
import com.example.customview.widget.LoadingView
import com.example.customview.widget.ProductProgressBar
import com.example.customview.widget.StudyPlanProgressView

/**
 * 用于展示 Progress Bar 功能的 Activity。
 */
open class ProgressBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPregressBarBinding
    private lateinit var circleProgressBarView: CircleProgressBarView
    private lateinit var horizontalProgressBar: HorizontalProgressBar
    private lateinit var productProgressBar: ProductProgressBar
    private lateinit var loadingView: LoadingView
    private lateinit var textView: TextView
    private lateinit var loadingLineView: LoadingLineView
    private lateinit var button: Button
    private lateinit var studyPlanProgressView: StudyPlanProgressView

    private val progressListener = CircleProgressBarView.ProgressListener { currentProgress ->
        textView.text = "当前进度：$currentProgress"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPregressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        circleProgressBarView = binding.circleProgressView
        horizontalProgressBar = binding.horizontalProgressView
        productProgressBar = binding.productProgressView
        loadingView = binding.loadingView
        loadingLineView = binding.loadingLineView
        textView = binding.progressTv
        button = binding.startAnimationBtn
        studyPlanProgressView = binding.studyPlanProgressView

        circleProgressBarView.setProgressWithAnimation(60f)
        circleProgressBarView.setProgressListener(progressListener)
        circleProgressBarView.startProgressAnimation()

        horizontalProgressBar
            .setProgressWithAnimation(60f)
            .setProgressListener(HorizontalProgressBar.ProgressListener { })
        horizontalProgressBar.startProgressAnimation()

        productProgressBar
            .setProgress(60f)
            .setProgressListener(ProductProgressBar.ProgressListener { currentProgress ->
                Log.i(TAG, "currentProgressListener: $currentProgress")
            })

        loadingView.startAnimation()

        button.setOnClickListener {
            loadingLineView.startLoading()
            loadingView.startAnimation()
            horizontalProgressBar.setProgressWithAnimation(100f)
            productProgressBar.setProgress(100f)
            circleProgressBarView.setProgressWithAnimation(60f).startProgressAnimation()
            circleProgressBarView.setProgressListener(progressListener)
            studyPlanProgressView.setData(getPlanData(true))
        }
        studyPlanProgressView.setData(getPlanData(false))
    }

    private fun getPlanData(isAll: Boolean): List<String> =
        mutableListOf("08月10日", "08月11日", "08月12日", "08月13日").apply {
            if (isAll) {
                add("08月14日")
                add("08月15日")
                add("08月16日")
            }
        }

    override fun onResume() {
        super.onResume()
        circleProgressBarView.resumeProgressAnimation()
    }

    override fun onPause() {
        super.onPause()
        circleProgressBarView.pauseProgressAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        circleProgressBarView.stopProgressAnimation()
    }

    companion object {
        private const val TAG = "ProgressBarActivity"
    }
}
