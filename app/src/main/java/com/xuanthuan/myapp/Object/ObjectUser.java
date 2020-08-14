package com.xuanthuan.myapp.Object;

public class ObjectUser {
    String id, name, email, urlimg;


    public ObjectUser() {
    }

    public ObjectUser(String id, String name, String email, String urlimg) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.urlimg = urlimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }
}
