package com.example.group.bean;

/**
 * Created by test on 2019-06-03.
 */
public class User {
    public String userName;
    public String uid;

    public User(String userName, String uid) {
        this.userName = userName;
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
