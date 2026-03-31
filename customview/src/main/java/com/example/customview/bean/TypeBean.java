package com.example.customview.bean;
/**
 * 用于描述 Type 数据的实体类。
 */

public class TypeBean {
    private String title;
    private int type;

    public TypeBean(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
