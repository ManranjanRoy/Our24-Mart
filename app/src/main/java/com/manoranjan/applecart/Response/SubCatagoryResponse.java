package com.manoranjan.applecart.Response;

import com.google.gson.annotations.SerializedName;
import com.manoranjan.applecart.model.CatagoryModel;
import com.manoranjan.applecart.model.SUbcatagoryModel;

import java.util.List;

public class SubCatagoryResponse {

    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private List<SUbcatagoryModel> data;


    public String getStatus() {
        return status;
    }

    public List<SUbcatagoryModel> getData() {
        return data;
    }
}
