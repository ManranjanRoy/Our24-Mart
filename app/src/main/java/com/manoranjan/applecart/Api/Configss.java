package com.manoranjan.applecart.Api;

public class Configss {

        //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public  static  final  String tokencode="123";
    public  static  final  String login_id="";

    public  static  final  String Uname="h";
    public  static  final  String Email_id="fh";
    public  static  final  String mobileno="hyjty";
    public  static  final  String login_role="0";


    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";



}
