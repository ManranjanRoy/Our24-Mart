package com.manoranjan.applecart.presenter;

public interface SignupPresenter {
    // void validatefield(String email,String password);
    ////fname,lname,email,monileno,address1,address2,town,pinciode,password,cpassword
    void signup(String fname, String lname, String email, String mobileno, String address1, String address2,
                String town, String pincode, String password, String cpassword);
}