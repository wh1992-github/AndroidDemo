package com.example.storage.bean;
/**
 * 封装 Sms Content 相关逻辑的类。
 */

public class SmsContent {
    public String address;
    public String person;
    public String body;
    public String date;
    public int type;

    public SmsContent() {
        address = "";
        person = "";
        body = "";
        date = "";
        type = 0;
    }
}
