package com.example.wang.myandroidproject;

/**
 * Created by Willam on 2018/5/24.
 */

public class goods_message {
    public String itemtitle;
    public String itemContent;
    public String sort;
    public int price;
    public int id;
    public goods_message(){}
    public goods_message(String itemtitle, String itemContent, String sort, int price, int id){
        this.itemtitle=itemtitle;
        this.itemContent=itemContent;
        this.sort=sort;
        this.price=price;
        this.id=id;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}