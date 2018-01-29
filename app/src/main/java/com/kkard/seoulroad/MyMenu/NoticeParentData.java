package com.kkard.seoulroad.MyMenu;

/**
 * Created by SuGeun on 2017-09-05.
 */

public class NoticeParentData {
    private int type;
    private String title;

    public NoticeParentData(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public int getType(){return type;}
    public String getTitle() {
        return title;
    }
}
