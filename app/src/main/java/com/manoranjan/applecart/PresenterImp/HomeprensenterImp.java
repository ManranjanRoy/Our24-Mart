package com.manoranjan.applecart.PresenterImp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Response.CatagoryResponse;
import com.manoranjan.applecart.model.CatagoryModel;
import com.manoranjan.applecart.presenter.HomePresenter;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.HomeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.apend.slider.model.Slide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeprensenterImp implements HomePresenter {

    HomeView homeView;
    Context context;

    public HomeprensenterImp(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void loadproduct() {
        homeView.Showprogess();
        CountryService countryService=new CountryService();
        countryService.getAPI().getcatagory(ApiLinks.catagory).enqueue(new Callback<CatagoryResponse>() {
            @Override
            public void onResponse(Call<CatagoryResponse> call, Response<CatagoryResponse> response) {
                homeView.dismissproggress();
                if (response.body().getStatus().equals("true")){
                    List<CatagoryModel> catagoryModel=response.body().getData();
                    homeView.getproduct(catagoryModel);
                }
            }

            @Override
            public void onFailure(Call<CatagoryResponse> call, Throwable t) {
                homeView.dismissproggress();

            }
        });
        //homeView.Showprogess();

    }

    @Override
    public void loadslider() {
       final List<Slide> slideList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiLinks.slider,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("banner",response.toString());
                        try {
                            JSONObject outer=new JSONObject(response);
                            JSONArray jsonArray=outer.getJSONArray("data");
                            if (outer.getString("status").equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject inner = jsonArray.getJSONObject(i);
                                    slideList.add(new Slide(i+1,
                                            ApiLinks.baseimgurl+inner.getString("image"),0));
                                }
                                homeView.getSLider(slideList);
                            }
                            else{
                                Toast.makeText(context,outer.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, e.toString()+"exception   ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                //params.put("sub_category_id",subid);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void loadbestoffer() {
        final List<Slide> slideList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiLinks.bestoffer_slider,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("banner",response.toString());
                        try {
                            JSONObject outer=new JSONObject(response);
                            JSONArray jsonArray=outer.getJSONArray("data");
                            if (outer.getString("status").equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject inner = jsonArray.getJSONObject(i);
                                    slideList.add(new Slide(i+1,
                                            ApiLinks.baseimgurl+inner.getString("image"),0));
                                }
                                homeView.getBestoffer(slideList);
                            }
                            else{
                                Toast.makeText(context,outer.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, e.toString()+"exception   ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                //params.put("sub_category_id",subid);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void loadtrendingproducts() {
        final List<Slide> slideList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiLinks.trending_slider,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("banner",response.toString());
                        try {
                            JSONObject outer=new JSONObject(response);
                            JSONArray jsonArray=outer.getJSONArray("data");
                            if (outer.getString("status").equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject inner = jsonArray.getJSONObject(i);
                                    slideList.add(new Slide(i+1,
                                            ApiLinks.baseimgurl+inner.getString("image"),0));
                                }
                                homeView.getTrendingproduct(slideList);
                            }
                            else{
                                Toast.makeText(context,outer.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, e.toString()+"exception   ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                //params.put("sub_category_id",subid);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void loadbestproducts() {
        final List<Slide> slideList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiLinks.best_product,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("banner",response.toString());
                        try {
                            JSONObject outer=new JSONObject(response);
                            JSONArray jsonArray=outer.getJSONArray("data");
                            if (outer.getString("status").equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject inner = jsonArray.getJSONObject(i);
                                    slideList.add(new Slide(i+1,
                                            ApiLinks.baseimgurl+inner.getString("image"),0));
                                }
                                homeView.getbestproduct(slideList);
                            }
                            else{
                                Toast.makeText(context,outer.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, e.toString()+"exception   ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                //params.put("sub_category_id",subid);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void getcontext(Context context) {
        this.context=context;
    }
}
