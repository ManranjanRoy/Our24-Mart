package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manoranjan.applecart.Adaptor.DeliveryAddressAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.AddressResponse;
import com.manoranjan.applecart.model.AddressModel;
import com.manoranjan.applecart.model.Common;
import com.manoranjan.applecart.service.CountryService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddress extends AppCompatActivity {
    RecyclerView addressrecyclerview;
    DeliveryAddressAdaptor addressAdaptor;
    ProgressDialog progressDialog;
    String tokencode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        tokencode = sharedPreferences.getString(Configss.tokencode, "default");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);
            }
        });
        progressDialog=new ProgressDialog(DeliveryAddress.this);
        progressDialog.setMessage("Please Wait...");
        addressrecyclerview=findViewById(R.id.recyclerview);
        LinearLayout addaddress=findViewById(R.id.addaddress);
        addressrecyclerview.setHasFixedSize(true);
        addressrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // dialog.show();
        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticData.addresstype="add1";
                startActivity(new Intent(getApplicationContext(),AddAddressActivity.class));
            }
        });

        getAddress();
    }

    private void getAddress() {
        progressDialog.show();

        CountryService countryService=new CountryService();
        countryService.getAPI().getAddress(ApiLinks.view_address,tokencode).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("true")){
                    final List<AddressModel> addressModels=response.body().getData();


                    addressAdaptor=new DeliveryAddressAdaptor(getApplicationContext(),addressModels);
                    addressrecyclerview.setAdapter(addressAdaptor);
                    addressAdaptor.setonItemClickListner(new DeliveryAddressAdaptor.OnitemClickListner() {
                        @Override
                        public void OnItemCLiCK(int position) {
                            StaticData.address_id=addressModels.get(position).getId();
                            StaticData.addresstype="update";
                            AddressModel addressModel=addressModels.get(position);
                            Common.addressModel=addressModel;
                            startActivity(new Intent(getApplicationContext(),AddAddressActivity.class));
                        }

                        @Override
                        public void Deleteaddress(int position) {
                            deleteaddress(addressModels.get(position).getId());
                        }
                    });





                    //productDetailsView.getproductdetails(productListModels);
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void deleteaddress(final String id) {
        final ProgressDialog progressDialog=new ProgressDialog(DeliveryAddress.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.delete_address,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deleteaddresswishlist",response.toString());
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject outer=new JSONObject(response);

                            if (outer.getString("status").equals("true")) {
                                Toast.makeText(getApplicationContext(), outer.getString("message"), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                getAddress();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), outer.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("access_token",tokencode);
                params.put("address_id", id);
                //returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
