package com.example.weixin.bean;
/**
 * 封装 Share Chanels 相关逻辑的类。
 */

public class ShareChanels {
    public String channelName;
    public int channelType;

    public ShareChanels(String name, int type) {
        channelName = name;
        channelType = type;
    }
}
