package com.manoranjan.applecart.PresenterImp;

import android.content.Context;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Response.ProductListResponse;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.presenter.ProductListpresentation;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.ProductListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductlistPresenterImp implements ProductListpresentation {

    ProductListView productListView;
    Context context;

    public ProductlistPresenterImp(ProductListView productListView) {
        this.productListView = productListView;
    }

    @Override
    public void getproductlist(final String catid) {
        CountryService countryService=new CountryService();
        productListView.Showprogess();
        countryService.getAPI().getproductlist(ApiLinks.productlist, catid).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                productListView.dismissproggress();
                if (response.body().getStatus().equals("true")){
                    List<ProductListModel> productListModels=response.body().getData();
                    productListView.getproduct(productListModels);
                }
                else{
                    List<ProductListModel> productListModels=new ArrayList<>();
                    productListView.getproduct(productListModels);
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                productListView.dismissproggress();
            }
        });

    }

    @Override
    public void getContext(Context context) {
        this.context=context;
    }
}
