package com.example.wang.myandroidproject;

public class Task {
    private String title;
    private String short_des;
    private int price;
    private String sort;
    private String time;
    private int invisible_id;

    Task(String title, String short_des, int price, String sort, String time, int invisible_id) {
        this.title = title;
        this.short_des = short_des;
        this.price = price;
        this.sort = sort;
        this.time = time;
        this.invisible_id = invisible_id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_des() {
        return short_des;
    }

    public int getPrice() {
        return price;
    }

    public String getSort() {
        return sort;
    }

    public String getTime() {
        return time;
    }

    public int getInvisible_id() {
        return invisible_id;
    }
}
