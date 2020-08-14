package com.xuanthuan.myapp.Object;

public class ObjectChatAll {
    String nameUser, idUser, imgUser, date, message;

    public ObjectChatAll() {
    }

    public ObjectChatAll(String nameUser, String idUser, String imgUser, String date, String message) {
        this.nameUser = nameUser;
        this.idUser = idUser;
        this.imgUser = imgUser;
        this.date = date;
        this.message = message;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
