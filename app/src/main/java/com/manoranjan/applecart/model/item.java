package com.manoranjan.applecart.model;

public class item
{
    String name;
    boolean isselected;

    public item() {
    }

    public item(String name, boolean isselected) {
        this.name = name;
        this.isselected = isselected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
