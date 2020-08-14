package com.xuanthuan.myapp.Object;

import java.util.Map;

public class ObjectMessenger {
    String  ulr, message, timeMessage, date;

    public ObjectMessenger() {
    }

    public ObjectMessenger(String ulr, String message, String timeMessage, String date) {
        this.ulr = ulr;
        this.message = message;
        this.timeMessage = timeMessage;
        this.date = date;
    }

    public String getUlr() {
        return ulr;
    }

    public void setUlr(String ulr) {
        this.ulr = ulr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
