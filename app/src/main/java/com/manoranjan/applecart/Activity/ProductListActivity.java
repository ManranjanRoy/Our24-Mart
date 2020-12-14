package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.manoranjan.applecart.Adaptor.ProductBySubCatAdaptor;
import com.manoranjan.applecart.Adaptor.ProductlistAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.PresenterImp.ProductlistPresenterImp;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.SubCatagoryResponse;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.Common;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.model.SUbcatagoryModel;
import com.manoranjan.applecart.presenter.ProductListpresentation;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.ProductListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity implements ProductListView {
    ProductListpresentation productListpresentation;
    RecyclerView chapterrecycler,catageryrecycler;
    ProductlistAdaptor productlistAdaptor;
    ProductBySubCatAdaptor productBySubCatAdaptor;
    ProgressDialog progressDialog;
    DatabaseHelper myDb;
    String tokencode;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        myDb=new DatabaseHelper(this);
       /* findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProductDetailsActivity.class));
            }
        });*/
        TextView title=findViewById(R.id.title);
        title.setText("Product List");
        getcount();

        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode, "Not Available");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });

        productListpresentation=new ProductlistPresenterImp(this);
        progressDialog=new ProgressDialog(ProductListActivity.this);
        progressDialog.setMessage("Please wait while Loading ...");
        progressDialog.setCancelable(false);
        chapterrecycler = findViewById(R.id.recyclerview);
        catageryrecycler = findViewById(R.id.recyclerview1);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        catageryrecycler.setHasFixedSize(true);
        catageryrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcount();
                productListpresentation.getproductlist(StaticData.subcat_id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        loadcat();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getcount();
    }

    private void loadcat() {
        Showprogess();
        CountryService countryService=new CountryService();
        countryService.getAPI().getsubcat(ApiLinks.sub_catagory,StaticData.cat_id).enqueue(new Callback<SubCatagoryResponse>() {
            @Override
            public void onResponse(Call<SubCatagoryResponse> call, Response<SubCatagoryResponse> response) {
                //
                // homeView.dismissproggress();
                //dismissproggress();
                if (response.body().getStatus().equals("true")){
                    final List<SUbcatagoryModel> subcatagoryModels=response.body().getData();
                      StaticData.subcat_id=subcatagoryModels.get(0).getId();
                    Common.currentitem=subcatagoryModels.get(0);
                    Log.d("check","response ");
                    productBySubCatAdaptor =new ProductBySubCatAdaptor(getApplicationContext(),subcatagoryModels);
                    catageryrecycler.setAdapter(productBySubCatAdaptor);
                    productListpresentation.getproductlist(StaticData.subcat_id);
                    productBySubCatAdaptor.setonItemClickListner(new ProductBySubCatAdaptor.OnitemClickListner() {
                        @Override
                        public void OnItemCLiCK(int position) {
                            StaticData.cat_id=subcatagoryModels.get(position).getId();
                            productBySubCatAdaptor.updaterowindex(position);
                            productListpresentation.getproductlist(subcatagoryModels.get(position).getId());
                             //Toast.makeText(getApplicationContext(),"Hii", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void OnCartCLiCK(int position) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<SubCatagoryResponse> call, Throwable t) {
                dismissproggress();

            }
        });
    }

    @Override
    public void Showprogess() {
        progressDialog.show();

    }

    @Override
    public void dismissproggress() {
        progressDialog.dismiss();

    }

    @Override
    public void getproduct(final List<ProductListModel> productModelList) {
        TextView count=findViewById(R.id.cartcount);
        productlistAdaptor=new ProductlistAdaptor(getApplicationContext(),productModelList,count);
        chapterrecycler.setAdapter(productlistAdaptor);
        productlistAdaptor.setonItemClickListner(new ProductlistAdaptor.OnitemClickListner() {
            @Override
            public void onwishlistclick(int position) {
                getcount();
                //addwishlist(productListModels.get(position).getId());
            }

            @Override
            public void addtocartclick(int position) {

            }
        });

    }

    public void getcount() {
        TextView count=findViewById(R.id.cartcount);
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            // show message
            count.setText(String.valueOf(ress.getCount()));
        }else{
            count.setText(String.valueOf(ress.getCount()));
        }

    }




}
