package com.manoranjan.applecart.Response;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String messages;

   /* @SerializedName("result")
    private List<Country> result;

    public List<Country> getResult() {
        return result;
    }*/

    public String getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }
}
