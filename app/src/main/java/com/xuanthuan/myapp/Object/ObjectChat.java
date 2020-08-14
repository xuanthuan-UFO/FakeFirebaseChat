package com.xuanthuan.myapp.Object;

public class ObjectChat {
    String sender, receiver, message, name, urlimgreceiver, date, nameuser, urlimgUser;

    public ObjectChat() {
    }

    public ObjectChat(String sender, String receiver, String message, String name, String urlimgreceiver, String date, String nameuser, String urlimgUser) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.name = name;
        this.urlimgreceiver = urlimgreceiver;
        this.date = date;
        this.nameuser = nameuser;
        this.urlimgUser = urlimgUser;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlimgreceiver() {
        return urlimgreceiver;
    }

    public void setUrlimgreceiver(String urlimgreceiver) {
        this.urlimgreceiver = urlimgreceiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getUrlimgUser() {
        return urlimgUser;
    }

    public void setUrlimgUser(String urlimgUser) {
        this.urlimgUser = urlimgUser;
    }
}
