package com.myTara567.app;


public class Notice_Model {
    String notice_title;
    String notice_msg;
    String notice_date;

    public Notice_Model() {
    }

    public Notice_Model(String notice_title, String notice_msg, String notice_date) {
        this.notice_title = notice_title;
        this.notice_msg = notice_msg;
        this.notice_date = notice_date;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_msg() {
        return notice_msg;
    }

    public void setNotice_msg(String notice_msg) {
        this.notice_msg = notice_msg;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }
}

