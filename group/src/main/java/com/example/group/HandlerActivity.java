package com.example.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class HandlerActivity extends AppCompatActivity {
    private static final String TAG = "HandlerActivity";
    private Handler mMainHandler, mWorkHandler;
    private HandlerThread mHandlerThread;
    private TextView mTextView;
    private Button mButton1, mButton2, mButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        //显示文本
        mTextView = findViewById(R.id.textview);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        //创建与主线程关联的Handler
        mMainHandler = new Handler();
        //步骤1：创建HandlerThread实例对象
        mHandlerThread = new HandlerThread("handlerThread");
        //步骤2：启动线程
        mHandlerThread.start();
        //步骤3：创建工作线程Handler&&复写handleMessage
        mWorkHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        //通过主线程Handler.post方法进行在主线程的UI更新操作
                        mMainHandler.post(() -> mTextView.setText("我爱学习"));
                        break;
                    case 2:
                        //通过主线程Handler.post方法进行在主线程的UI更新操作
                        mMainHandler.post(() -> mTextView.setText("我不喜欢学习"));
                        break;
                    default:
                        break;
                }
            }
        };
        //步骤4：使用工作线程Handler向工作线程的消息队列发送消息
        mButton1.setOnClickListener(v -> {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = "A";
            mWorkHandler.sendMessage(msg);
        });
        mButton2.setOnClickListener(v -> {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = "B";
            mWorkHandler.sendMessage(msg);
        });
        //步骤5：退出线程
        mButton3.setOnClickListener(v -> mHandlerThread.quit());
    }
}
