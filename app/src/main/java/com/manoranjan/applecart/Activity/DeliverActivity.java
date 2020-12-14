package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.manoranjan.applecart.Adaptor.AddressAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.AddressResponse;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.AddressModel;
import com.manoranjan.applecart.service.CountryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliverActivity extends AppCompatActivity {
    BottomSheetDialog dialog;
    String tokencode;
    EditText fname,lname,email,mobileno,address1,address2,town,pincode,comment,state;
    EditText baddress1,baddress2,btown,bpincode;
    Button buy;
    ProgressDialog progressDialog;
    RecyclerView addressrecyclerview;
    AddressAdaptor addressAdaptor;
    LinearLayout billingaddrresslayout;
    DatabaseHelper myDb;
    Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        myDb=new DatabaseHelper(this);
        init_modal_bottomsheet();
        progressDialog=new ProgressDialog(DeliverActivity.this);
        progressDialog.setMessage("Please Wait...");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=3;
                startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        tokencode = sharedPreferences.getString(Configss.tokencode, "default");
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        email=findViewById(R.id.emailid);
        mobileno=findViewById(R.id.mobileno);
        address1=findViewById(R.id.addressline1);
        address2=findViewById(R.id.addressline2);
        town=findViewById(R.id.town);
        pincode=findViewById(R.id.pincode);
        state=findViewById(R.id.state);
        getAddress();



        buy=findViewById(R.id.placeorder);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatefield();
            }
        });


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    billingaddrresslayout.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),"checked",Toast.LENGTH_LONG).show();

                    // The toggle is enabled
                } else {
                    billingaddrresslayout.setVisibility(View.GONE);
                    // The toggle is disabled
                }
            }
        });



        //dialog.show();
    }

    private void validatefield() {
        final String fname1 = fname.getText().toString().trim();
        final String lname1 = lname.getText().toString().trim();
        final  String email1=email.getText().toString().trim();
        final String mob = mobileno.getText().toString().trim();
        final String address11 = address1.getText().toString().trim();
        final String address22 = address2.getText().toString().trim();
        final  String town1=town.getText().toString().trim();
        final String pincode1 = pincode.getText().toString().trim();

        if(TextUtils.isEmpty(fname1)) {
            fname.setError("Please Enter Your Name");
            return ;
        }else if(TextUtils.isEmpty(lname1)) {
            lname.setError("Please Enter Your Last First Name");
            return ;
        } /*else if(TextUtils.isEmpty(email1)) {
            email.setError("Please Enter your Email");
            return ;
        }else if (!TextUtils.isEmpty(email1) && !Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Enter Valid Email");
            return ;
        }*/
        else if(TextUtils.isEmpty(mob)) {
            mobileno.setError("Enter Your Mobile No");
            return  ;
        }
        else if(TextUtils.isEmpty(town1)) {
            town.setError("Enter Your Town");
            return  ;
        }
        else if(TextUtils.isEmpty(pincode1)) {
            pincode.setError("Enter Your 6 digit pincode");
            return  ;
        }
        else if(mob.length()>10) {
            mobileno.setError("Enter Your Mobile No");
            return ;
        }

        else if(TextUtils.isEmpty(address11)) {
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            address1.setError("Enter Your Address ");
            return ;
        }
        else if(TextUtils.isEmpty(address22)) {
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            address2.setError("Enter Your Address ");
            return ;
        }
        else if(sw.isChecked() && baddress1.getText().toString().isEmpty()){
            baddress1.setError("Enter Billing Address line 1");
            return;
        }
        else if(sw.isChecked() && baddress2.getText().toString().isEmpty()){
            baddress2.setError("Enter Billing Address line 2");
            return;
        }
        else if(sw.isChecked() && btown.getText().toString().isEmpty()){
            btown.setError("Enter Town");
            return;
        }
        else if(sw.isChecked() && bpincode.getText().toString().isEmpty()){
            bpincode.setError("Enter Pincode");
            return;
        }
        else{
           // placeorder();
        }
    }

    /*private void placeorder() {
        progressDialog.show();
        final String fname1 = fname.getText().toString().trim();
        final String lname1 = lname.getText().toString().trim();
        final  String email1=email.getText().toString().trim();
        final String mob = mobileno.getText().toString().trim();
        final String address11 = address1.getText().toString().trim();
        final String address22 = address2.getText().toString().trim();
        final  String town1=town.getText().toString().trim();
        final String pincode1 = pincode.getText().toString().trim();
        // @Field("access_token")String access_token,
        //            @Field("firstname") String firstname,
        //            @Field("lastname") String lastname,
        //            @Field("mobile") String mobile,
        //            @Field("email") String email,
        //            @Field("pincode") String pincode,
        //            @Field("town_city") String town_city,
        //            @Field("address") String address,
        //            //@Field("gst") String gst,
        //           // @Field("login_type") String login_type,
        //           // @Field("payment_status") String payment_status,
        //            @Field("total_amount") String total_amount);
        new CountryService().getAPI().placeorder(ApiLinks.place_order,tokencode,fname1,lname1,mob,email1,
                pincode1,town1,address11+" "+address22, StaticData.totalammount,StaticData.arraycart,
                state.getText().toString()," ",
                "","","")
                .enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        progressDialog.dismiss();
                        if (response.body().getStatus().equals("true")) {
                            boolean check=myDb.deleteAll();
                            if (check) {
                                Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                StaticData.postion=0;
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(), "Order Placed Sucessfully cart not cleared", Toast.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });

    }*/

    private void getAddress() {
        progressDialog.show();
        CountryService countryService=new CountryService();
        countryService.getAPI().getAddress(ApiLinks.view_address,tokencode).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("true")){
                    final List<AddressModel> addressModels=response.body().getData();


                    addressAdaptor=new AddressAdaptor(getApplicationContext(),addressModels);
                    addressrecyclerview.setAdapter(addressAdaptor);
                    addressAdaptor.setonItemClickListner(new AddressAdaptor.OnitemClickListner() {
                        @Override
                        public void OnItemCLiCK(int position) {
                            dialog.cancel();
                            AddressModel addressModel=addressModels.get(position);
                            fname.setText(addressModel.getFirstname());
                            lname.setText(addressModel.getLastname());
                            mobileno.setText(addressModel.getMobile());
                            email.setText(addressModel.getEmail());
                            address1.setText(addressModel.getAddress_1());
                            address2.setText(addressModel.getAddress_2());
                            town.setText(addressModel.getTown());
                            pincode.setText(addressModel.getPincode());

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

    public void init_modal_bottomsheet() {

        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        addressrecyclerview=modalbottomsheet.findViewById(R.id.recyclerview);
        ImageView cancel=modalbottomsheet.findViewById(R.id.cancel);
        LinearLayout addaddress=modalbottomsheet.findViewById(R.id.addaddress);
        addressrecyclerview.setHasFixedSize(true);
        addressrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticData.addresstype="add";
                StaticData.address_id="0";
                startActivity(new Intent(getApplicationContext(),AddAddressActivity.class));
            }
        });

       /* btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btngallery = (Button) modalbottomsheet.findViewById(R.id.btn_gallery);
        btncamera = (Button) modalbottomsheet.findViewById(R.id.btn_camera);
        btn_cancel.setOnClickListener(this);
        btncamera.setOnClickListener(this);
        btngallery.setOnClickListener(this);*/
    }
}
