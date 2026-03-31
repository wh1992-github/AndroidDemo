package com.example.customview.bean;
/**
 * 用于描述 User 数据的实体类。
 */

public class UserBean {
    private String userName;
    private String sortLetters = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
