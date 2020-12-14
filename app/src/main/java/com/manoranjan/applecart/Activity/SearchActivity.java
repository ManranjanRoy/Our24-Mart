package com.manoranjan.applecart.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.manoranjan.applecart.Adaptor.AllProductlistAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.ProductListResponse;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.service.CountryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    List<ProductListModel> productListModels;
    RecyclerView chapterrecycler;
    AllProductlistAdaptor productlistAdaptor;
    EditText searchbar;
    ProgressBar progressBar;
    RelativeLayout rrnodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        productListModels=new ArrayList<>();
        progressBar = findViewById(R.id.progressBar_cyclic);
        chapterrecycler = findViewById(R.id.recyclerview);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                finish();
            }
        });

        searchbar=findViewById(R.id.editserahbar);
        rrnodata=findViewById(R.id.rrnodata);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        if (searchbar.getText().toString().isEmpty()) {
            rrnodata.setVisibility(View.VISIBLE);
            List<ProductListModel> hospitalListModels = new ArrayList<>();
            productlistAdaptor = new AllProductlistAdaptor(getApplicationContext(), hospitalListModels);
            chapterrecycler.setAdapter(productlistAdaptor);
        }else{
            if (productListModels.size() == 0) {
                rrnodata.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                productlistAdaptor = new AllProductlistAdaptor(getApplicationContext(), productListModels);
                chapterrecycler.setAdapter(productlistAdaptor);
            } else {
                rrnodata.setVisibility(View.GONE);
                productlistAdaptor = new AllProductlistAdaptor(getApplicationContext(), productListModels);
                chapterrecycler.setAdapter(productlistAdaptor);
            }
        }

        addproduct();
    }
    private void filter(String text) {
        progressBar.setVisibility(View.VISIBLE);
        if (text.isEmpty() || text.equals("")){
            rrnodata.setVisibility(View.VISIBLE);
            Log.d("cheksearch","empty");
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
            //addproducts();
            List<ProductListModel> hospitalListModels = new ArrayList<>();
            productlistAdaptor = new AllProductlistAdaptor(getApplicationContext(), hospitalListModels);
            chapterrecycler.setAdapter(productlistAdaptor);
            progressBar.setVisibility(View.GONE);
            productlistAdaptor.filterdlist(hospitalListModels);
        } else {
            rrnodata.setVisibility(View.GONE);
            List<ProductListModel> hospitalListModels = new ArrayList<>();
            for (ProductListModel item : productListModels) {
                if (item.getName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
                        ||item.getCatName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
                        ||item.getSubCatName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
                ) {
                    hospitalListModels.add(item);
                }
            }
            progressBar.setVisibility(View.GONE);
            productlistAdaptor.filterdlist(hospitalListModels);
        }

    }

    private void addproduct() {
        CountryService countryService=new CountryService();
        progressBar.setVisibility(View.VISIBLE);
        countryService.getAPI().getproductlist(ApiLinks.productlist, "5").enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus().equals("true")){
                    List<ProductListModel> productListModels1=response.body().getData();
                    productListModels=productListModels1;
                }
                else{
                    List<ProductListModel> productListModels1=new ArrayList<>();
                    productListModels=productListModels1;
                }

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
