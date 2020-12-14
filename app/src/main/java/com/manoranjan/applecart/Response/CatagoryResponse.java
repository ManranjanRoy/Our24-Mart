package com.manoranjan.applecart.Response;

import com.manoranjan.applecart.model.CatagoryModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatagoryResponse {

    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private List<CatagoryModel> data;


    public String getStatus() {
        return status;
    }

    public List<CatagoryModel> getData() {
        return data;
    }
}
