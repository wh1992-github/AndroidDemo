package com.example.network.bean;
/**
 * 用于描述 Mail 数据的实体类。
 */

public class MailItem {
    public String mail_title;
    public String mail_date;

    public MailItem() {
        this.mail_title = "";
        this.mail_date = "";
    }

    public MailItem(String mail_title, String mail_date) {
        this.mail_title = mail_title;
        this.mail_date = mail_date;
    }

}
