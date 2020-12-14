package com.manoranjan.applecart.model;

public class SUbcatagoryModel {
    // "id": "2",
    //            "cat_id": "12",
    //            "name": "Fortune",
    //            "image": "eede75e9caad5fd2dc16731c827911a4.jpg",
    //            "created_date": "2020-04-18",
    //            "updated_date": "2020-04-18 04:33:56"
    String id,cat_id,name,image,created_date,updated_date;

    public SUbcatagoryModel() {
    }

    public SUbcatagoryModel(String id, String cat_id, String name, String image, String created_date, String updated_date) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.image = image;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
