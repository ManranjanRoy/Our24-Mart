package com.manoranjan.applecart.view;

import com.manoranjan.applecart.model.CatagoryModel;

import java.util.List;

import ir.apend.slider.model.Slide;

public interface HomeView {
    
    void Showprogess();
    void dismissproggress();
    void getproduct(List<CatagoryModel> catagoryModels);
    void getSLider(List<Slide> slideList);
    void getBestoffer(List<Slide> slideList);
    void getTrendingproduct(List<Slide> slideList);
    void getbestproduct(List<Slide> slideList);
}
