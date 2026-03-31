package com.example.storage.bean;
/**
 * 用于描述 Call 数据的实体类。
 */

public class CallRecord {
    public String name;
    public String phone;
    public int type;
    public String date;
    public long duration;
    public int _new;

    public CallRecord() {
        name = "";
        phone = "";
        type = 0;
        date = "";
        duration = 0;
        _new = 0;
    }

}
