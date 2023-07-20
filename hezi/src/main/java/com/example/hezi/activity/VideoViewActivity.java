package com.example.hezi.activity;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hezi.R;
import com.example.hezi.databinding.ActivityVideoViewBinding;
import com.example.hezi.drawable.RoundRectDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class VideoViewActivity extends AppCompatActivity {
    private static final String TAG = "VideoViewActivity";
    private ActivityVideoViewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVideoViewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ColorStateList colorStateList = getColorStateList(R.color.video_color_bg);
        RoundRectDrawable mRoundRectDrawable = new RoundRectDrawable(colorStateList, 20);
        mBinding.videoView.setBackground(mRoundRectDrawable);
        mBinding.videoView.setClipToOutline(true);
        mBinding.videoView.setElevation(0);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.asd;
        mBinding.videoView.setVideoURI(Uri.parse(uri));
        //监听视频播放完的代码
        mBinding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
        //添加字幕
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("subtitle.vtt");
            MediaFormat mediaFormat = MediaFormat.createSubtitleFormat(MediaFormat.MIMETYPE_TEXT_VTT, Locale.getDefault().getLanguage());
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar);
            mediaFormat.setInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, 1);
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 3);
            mBinding.videoView.addSubtitleSource(inputStream, mediaFormat);
        } catch (IOException e) {
            Log.i(TAG, "init: " + e.getMessage());
        }
        mBinding.videoView.start();
    }

}
