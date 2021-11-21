package com.example.util;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class AndroidApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fresco.initialize(this.getApplicationContext(), FrescoConfigUtil.getDefaultImagePipelineConfig(this));
    }

    public static Context getContext() {
        return sContext;
    }
}
