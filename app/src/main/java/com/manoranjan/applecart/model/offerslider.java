package com.manoranjan.applecart.model;

public class offerslider {
    int id;
    String url;
    int length;

    public offerslider() {
    }

    public offerslider(int id, String url, int length) {
        this.id = id;
        this.url = url;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
