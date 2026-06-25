package com.example.customview.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R
import com.example.customview.databinding.ActivityWaveViewBinding

open class WaveViewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityWaveViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWaveViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.wakingBtn.setOnClickListener(this)
        mBinding.speakingBtn.setOnClickListener(this)
        mBinding.increaseBtn.setOnClickListener(this)
        mBinding.decreaseBtn.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.waking_btn -> {
                //唤醒动效
                mBinding.gradientVoiceLightView.speaking(false)
            }

            R.id.speaking_btn -> {
                //发话动效
                mBinding.gradientVoiceLightView.speaking(true)
            }

            R.id.increase_btn -> {
                if (mBinding.gradientVoiceLightView.isSpeaking) {
                    mBinding.gradientVoiceLightView.setSoundLevel(mBinding.gradientVoiceLightView.level + 10)
                }
            }

            R.id.decrease_btn -> {
                if (mBinding.gradientVoiceLightView.isSpeaking) {
                    mBinding.gradientVoiceLightView.setSoundLevel(mBinding.gradientVoiceLightView.level - 10)
                }
            }
        }
    }
}
