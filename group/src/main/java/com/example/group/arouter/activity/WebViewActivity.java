package com.example.group.arouter.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.group.R;
import com.example.group.arouter.data.ARouterConstants;
/**
 * 用于展示 Web View 功能的 Activity。
 */

@Route(path = ARouterConstants.COM_URL)
public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        this.webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/scheme-test.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理WebView资源,防止内存泄漏
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            webView.onPause();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
