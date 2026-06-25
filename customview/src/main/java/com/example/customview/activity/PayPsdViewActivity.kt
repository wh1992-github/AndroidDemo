package com.example.customview.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityPayPsdViewBinding
import com.example.customview.widget.PayPsdInputView

/**
 * 用于展示 Pay Psd View 功能的 Activity。
 */
open class PayPsdViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayPsdViewBinding
    private lateinit var passwordInputView: PayPsdInputView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayPsdViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passwordInputView = binding.password

        passwordInputView.setComparePassword(object : PayPsdInputView.onPasswordListener {
            override fun onDifference(oldPsd: String, newPsd: String) {
                Toast.makeText(
                    this@PayPsdViewActivity,
                    "两次密码输入不同$oldPsd!=$newPsd",
                    Toast.LENGTH_SHORT
                ).show()
                passwordInputView.cleanPsd()
            }

            override fun onEqual(psd: String) {
                Toast.makeText(this@PayPsdViewActivity, "密码相同$psd", Toast.LENGTH_SHORT).show()
                passwordInputView.setComparePassword("")
                passwordInputView.cleanPsd()
            }

            override fun inputFinished(inputPsd: String) {
                Toast.makeText(this@PayPsdViewActivity, "输入完毕：$inputPsd", Toast.LENGTH_SHORT).show()
                passwordInputView.setComparePassword(inputPsd)
            }
        })
    }
}
