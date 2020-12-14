package com.manoranjan.applecart.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.OrderResponse;
import com.manoranjan.applecart.model.AddressModel;
import com.manoranjan.applecart.model.Common;
import com.manoranjan.applecart.service.CountryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {
    String tokencode;
    EditText fname,lname,email,mobileno,address1,address2,town,state,pincode;
    Button buy;
    ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    String radiostatus="1";
    RadioButton radioButton1,radioButton2,radioButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        progressDialog=new ProgressDialog(AddAddressActivity.this);
        progressDialog.setMessage("Please Wait...");
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1=findViewById(R.id.radioButton1);
        radioButton2=findViewById(R.id.radioButton2);
        radioButton3=findViewById(R.id.radioButton3);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (rb.getText().equals("Home")){
                        radiostatus="1";
                    }else if (rb.getText().equals("Office")){
                        radiostatus="2";
                    }
                    else if (rb.getText().equals("Others")){
                        radiostatus="3";
                    }

                    Toast.makeText(getApplicationContext(), radiostatus, Toast.LENGTH_SHORT).show();

                }

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticData.addresstype.equals("update")) {
                    Intent i = new Intent(getApplicationContext(), DeliveryAddress.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //StaticData.postion=0;
                    startActivity(i);
                }else if (StaticData.addresstype.equals("add")) {
                    Intent i = new Intent(getApplicationContext(), DeliverActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //StaticData.postion=0;
                    startActivity(i);
                }else if (StaticData.addresstype.equals("add1")) {
                    Intent i = new Intent(getApplicationContext(), DeliveryAddress.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //StaticData.postion=0;
                    startActivity(i);
                }
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
        buy=findViewById(R.id.placeorder);
        //getAddress();
        if (StaticData.addresstype.equals("update")){
            TextView title=findViewById(R.id.title);
            title.setText("Update Address");
            buy.setText("Update Address");
            if (Common.addressModel!=null) {
                AddressModel addressModel = Common.addressModel;
                fname.setText(addressModel.getFirstname());
                lname.setText(addressModel.getLastname());
                mobileno.setText(addressModel.getMobile());
                email.setText(addressModel.getEmail());
                address1.setText(addressModel.getAddress_1());
                address2.setText(addressModel.getAddress_2());
                town.setText(addressModel.getTown());
                pincode.setText(addressModel.getPincode());
                state.setText(addressModel.getState());
                if (addressModel.getStatus().equals("1")){
                   radioButton1.setChecked(true);
                }else if (addressModel.getStatus().equals("2")){
                   radioButton2.setChecked(true);
                }else if (addressModel.getStatus().equals("3")){
                    radioButton3.setChecked(true);
                }

            }


        }

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatefield();
            }
        });


    }

    private void validatefield() {
        final String fname1 = fname.getText().toString().trim();
        final String lname1 = lname.getText().toString().trim();
        final  String email1=email.getText().toString().trim();
        final String mob = mobileno.getText().toString().trim();
        final String address11 = address1.getText().toString().trim();
        final String address22 = address2.getText().toString().trim();
        final  String town1=town.getText().toString().trim();
        final  String state1=state.getText().toString().trim();
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
        else if(TextUtils.isEmpty(state1)) {
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            state.setError("Enter Your State ");
            return ;
        }
        else if(TextUtils.isEmpty(address11)) {
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            address1.setError("Enter Your Address ");
            return ;
        }
        else{
            if (StaticData.addresstype.equals("update")) {
                updateaddress();
            }else{
                addorder();
            }
        }
    }

    private void updateaddress() {
        progressDialog.show();
        final String fname1 = fname.getText().toString().trim();
        final String lname1 = lname.getText().toString().trim();
        final  String email1=email.getText().toString().trim();
        final String mob = mobileno.getText().toString().trim();
        final String address11 = address1.getText().toString().trim();
        final String address22 = address2.getText().toString().trim();
        final  String town1=town.getText().toString().trim();
        final  String state1=state.getText().toString().trim();
        final String pincode1 = pincode.getText().toString().trim();

        new CountryService().getAPI().updateAddress(ApiLinks.updateAddress,
                tokencode,StaticData.address_id,fname1,lname1,mob,email1,
                address11,address22,town1,
                state.getText().toString(),pincode1,radiostatus).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.body().getStatus().equals("true")) {
                    Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), DeliveryAddress.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //StaticData.postion=0;
                    startActivity(i);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addorder() {
        progressDialog.show();
        final String fname1 = fname.getText().toString().trim();
        final String lname1 = lname.getText().toString().trim();
        final  String email1=email.getText().toString().trim();
        final String mob = mobileno.getText().toString().trim();
        final String address11 = address1.getText().toString().trim();
        final String address22 = address2.getText().toString().trim();
        final  String town1=town.getText().toString().trim();
        final String pincode1 = pincode.getText().toString().trim();

        new CountryService().getAPI().addaddress(ApiLinks.add_address,
                tokencode,fname1,lname1,mob,email1,
                address11,address22,town1,
                state.getText().toString(),pincode1,radiostatus).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                progressDialog.dismiss();

                if (response.body().getStatus().equals("true")) {
                    Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();

                    if (StaticData.addresstype.equals("add1")) {
                        progressDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), DeliveryAddress.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //StaticData.postion=0;
                        startActivity(i);
                    } else{
                        progressDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), DeliverActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
