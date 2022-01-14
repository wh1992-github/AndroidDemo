package com.example.test.encrypt;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Base64Util {
    private static final String TAG = "Base64Util";

    public static String base64Encode(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        //编码
        String encodeText = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        Log.i(TAG, "base64: encodeText = " + encodeText);

        return encodeText;
    }

    public static String base64Decode(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        //解码
        String decodeText = new String(decoder.decode(text), StandardCharsets.UTF_8);
        Log.i(TAG, "base64: decodeText = " + decodeText);

        return decodeText;
    }
}
