package com.manoranjan.applecart.Response;

import com.manoranjan.applecart.model.Profile;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String messages;

    @SerializedName("data")
    private List<Profile> data;

    public List<Profile> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }
}
