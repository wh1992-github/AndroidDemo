package com.example.device.activity;

import android.app.Application;

import com.example.device.util.AdapterUtil;

import me.jessyan.autosize.AutoSizeConfig;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //两种适配方式都行
        resetAutoSize();
        AdapterUtil.setApplicationDensity(this,360);
    }


    private void resetAutoSize() {
        //2、结合AndroidManifest配置生效
        AutoSizeConfig.getInstance().setBaseOnWidth(true);


        //1、结合AndroidManifest配置生效
        //int designWidthInDp = 720;
        //int designHeightInDp = 800;
        //AutoSizeConfig.getInstance().setDesignWidthInDp(designWidthInDp).setDesignHeightInDp(designHeightInDp);
    }

}
