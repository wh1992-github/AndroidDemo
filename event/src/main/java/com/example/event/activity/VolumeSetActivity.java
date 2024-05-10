package com.example.event.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.event.R;
import com.example.event.widget.VolumeDialog;
import com.example.event.widget.VolumeDialog.VolumeAdjustListener;

/**
 * Created by test on 2017/11/23.
 */
@SuppressLint("SetTextI18n")
public class VolumeSetActivity extends AppCompatActivity implements VolumeAdjustListener {
    private TextView mVolumeTv;
    private ProgressBar mProgressBar;
    private VolumeDialog mVolumeDialog; //声明一个音量调节对话框对象
    private AudioManager mAudioManager; //声明一个音量管理器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_set);
        mVolumeTv = findViewById(R.id.tv_volume);
        mProgressBar = findViewById(R.id.progress_bar);
        //从系统服务中获取音量管理器
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mVolumeTv.setText("调节后的音乐音量大小为：" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mProgressBar.setProgress(mProgressBar.getMax() * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }

    //在发生物理按键动作时触发
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
                && event.getAction() == KeyEvent.ACTION_DOWN) { //按下音量加键
            //显示音量调节对话框,并将音量调大一级
            showVolumeDialog(AudioManager.ADJUST_RAISE);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                && event.getAction() == KeyEvent.ACTION_DOWN) { //按下音量减键
            //显示音量调节对话框,并将音量调小一级
            showVolumeDialog(AudioManager.ADJUST_LOWER);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //显示音量调节对话框
    private void showVolumeDialog(int direction) {
        if (mVolumeDialog == null) {
            //创建一个音量调节对话框
            mVolumeDialog = new VolumeDialog(this);
            //设置音量调节对话框的音量调节监听器
            mVolumeDialog.setVolumeAdjustListener(this);
        }
        if (!mVolumeDialog.isShowing()) {
            //显示音量调节对话框
            mVolumeDialog.show();
        }
        //令音量调节对话框按音量方向调整音量
        mVolumeDialog.adjustVolume(direction);
    }

    //在音量调节完成后触发
    @Override
    public void onVolumeAdjust(int volume) {
        mVolumeTv.setText("调节后的音乐音量大小为：" + volume);
        mProgressBar.setProgress(mProgressBar.getMax() * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }

}
