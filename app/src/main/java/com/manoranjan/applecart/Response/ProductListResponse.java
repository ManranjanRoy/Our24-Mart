package com.manoranjan.applecart.Response;

import com.manoranjan.applecart.model.ProductListModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListResponse {

    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private List<ProductListModel> data;


    public String getStatus() {
        return status;
    }

    public List<ProductListModel> getData() {
        return data;
    }
}
