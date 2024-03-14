package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.example.group.databinding.ActivityHandlerBinding;
import com.example.group.util.LogUtil;

import java.lang.reflect.Method;

//Handler机制, 同步屏障
@SuppressLint({"LogNotTimber", "DiscouragedPrivateApi"})
@RequiresApi(api = Build.VERSION_CODES.M)
public class HandlerActivity extends AppCompatActivity {
    private static final String TAG = "HandlerActivity";
    public static final int MESSAGE_TYPE_SYNC = 1;
    public static final int MESSAGE_TYPE_ASYN = 2;
    private ActivityHandlerBinding mViewBinding;
    private Handler mMainHandler, mWorkHandler;
    private HandlerThread mHandlerThread;
    private Handler mSyncHandler;
    private int mSyncToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityHandlerBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        //创建与主线程关联的Handler
        mMainHandler = new Handler();
        //步骤1：创建HandlerThread实例对象
        mHandlerThread = new HandlerThread("HandlerThread");
        //步骤2：启动线程
        mHandlerThread.start();
        //步骤3：创建工作线程Handler&&复写handleMessage
        mWorkHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        //通过主线程Handler.post方法进行在主线程的UI更新操作
                        mMainHandler.post(() -> mViewBinding.textview.setText("我爱学习"));
                        break;
                    case 2:
                        //通过主线程Handler.post方法进行在主线程的UI更新操作
                        mMainHandler.post(() -> mViewBinding.textview.setText("我不喜欢学习"));
                        break;
                    default:
                        break;
                }
            }
        };
        mSyncHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_TYPE_SYNC) {
                    LogUtil.i(TAG, "收到普通消息");
                } else if (msg.what == MESSAGE_TYPE_ASYN) {
                    LogUtil.i(TAG, "收到异步消息");
                }
            }
        };

        //步骤4：使用工作线程Handler向工作线程的消息队列发送消息
        mViewBinding.button1.setOnClickListener(v -> {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = "A";
            mWorkHandler.sendMessage(msg);
        });
        mViewBinding.button2.setOnClickListener(v -> {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = "B";
            mWorkHandler.sendMessage(msg);
        });
        //步骤5：退出线程
        mViewBinding.button3.setOnClickListener(v -> mHandlerThread.quit());

        mViewBinding.button5.setOnClickListener(v -> sendSyncBarrier());
        mViewBinding.button6.setOnClickListener(v -> removeSyncBarrier());
        mViewBinding.button7.setOnClickListener(v -> sendSyncMessage());
        mViewBinding.button8.setOnClickListener(v -> sendAsynMessage());

        initHandler();
    }


    private void initHandler() {
        new Thread(() -> {
            Looper.prepare();

            Looper.loop();
        }).start();
    }

    //往消息队列插入同步屏障
    public void sendSyncBarrier() {
        try {
            LogUtil.i(TAG, "sendSyncBarrier: 插入同步屏障");
            MessageQueue queue = mSyncHandler.getLooper().getQueue();
            Method method = queue.getClass().getDeclaredMethod("postSyncBarrier");
            method.setAccessible(true);
            mSyncToken = (int) method.invoke(queue);
            LogUtil.i(TAG, "sendSyncBarrier: token = " + mSyncToken);
        } catch (Exception e) {
            LogUtil.e(TAG, "sendSyncBarrier: e = " + e.getMessage());
        }
    }

    //移除屏障
    public void removeSyncBarrier() {
        try {
            LogUtil.i(TAG, "removeSyncBarrier: 移除同步屏障");
            MessageQueue queue = mSyncHandler.getLooper().getQueue();
            Method method = queue.getClass().getDeclaredMethod("removeSyncBarrier", int.class);
            method.setAccessible(true);
            method.invoke(queue, mSyncToken);
            LogUtil.i(TAG, "removeSyncBarrier: token = " + mSyncToken);
        } catch (Exception e) {
            LogUtil.e(TAG, "removeSyncBarrier: e = " + e.getMessage());
        }
    }

    //往消息队列插入普通消息
    public void sendSyncMessage() {
        LogUtil.i(TAG, "发送普通消息");
        Message message = Message.obtain();
        message.what = MESSAGE_TYPE_SYNC;
        mSyncHandler.sendMessageDelayed(message, 1000);
    }

    //往消息队列插入异步消息
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void sendAsynMessage() {
        LogUtil.i(TAG, "发送异步消息");
        Message message = Message.obtain();
        message.what = MESSAGE_TYPE_ASYN;
        message.setAsynchronous(true);
        mSyncHandler.sendMessageDelayed(message, 1000);
    }
}