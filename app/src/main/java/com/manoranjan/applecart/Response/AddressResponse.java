package com.manoranjan.applecart.Response;

import com.manoranjan.applecart.model.AddressModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<AddressModel> data;

    public String getStatus() {
        return status;
    }

    public List<AddressModel> getData() {
        return data;
    }
}
