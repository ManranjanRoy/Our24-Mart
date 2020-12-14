package com.manoranjan.applecart.model;

public class CartMedicineModel {

 //"id": "1",
    //            "title": "new product",
    //            "details": "<p>tester</p>\r\n",
    //            "code": "78552",
    //            "price": "300.00",
    //            "category_name": "Baby Care",
    //            "color": "R.D Medical Center"
 //String id,title,details,code,price,category_name,store_name,quantity,totalprice;
   String id,title,details,code,price,category_name,color,quantity,totalprice;

    public CartMedicineModel() {
    }

    public CartMedicineModel(String id, String title, String details, String code,
                             String price, String category_name, String color, String quantity, String totalprice) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.code = code;
        this.price = price;
        this.category_name = category_name;
        this.color = color;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
