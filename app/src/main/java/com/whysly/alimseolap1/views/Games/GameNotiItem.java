package com.whysly.alimseolap1.views.Games;

import androidx.room.PrimaryKey;

public class GameNotiItem {
    String app_name;
    String notitext;
    String noti_date;
    String notititle;
    int item_id;

    public GameNotiItem(String package_name, String app_name, String notititle, String notitext, String noti_date, int item_id) {
        this.app_name = app_name;
        this.notititle = notititle;
        this.noti_date = noti_date;
        this.notitext = notitext;
        this.item_id = item_id;

    }
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getNoti_date() {
        return noti_date;
    }

    public void setNoti_date(String noti_date) {
        this.noti_date = noti_date;
    }

    public String getNotititle() {
        return notititle;
    }

    public void setNotititle(String notititle) {
        this.notititle = notititle;
    }

    public String getName() {
        return app_name;
    }

    public void setName(String app_name) {
        this.app_name = app_name;
    }

    public String getNotitext() {
        return notitext;
    }

    public void setNotitext(String notitext) {
        this.notitext = notitext;
    }
}