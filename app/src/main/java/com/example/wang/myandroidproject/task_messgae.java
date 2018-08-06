package com.example.wang.myandroidproject;

/**
 * Created by Willam on 2018/5/12.
 */

public class task_messgae {
    public String itemtitle;
    public String itemContent;
    public String sort;
    public int price;
    public int id;
    public task_messgae(){
    }
    public task_messgae(String itemtitle, String itemContent, String sort, int price, int id){
        this.itemtitle=itemtitle;
        this.itemContent=itemContent;
        this.sort=sort;
        this.price=price;
        this.id=id;
    }
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
