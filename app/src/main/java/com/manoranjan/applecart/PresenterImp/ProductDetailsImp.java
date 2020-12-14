package com.manoranjan.applecart.PresenterImp;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.Response.ProductListResponse;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.presenter.ProductDetailsPresenter;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.ProductDetailsView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsImp implements ProductDetailsPresenter{
    ProductDetailsView productDetailsView;

    public ProductDetailsImp(ProductDetailsView productDetailsView) {
        this.productDetailsView = productDetailsView;
    }

    @Override
    public void getproductdetails() {
        productDetailsView.showProgress();
        CountryService countryService=new CountryService();
        countryService.getAPI().getproductbyid(ApiLinks.product_byid, StaticData.p_id).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                productDetailsView.dismissprogress();
                if (response.body().getStatus().equals("true")){
                    List<ProductListModel> productListModels=response.body().getData();
                    productDetailsView.getproductdetails(productListModels);
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                productDetailsView.dismissprogress();
                productDetailsView.onError("Error");
            }
        });

    }
}
