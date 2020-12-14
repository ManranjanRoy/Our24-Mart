package com.manoranjan.applecart.model;

public class wishlistmodel {
    //"product_id": "16",
    //            "wishlist_id": "21",
    //            "name": "Fabric product",
    //            "description": "This is description",
    //            "price": "200",
    //            "qty": "1",
    //            "image": "69acb875645fe41afa785e3816dfaf75.jpg",
    //            "delivery_charge": "0",
    //            "created_date": "2020-03-23 09:05:07",
    //            "updated_date": "2020-03-23 09:17:06"
    String product_id,wishlist_id,name,description,price,qty,image,delivery_charge,created_date,updated_date;

    public wishlistmodel() {
    }

    public wishlistmodel(String product_id, String wishlist_id, String name, String description, String price, String qty, String image, String delivery_charge, String created_date, String updated_date) {
        this.product_id = product_id;
        this.wishlist_id = wishlist_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.image = image;
        this.delivery_charge = delivery_charge;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(String wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
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
