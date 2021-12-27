package com.example.group;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.group.bigpicture.ImageSurfaceView;

import java.io.IOException;

public class BigPictureActivity extends Activity {
    private static final String TAG = "BigPictureActivity";
    private static final String MAP_FILE = "world.jpg";

    private ImageSurfaceView imageSurfaceView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_big_picture);
        imageSurfaceView = findViewById(R.id.worldview);
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
