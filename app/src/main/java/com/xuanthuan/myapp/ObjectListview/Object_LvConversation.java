package com.xuanthuan.myapp.ObjectListview;

public class Object_LvConversation {
    String urlimg, nameAcount, message, time;

    public Object_LvConversation(String urlimg, String nameAcount, String message, String time) {
        this.urlimg = urlimg;
        this.nameAcount = nameAcount;
        this.message = message;
        this.time = time;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public String getNameAcount() {
        return nameAcount;
    }

    public void setNameAcount(String nameAcount) {
        this.nameAcount = nameAcount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
