package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manoranjan.applecart.Adaptor.Catagorylist2Adaptor;
import com.manoranjan.applecart.Adaptor.CatagorylistAdaptor;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.PresenterImp.HomeprensenterImp;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.CatagoryModel;
import com.manoranjan.applecart.presenter.HomePresenter;
import com.manoranjan.applecart.view.HomeView;

import java.util.List;

import ir.apend.slider.model.Slide;

public class CatagoryActivity extends AppCompatActivity  implements HomeView {
    RecyclerView chapterrecycler;
    HomePresenter homePresenter;

            Catagorylist2Adaptor catagorylistAdaptor;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);
        progressDialog=new ProgressDialog(CatagoryActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title=findViewById(R.id.title);
        title.setText("Category");

        progressDialog.setMessage("Please wait while Loading ...");
        progressDialog.setCancelable(false);
        swipeRefreshLayout=findViewById(R.id.swipe);
        chapterrecycler=findViewById(R.id.recyclerview);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        homePresenter=new HomeprensenterImp(this);
        homePresenter.loadproduct();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePresenter.loadproduct();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               /* Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);*/
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
    }

    @Override
    public void Showprogess() {
        progressDialog.show();
        // Toast.makeText(getContext(),"Hii",Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissproggress() {
        progressDialog.dismiss();
    }


    @Override
    public void getproduct(List<CatagoryModel> catagoryModels) {
        if (catagoryModels.size()>0) {
            StaticData.cat_id = catagoryModels.get(0).getId();
            catagorylistAdaptor = new Catagorylist2Adaptor(getApplicationContext(), catagoryModels);
            chapterrecycler.setAdapter(catagorylistAdaptor);
        }else {
            Toast.makeText(getApplicationContext(), "No Data   ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getSLider(List<Slide> slideList) {

    }

    @Override
    public void getBestoffer(List<Slide> slideList) {

    }

    @Override
    public void getTrendingproduct(List<Slide> slideList) {

    }

    @Override
    public void getbestproduct(List<Slide> slideList) {

    }
}
