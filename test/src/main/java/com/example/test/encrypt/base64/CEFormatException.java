package com.example.test.encrypt.base64;

import java.io.IOException;
/**
 * 封装 CE Format Exception 相关逻辑的类。
 */

public class CEFormatException extends IOException {
    public CEFormatException(String s) {
        super(s);
    }
}
