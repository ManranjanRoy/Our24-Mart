package com.manoranjan.applecart.view;

import com.manoranjan.applecart.model.ProductListModel;

import java.util.List;

public interface ProductListView {
    void Showprogess();
    void dismissproggress();
    void getproduct(List<ProductListModel> productListModels);
}
