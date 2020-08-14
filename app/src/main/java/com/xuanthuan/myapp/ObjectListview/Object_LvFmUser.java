package com.xuanthuan.myapp.ObjectListview;

public class Object_LvFmUser {
    String urlimg, name, userId;

    public Object_LvFmUser() {
    }

    public Object_LvFmUser(String urlimg, String name, String userId) {
        this.urlimg = urlimg;
        this.name = name;
        this.userId = userId;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
