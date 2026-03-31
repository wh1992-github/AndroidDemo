package com.example.network.bean;
/**
 * 封装 Friend 相关逻辑的类。
 */

public class Friend {
    public String device_id;
    public String nick_name;
    public String login_time;

    public Friend(String device_id, String nick_name, String login_time) {
        this.device_id = device_id;
        this.nick_name = nick_name;
        this.login_time = login_time;
    }

}
