package com.example.storage.bean;
/**
 * 用于描述 Cart 数据的实体类。
 */

public class CartInfo {
    public long rowid;
    public int xuhao;
    public long goods_id;
    public int count;
    public String update_time;

    public CartInfo() {
        rowid = 0L;
        xuhao = 0;
        goods_id = 0L;
        count = 0;
        update_time = "";
    }
}
