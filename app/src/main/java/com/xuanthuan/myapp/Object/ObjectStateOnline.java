package com.xuanthuan.myapp.Object;

public class ObjectStateOnline {
    String date, state, time;

    public ObjectStateOnline() {
    }

    public ObjectStateOnline(String date, String time, String state) {
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
