package com.example.custom.bean;
/**
 * 封装 Friend 相关逻辑的类。
 */

public class Friend {
    public String phone;
    public String relation;
    public String value;
    public boolean admit_circle;

    public Friend(String phone) {
        this.phone = phone;
        relation = "其他";
        value = "";
        admit_circle = true;
    }

}
