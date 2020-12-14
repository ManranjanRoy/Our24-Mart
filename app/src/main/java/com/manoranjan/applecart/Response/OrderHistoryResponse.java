package com.manoranjan.applecart.Response;

import com.google.gson.annotations.SerializedName;
import com.manoranjan.applecart.model.CatagoryModel;
import com.manoranjan.applecart.model.OrderHistoryModel;

import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private List<OrderHistoryModel> data;


    public String getStatus() {
        return status;
    }

    public List<OrderHistoryModel> getData() {
        return data;
    }
}
