package com.manoranjan.applecart.model;

public class CatagoryModel {
    //"id": "12",
    //            "name": "Fabric",
    //            "image": "911fa479435dc6993ae408690908a300.jpg",
    //            "description": "",
    //            "status": "1",
    //            "created_date": "2020-03-23"
    String id,name,image,status,created_date,description;

    public CatagoryModel() {
    }

    public CatagoryModel(String id, String name, String image, String status, String created_date, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.created_date = created_date;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
