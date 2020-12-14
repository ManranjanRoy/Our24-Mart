package com.manoranjan.applecart.view;

import com.manoranjan.applecart.model.ProductListModel;

import java.util.List;

public interface ProductDetailsView {
    void onSucess(String msg);
    void onError(String msg);
    void showProgress();
    void dismissprogress();
    void getproductdetails(List<ProductListModel> data);

}
