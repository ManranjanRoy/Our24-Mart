package com.manoranjan.applecart.model;

public class AddressModel {

    //{"id":"4","customer_id":"16","firstname":"Manoranjan","lastname":"roy",
    // "town":"Howrah","mobile":"+919062149","email":"mannu13896@gmail.com","pincode":"711106","address_1":"20","address_2":"c road Bamangachi salkia",
    // "state":"West Bengal","status":"0","created_date":"0000-00-00 00:00:00"}
    private String id;

    private String customer_id;

    private String firstname;

    private String lastname;

    private String town;

    private String mobile;

    private String email;

    private String pincode;

    private String address_1;
    private String address_2;

    private String state;
    private  String status;

    private String created_date;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setCustomer_id(String customer_id){
        this.customer_id = customer_id;
    }
    public String getCustomer_id(){
        return this.customer_id;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public String getFirstname(){
        return this.firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public String getLastname(){
        return this.lastname;
    }
    public void setTown(String town){
        this.town = town;
    }
    public String getTown(){
        return this.town;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPincode(String pincode){
        this.pincode = pincode;
    }
    public String getPincode(){
        return this.pincode;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
