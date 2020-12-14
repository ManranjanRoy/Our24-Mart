package com.manoranjan.applecart.model;

public class Color {
    private String id;

    private String product_id;

    private String color;

    private  boolean isselected;



    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setProduct_id(String product_id){
        this.product_id = product_id;
    }
    public String getProduct_id(){
        return this.product_id;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getColor(){
        return this.color;
    }
}
