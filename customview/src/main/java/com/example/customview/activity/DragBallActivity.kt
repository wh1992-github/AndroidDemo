package com.example.customview.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityDragBallBinding
import com.example.customview.widget.DragBallView

/**
 * 用于展示 Drag Ball 功能的 Activity。
 */
open class DragBallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDragBallBinding
    private lateinit var resetBtn: Button
    private lateinit var msgCountBtn: Button
    private lateinit var dragBallView: DragBallView
    private lateinit var msgCountEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragBallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resetBtn = binding.resetBtn
        msgCountBtn = binding.msgCountBtn
        dragBallView = binding.dragBallView
        msgCountEt = binding.msgCountEt
        resetBtn.setOnClickListener { dragBallView.reset() }
        msgCountBtn.setOnClickListener {
            val text = msgCountEt.text.toString().trim()
            if (!TextUtils.isEmpty(text)) {
                dragBallView.setMsgCount(text.toInt())
            }
        }
        dragBallView.setOnDragBallListener {
            Toast.makeText(this@DragBallActivity, "消失了", Toast.LENGTH_SHORT).show()
        }
    }
}
