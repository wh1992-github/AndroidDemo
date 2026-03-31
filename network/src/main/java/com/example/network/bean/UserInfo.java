package com.example.network.bean;
/**
 * 用于描述 User 数据的实体类。
 */

public class UserInfo {
    public String name;
    public int age;
    public long height;
    public float weight;
    public boolean married;

    public UserInfo() {
        name = "";
        age = 0;
        height = 0L;
        weight = 0.0f;
        married = false;
    }
}
