package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manoranjan.applecart.Adaptor.OrderHistorylistAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.OrderHistoryResponse;
import com.manoranjan.applecart.Response.OrderResponse;
import com.manoranjan.applecart.model.OrderHistoryModel;
import com.manoranjan.applecart.service.CountryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<OrderHistoryModel> foodmodels;
    OrderHistorylistAdaptor foodlistAdaptor;
    String tokencode;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        progressDialog=new ProgressDialog(OrderHistoryActivity.this);
        progressDialog.setMessage("Please Wait");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);
            }
        });
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode, "Not Available");
        init();
    }
    private void init() {

        recyclerView=findViewById(R.id.recyclerview1);
        foodmodels=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        hotlerlist();

        //loadcatagory();
    }
    public  void hotlerlist(){
        final ProgressDialog progressDialog=new ProgressDialog(OrderHistoryActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        new CountryService().getAPI().orderhistory(ApiLinks.order_history,tokencode)
                .enqueue(new Callback<OrderHistoryResponse>() {
                             @Override
                             public void onResponse(Call<OrderHistoryResponse> call, retrofit2.Response<OrderHistoryResponse> response) {
                                progressDialog.dismiss();
                                 if (response.body().getStatus().equals("true")) {
                                     final List<OrderHistoryModel> orderHistoryModels = response.body().getData();
                                     foodlistAdaptor = new OrderHistorylistAdaptor(getApplicationContext(), orderHistoryModels);
                                     recyclerView.setAdapter(foodlistAdaptor);
                                     foodlistAdaptor.setonItemClickListner(new OrderHistorylistAdaptor.OnitemClickListner() {
                                         @Override
                                         public void OnItemCLiCK(int position) {

                                         }

                                         @Override
                                         public void onCancel(int position) {
                                             canceloredr1(orderHistoryModels.get(position).getOrderId(),orderHistoryModels.get(position).getProduct_id());

                                         }

                                         @Override
                                         public void onReturn(int position) {
                                             returnorder(orderHistoryModels.get(position).getOrderId(),orderHistoryModels.get(position).getProduct_id());
                                         }
                                     });

                                     //productBySubCatAdaptor.row_index=0;
                                 }
                             }

                             @Override
                             public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {

                             }
                         });
    }



   /* private void canceloredr(final String order_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Reason For Cancel");

        final EditText input = new EditText(OrderHistoryActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Reason", Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog.show();
                    new CountryService().getAPI().updateorder(ApiLinks.order_update, tokencode, order_id, "1", input.getText().toString()).enqueue(
                            new Callback<OrderResponse>() {
                                @Override
                                public void onResponse(Call<OrderResponse> call, retrofit2.Response<OrderResponse> response) {
                                    progressDialog.dismiss();
                                    if (response.body().getStatus().equals("true")) {

                                        Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<OrderResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                }
                            }
                    );
                }

                //Toast.makeText(getApplicationContext(), "Text entered is " + input.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "To Cancel Enter Reason", Toast.LENGTH_SHORT).show();

            }
        });
        builder.show();
    }
*/
    private void canceloredr1(final String order_id, final String product_id) {
        //final EditText input = new EditText(OrderHistoryActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialoglayoutreason)
                .setTitle("Enter Your Reason")
                .setPositiveButton("Submit", null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        EditText input=dialog.findViewById(R.id.input);
                        //input.setHint("Enter your Reason");
                        if (input.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Reason", Toast.LENGTH_LONG).show();
                        }
                        else {
                            progressDialog.show();
                            new CountryService().getAPI().updateorder(ApiLinks.order_update, tokencode, order_id, product_id,"4", input.getText().toString()).enqueue(
                                    new Callback<OrderResponse>() {
                                        @Override
                                        public void onResponse(Call<OrderResponse> call, retrofit2.Response<OrderResponse> response) {
                                            progressDialog.dismiss();
                                            if (response.body().getStatus().equals("true")) {

                                                Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                dialog.dismiss();

                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                        }
                                    }
                            );
                        }

                        //Dismiss once everything is OK.

                    }
                });

                Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void returnorder(final String order_id, final String product_id) {
        // final EditText input = new EditText(OrderHistoryActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialoglayoutreason)
                .setTitle("Enter Your Reason")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        EditText input=dialog.findViewById(R.id.input);
                        input.setHint("Enter your Reason");
                        if (input.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Reason", Toast.LENGTH_LONG).show();
                        }
                        else {
                            progressDialog.show();
                            new CountryService().getAPI().updateorder(ApiLinks.order_update, tokencode, order_id, product_id,"3",
                                    input.getText().toString()).enqueue(
                                    new Callback<OrderResponse>() {
                                        @Override
                                        public void onResponse(Call<OrderResponse> call, retrofit2.Response<OrderResponse> response) {
                                            progressDialog.dismiss();
                                            if (response.body().getStatus().equals("true")) {

                                                Toast.makeText(getApplicationContext(), response.body().getMessages(), Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                dialog.dismiss();

                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                        }
                                    }
                            );
                        }

                        //Dismiss once everything is OK.

                    }
                });

                Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }


}
