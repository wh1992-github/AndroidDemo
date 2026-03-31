package com.example.network.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.network.R;
import com.example.network.thread.MessageTransmit;
import com.example.network.util.DateUtil;

import java.util.Locale;

/**
 * Created by test on 2017/11/11.
 */
public class SocketActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "SocketActivity";
    private EditText et_socket;
    private TextView tv_socket;  // 改为实例变量,防止内存泄漏
    private MessageTransmit mTransmit; //声明一个消息传输对象
    private Handler mHandler;  // 改为实例Handler,防止内存泄漏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        et_socket = findViewById(R.id.et_socket);
        tv_socket = findViewById(R.id.tv_socket);
        findViewById(R.id.btn_socket).setOnClickListener(this);

        // 创建实例Handler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: " + msg.obj);
                if (tv_socket != null) {
                    //拼接服务器的应答字符串
                    String desc = String.format(Locale.getDefault(), "%s 收到服务器的应答消息：%s",
                            DateUtil.getNowTime(), msg.obj.toString());
                    tv_socket.setText(desc);
                }
            }
        };

        mTransmit = new MessageTransmit(mHandler); //创建一个消息传输，传入Handler
        new Thread(mTransmit).start(); //启动消息传输线程
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_socket) {
            //获得一个默认的消息对象
            Message msg = Message.obtain();
            msg.obj = et_socket.getText().toString(); //消息内容
            //通过消息线程的发送处理器,向后端发送消息
            if (mTransmit != null && mTransmit.mSendHandler != null) {
                mTransmit.mSendHandler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理Handler，防止内存泄漏
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        // 清理MessageTransmit的Handler
        if (mTransmit != null && mTransmit.mSendHandler != null) {
            mTransmit.mSendHandler.removeCallbacksAndMessages(null);
        }
    }

}

