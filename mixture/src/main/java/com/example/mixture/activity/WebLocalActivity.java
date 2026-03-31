package com.example.mixture.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.mixture.R;

/**
 * Created by test on 2017/12/11.
 */
@SuppressLint("SetTextI18n")
public class WebLocalActivity extends AppCompatActivity {
    private String mFilePath = "file:///android_asset/html/index.html";
    private WebView wv_assets_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_local);
        TextView tv_web_path = findViewById(R.id.tv_web_path);
        //从布局文件中获取名叫wv_assets_web的网页视图
        wv_assets_web = findViewById(R.id.wv_assets_web);
        tv_web_path.setText("下面网页来源于资产文件：" + mFilePath);
        //命令网页视图加载指定路径的网页
        wv_assets_web.loadUrl(mFilePath);
        //给网页视图设置默认的网页浏览客户端
        wv_assets_web.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理WebView资源,防止内存泄漏
        if (wv_assets_web != null) {
            wv_assets_web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wv_assets_web.clearHistory();
            wv_assets_web.clearCache(true);
            wv_assets_web.onPause();
            wv_assets_web.removeAllViews();
            wv_assets_web.destroy();
            wv_assets_web = null;
        }
    }
}
