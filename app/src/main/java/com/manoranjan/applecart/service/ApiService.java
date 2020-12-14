package com.manoranjan.applecart.service;


import com.manoranjan.applecart.Response.AddressResponse;
import com.manoranjan.applecart.Response.CatagoryResponse;
import com.manoranjan.applecart.Response.LoginResponse;
import com.manoranjan.applecart.Response.OrderHistoryResponse;
import com.manoranjan.applecart.Response.OrderResponse;
import com.manoranjan.applecart.Response.ProductListResponse;
import com.manoranjan.applecart.Response.SignupResponse;
import com.manoranjan.applecart.Response.SubCatagoryResponse;
import com.manoranjan.applecart.model.Data;
import com.manoranjan.applecart.model.SUbcatagoryModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * This class represents the Countries API, all endpoints can stay here.
 *
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 09/03/18.
 * Jesus loves you.
 */
public interface ApiService {

    @GET("country/get/all")
    Call<Data> getResults();



    @FormUrlEncoded
    @POST
    Call<SignupResponse> createUser(
            @Url String url,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("mobile") String mobile,
            @Field("pincode") String pincode,
            @Field("town_city") String town_city,
            @Field("gst") String gst,
            @Field("login_type") String login_type,
            @Field("password") String password,
            @Field("cpassword") String cpassword,
            @Field("address") String address,
            @Field("email") String email);

    @FormUrlEncoded
    @POST
    Call<LoginResponse> login(
            @Url String url,
            @Field("mobile") String mobile,
            @Field("password") String password);

    @FormUrlEncoded
    @POST
    Call<ProductListResponse> getproductlist (
            @Url String url,
            @Field("sub_cat_id") String sub_cat_id
    );

    @FormUrlEncoded
    @POST
    Call<ProductListResponse> getproductbyid(
            @Url String url,
            @Field("product_id") String product_id
    );
    @FormUrlEncoded
    @POST
    Call<AddressResponse> getAddress(
            @Url String url,
            @Field("access_token") String access_token
    );


    @GET
    Call<CatagoryResponse> getcatagory(
            @Url String url);
    @FormUrlEncoded
    @POST
    Call<SubCatagoryResponse> getsubcat(
            @Url String url,
            @Field("cat_id") String cat_id
    );

    //                      'order_status'=>0,
    //                      'total_amount' => $this->input->post('total_amount'),
    @FormUrlEncoded
    @POST
    Call<OrderResponse> placeorder(
            @Url String url,
            @Field("access_token") String access_token,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address_1") String address_1,
            @Field("address_2") String address_2,
            @Field("town_city") String town_city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("total_amount") String total_amount,
            @Field("json_order") String json_order,
            @Field("payment_type") String payment_type) ;

    @FormUrlEncoded
    @POST
    Call<OrderResponse> addaddress(
            @Url String url,
            @Field("access_token") String access_token,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address_1") String address_1,
            @Field("address_2") String address_2,
            @Field("town_city") String town_city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("status") String status);
    @FormUrlEncoded
    @POST
    Call<OrderResponse> updateAddress(
            @Url String url,
            @Field("access_token") String access_token,
            @Field("address_id") String address_id,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address_1") String address_1,
            @Field("address_2") String address_2,
            @Field("town_city") String town_city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("status") String status);

    //@Field("gst") String gst,
    // @Field("login_type") String login_type,
    // @Field("payment_status") String payment_status,
    @FormUrlEncoded
    @POST
    Call<OrderResponse> updateorder(
            @Url String url,
            @Field("access_token") String access_token,
            @Field("order_id") String order_id,
            @Field("product_id") String product_id,
            @Field("status") String status,
            @Field("reason") String reason);

    @FormUrlEncoded
    @POST
    Call<OrderHistoryResponse> orderhistory(
            @Url String url,
            @Field("access_token") String access_token
    );
}
