package com.example.network.http.tool;

import android.graphics.Bitmap;
/**
 * 用于描述 Http Resp 数据的实体类。
 */

public class HttpRespData {
    public String content;
    public Bitmap bitmap;
    public String cookie;
    public String err_msg;

    public HttpRespData() {
        content = "";
        bitmap = null;
        cookie = "";
        err_msg = "";
    }
}

