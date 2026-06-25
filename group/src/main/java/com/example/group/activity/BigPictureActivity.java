package com.example.group.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.group.bigpicture.ImageSurfaceView;
import com.example.group.databinding.ActivityBigPictureBinding;

import java.io.IOException;
/**
 * 用于展示 Big Picture 功能的 Activity。
 */

public class BigPictureActivity extends Activity {
    private ActivityBigPictureBinding binding;
    private static final String TAG = "BigPictureActivity";
    private static final String MAP_FILE = "world.jpg";

    private ImageSurfaceView imageSurfaceView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityBigPictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageSurfaceView = binding.worldview;
        try {
            imageSurfaceView.setInputStream(getAssets().open(MAP_FILE));
            imageSurfaceView.post(() -> {
                imageSurfaceView.setViewportCenter();
                //imageSurfaceView.setViewport(new Point(0, 0));
            });
        } catch (IOException e) {
            Log.e(TAG, "IOException: e = " + e.getMessage());
        }
    }
}
