package com.example.customview.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R
import com.example.customview.databinding.ActivityMacBinding
import com.example.customview.loadingview.mac.LVComputer
import com.example.customview.loadingview.mac.LVComputerDesktop
import com.example.customview.loadingview.mac.LVComputerIpad

/**
 * 用于展示 Loading View Mac 功能的 Activity。
 */
open class LoadingViewMacActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMacBinding
    private lateinit var mLVComputerDesktop: LVComputerDesktop
    private lateinit var mLVComputer: LVComputer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMacBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mLVComputerDesktop = binding.lvComputerDesktop
        mLVComputer = binding.lvComputer
    }

    fun startAnim(v: View) {
        when (v.id) {
            R.id.lv_computer_ipad -> {
                (v as LVComputerIpad).startAnim(4000)
                mLVComputerDesktop.startAnim(4000)
                mLVComputer.startAnim(4000)
            }
            R.id.lv_computer_desktop -> (v as LVComputerDesktop).startAnim(4000)
            R.id.lv_computer -> (v as LVComputer).startAnim(6000)
        }
    }
}
