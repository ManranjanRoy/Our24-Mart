package com.manoranjan.applecart.PresenterImp;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Response.SignupResponse;
import com.manoranjan.applecart.model.Data;
import com.manoranjan.applecart.presenter.SignupPresenter;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.SignupView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupprenterImp  implements SignupPresenter {
    SignupView signupView;


    public SignupprenterImp(SignupView signupView) {
        this.signupView = signupView;
    }

    @Override
    public void signup(String fname, String lname ,String email, String mobileno, String address1, String address2,
                       String town, String pincode, String password, String cpassword) {
           if (signupView.validatefiled()){
               signupView.Showprogess();
               CountryService countryService=new CountryService();

               countryService.getAPI().createUser(ApiLinks.register,fname,lname,mobileno,pincode,town,"","1",password,cpassword,address1+address2,email)
                       .enqueue(new Callback<SignupResponse>() {
                           @Override
                           public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                              SignupResponse data=response.body();
                              signupView.dismissproggress();

                               if (data != null && data.getStatus() != null) {
                                   if (data.getStatus().equals("true")) {

                                       signupView.onSucess();
                                   }
                                   else{
                                       signupView.onError(data.getMessages());
                                   }
                                   //List<Country> result = data.getRestResponse().getResult();
                                   // countryView.countriesReady(result);

                               }
                           }

                           @Override
                           public void onFailure(Call<SignupResponse> call, Throwable t) {
                                signupView.dismissproggress();
                           }
                       });
               // @Url String url,
               //            @Field("firstname") String firstname,
               //            @Field("lastname") String lastname,
               //            @Field("mobile") String mobile,
               //            @Field("pincode") String pincode,
               //            @Field("town_city") String town_city,
               //            @Field("gst") String gst,
               //            @Field("login_type") String login_type,
               //            @Field("password") String password,
               //            @Field("cpassword") String cpassword,
               //            @Field("address") String address,
               //            @Field("email") String email);
               countryService
                       .getAPI()
                       .getResults()
                       .enqueue(new Callback<Data>() {
                           @Override
                           public void onResponse(Call<Data> call, Response<Data> response) {
                               Data data = response.body();

                               if (data != null && data.getRestResponse() != null) {
                                   signupView.onSucess();
                                   //List<Country> result = data.getRestResponse().getResult();
                                  // countryView.countriesReady(result);

                               }
                           }

                           @Override
                           public void onFailure(Call<Data> call, Throwable t) {
                               try {
                                   throw new InterruptedException("Something went wrong!");
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
           }else{
               signupView.onError("error");
           }


    }
}
