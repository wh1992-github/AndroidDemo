package com.example.test.encrypt;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Base64Util {
    private static final String TAG = "Base64Util";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String base64Encode(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();
        //编码
        String encodeText = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        Log.i(TAG, "base64: encodeText = " + encodeText);
        //解码
        String decodeText = new String(decoder.decode(encodeText), StandardCharsets.UTF_8);
        Log.i(TAG, "base64: decodeText = " + decodeText);

        return encodeText;
    }

}
