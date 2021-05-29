package com.example.event.util;

import android.content.Context;
import android.media.AudioManager;

import static android.content.Context.AUDIO_SERVICE;

public class AudioManagerUtil {
    private AudioManager mAudioManager;

    public AudioManagerUtil(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    public void requestAudioFocus() {
        //获得音频焦点
        //第三个参数：告知系统，你要求获得音频焦点的用途，系统根据你所要求的类型来给其他监听者发出相应的焦点控制参数。
        //AUDIOFOCUS_GAIN：我要求完全获得焦点，其他人需要释放焦点。比如我要播放音乐了，这时就要抢占整个音频焦点。
        //AUDIOFOCUS_GAIN_TRANSIENT：只是短暂获得，一会就释放焦点，比如你只是想发个notification时用下一秒不到的铃声。
        //AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK：只是背景获得，之前的音频焦点使用者无需释放焦点给我，我将与其共同使用。
        //AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE：短暂性获得焦点，录音或者语音识别。
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public void abandonAudioFocus() {
        //释放音频焦点
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
    }

    //focusChange主要有以下四种参数：
    //AUDIOFOCUS_GAIN：你已经完全获得了音频焦点
    //AUDIOFOCUS_LOSS：你会长时间的失去焦点，所以不要指望在短时间内能获得。请结束自己的相关音频工作并做好收尾工作。比如另外一个音乐播放器开始播放音乐了（前提是这个另外的音乐播放器他也实现了音频焦点的控制，baidu音乐，天天静听很遗憾的就没有实现，所以他们两个是可以跟别的播放器同时播放的）
    //AUDIOFOCUS_LOSS_TRANSIENT：你会短暂的失去音频焦点，你可以暂停音乐，但不要释放资源，因为你一会就可以夺回焦点并继续使用
    //AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK：你的焦点会短暂失去，但是你可以与新的使用者共同使用音频焦点
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    //重新获得焦点，且符合播放条件，开始播放
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //会长时间失去焦点，直接暂停
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂失去焦点，先暂停，待重新获得音频焦点再重新播放
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂失去焦点，但是你可以与新的使用者共同使用音频焦点
                    break;
                default:
                    break;
            }
        }
    };
}
