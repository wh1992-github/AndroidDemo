package com.example.network.bean;

import java.util.ArrayList;
/**
 * 封装 Friend Group 相关逻辑的类。
 */

public class FriendGroup {
    public String title;
    public ArrayList<Friend> friend_list;

    public FriendGroup() {
        this.title = "";
        this.friend_list = new ArrayList<Friend>();
    }

    public FriendGroup(String title, ArrayList<Friend> friend_list) {
        this.title = title;
        this.friend_list = friend_list;
    }

}
