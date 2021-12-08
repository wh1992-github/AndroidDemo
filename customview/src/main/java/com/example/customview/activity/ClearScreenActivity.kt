package com.example.customview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.customview.R
import com.example.customview.widget.ClearScreenMode
import com.example.customview.widget.ClearScreenView
import kotlinx.android.synthetic.main.activity_clear_screen.*

class ClearScreenActivity : AppCompatActivity(), ClearScreenView.OnClearScreenListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clear_screen)

        clear_screen_container.addClearView(iv_clear_content)

        clear_screen_container.setOnClearScreenListener(this)

        btn_quick_clear.setOnClickListener { clear_screen_container.clearScreenMode = ClearScreenMode.QUICK_SCROLL }
        btn_slow_clear.setOnClickListener { clear_screen_container.clearScreenMode = ClearScreenMode.SLOW_SCROLL }
    }

    override fun onCleared() {
        Toast.makeText(this, "清屏了", Toast.LENGTH_SHORT).show()
    }

    override fun onRestored() {
        Toast.makeText(this, "恢复了", Toast.LENGTH_SHORT).show()
    }
}