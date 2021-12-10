package com.example.customview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.widget.DragBallView;

public class DragBallActivity extends AppCompatActivity {
    private Button resetBtn, msgCountBtn;
    private DragBallView dragBallView;
    private EditText msgCountEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ball);
        resetBtn = findViewById(R.id.reset_btn);
        msgCountBtn = findViewById(R.id.msg_count_btn);
        dragBallView = findViewById(R.id.drag_ball_view);
        msgCountEt = findViewById(R.id.msg_count_et);
        resetBtn.setOnClickListener(v -> dragBallView.reset());
        msgCountBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(msgCountEt.getText().toString().trim())) {
                int count = Integer.parseInt(msgCountEt.getText().toString().trim());
                dragBallView.setMsgCount(count);
            }
        });
        dragBallView.setOnDragBallListener(() -> Toast.makeText(DragBallActivity.this, "消失了", Toast.LENGTH_SHORT).show());
    }
}
