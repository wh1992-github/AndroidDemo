package com.example.customview.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.example.customview.databinding.ActivityFingerPrintBinding
import com.example.customview.utils.FingerprintUtil

/**
 * 用于展示 Fingerprint 功能的 Activity。
 */
open class FingerprintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFingerPrintBinding
    private lateinit var mResultTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mResultTv = binding.resultTv

        binding.openFingerBtn.setOnClickListener {
            openFinger()
        }

        binding.cancelFingerBtn.setOnClickListener {
            FingerprintUtil.cancel()
        }
    }

    private fun openFinger() {
        FingerprintUtil.callFingerPrint(this, object : FingerprintUtil.OnCallBackListener {
            override fun onSupportFailed() {
                Toast.makeText(this@FingerprintActivity, "当前设备不支持指纹", Toast.LENGTH_LONG)
                    .show()
                Log.i(TAG, "当前设备不支持指纹")
            }

            override fun onInsecurity() {
                Toast.makeText(
                    this@FingerprintActivity,
                    "当前设备未处于安全保护中",
                    Toast.LENGTH_LONG
                ).show()
                Log.i(TAG, "当前设备未处于安全保护中")
            }

            override fun onEnrollFailed() {
                Toast.makeText(this@FingerprintActivity, "请到设置中设置指纹", Toast.LENGTH_LONG)
                    .show()
                Log.i(TAG, "请到设置中设置指纹")
            }

            override fun onAuthenticationStart() {
                Toast.makeText(this@FingerprintActivity, "验证开始", Toast.LENGTH_LONG).show()
                Log.i(TAG, "onAuthenticationStart: ")
            }

            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                Toast.makeText(this@FingerprintActivity, errString, Toast.LENGTH_LONG).show()
                Log.i(TAG, "onAuthenticationError: ")
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(this@FingerprintActivity, "解锁失败", Toast.LENGTH_LONG).show()
                Log.i(TAG, "解锁失败")
            }

            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                Toast.makeText(this@FingerprintActivity, helpString, Toast.LENGTH_LONG).show()
                Log.i(TAG, "onAuthenticationHelp: ")
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                Toast.makeText(this@FingerprintActivity, "解锁成功", Toast.LENGTH_LONG).show()
                Log.i(TAG, "解锁成功")
                mResultTv.text = result!!.javaClass.name
            }
        })
    }

    companion object {
        private const val TAG = "FingerprintActivity"
    }
}
