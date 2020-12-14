package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manoranjan.applecart.Adaptor.CartlistAdaptor;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.CartMedicineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    DatabaseHelper myDb;
    RecyclerView chapterrecycler;
    CartlistAdaptor medicinelistAdaptor;
    String tokencode;
    double quantityvalue=1;
    double addval=1;
    double finalquantity;
    double startprice;
    double totalprice;
    float totalammounts;
    TextView totalammount,deliverycharge,subtotalammount;
    LinearLayout emptycartview;
    RelativeLayout cartview;
    Button continueshopping;
    Context thiscontext;
    List<CartMedicineModel> cartMedicineModels, updatedatalist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        myDb = new DatabaseHelper(getApplicationContext());
        totalammount=findViewById(R.id.totalammount);
        deliverycharge=findViewById(R.id.deliverycharge);
        subtotalammount=findViewById(R.id.subtotalammount);
        gettotalammount();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);

            }
        });
        progressDialog=new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Please wait while Loading ...");
        progressDialog.setCancelable(false);
        chapterrecycler = findViewById(R.id.recyclerview);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new LinearLayoutManager(thiscontext));

        continueshopping=findViewById(R.id.continueshopping);
        emptycartview=findViewById(R.id.emptyview);
        cartview=findViewById(R.id.cartview);

        continueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StaticData.postion=0;
                startActivity(i);
            }
        });
        loadcart();

        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartMedicineModels.size() != 0) {
                    JSONArray array = new JSONArray();

                    for (int i = 0; i < cartMedicineModels.size(); i++) {
                        JSONObject obj = new JSONObject();
                        //product_id
                        //amount
                        //quantities
                        //access_token
                        try {
                            obj.put("product_id", cartMedicineModels.get(i).getId());
                            obj.put("amount", cartMedicineModels.get(i).getTotalprice());
                            obj.put("quantities", cartMedicineModels.get(i).getQuantity());
                           // obj.put("color",cartMedicineModels.get(i).getColor());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(obj);


                    }
                    Log.d("arraycart",array.toString());
                    StaticData.arraycart=array.toString();
                    StaticData.totalammount=totalammount.getText().toString();

                    Intent i=new Intent(getApplicationContext(), PaymentActivity.class);
                    i.putExtra("array",array.toString());
                    Log.d("arraycart",array.toString());
                    i.putExtra("totalammount",totalammount.getText().toString());
                    startActivity(i);
                    //senddata(array);
                }
                //Intent i=new Intent(CartActivity.this, PaymentPageActivity.class);
                // startActivity(i);
            }
            // startActivity(new Intent(thiscontext,DeliverActivity.class));
        });

        cartMedicineModels = new ArrayList<>();
        updatedatalist = new ArrayList<>();
        chapterrecycler = findViewById(R.id.recyclerview);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new LinearLayoutManager(thiscontext));
        //loadsubject();
        addproducts();
        gettotalammount();
    }
    private void addproducts() {
        cartMedicineModels.clear();
        cartMedicineModels.clear();
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            emptycartview.setVisibility(View.VISIBLE);
            cartview.setVisibility(View.GONE);
            // show message
            //Toast.makeText(thiscontext, "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {
            emptycartview.setVisibility(View.GONE);
            cartview.setVisibility(View.VISIBLE);
            Cursor res = myDb.getAllData();
            // //String id,title,details,code,price,category_name,store_name,quantity,totalprice;
            StringBuffer buffer = new StringBuffer();
            cartMedicineModels.clear();
            while (res.moveToNext()) {
                cartMedicineModels.add(new CartMedicineModel(
                        res.getString(0), res.getString(1),
                        res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5),
                        res.getString(6), res.getString(7),
                        res.getString(8)));
                buffer.append("id :" + res.getString(0));
                buffer.append("title :" + res.getString(1));
                buffer.append("details :" + res.getString(2));
                buffer.append("code :" + res.getString(3));
                buffer.append("price :" + res.getString(4));
                buffer.append("category_name :" + res.getString(5));
                buffer.append("store_name :" + res.getString(6));
                buffer.append("quantity :" + res.getString(7));
                buffer.append("totalprice :" + res.getString(8) + "\n\n");
                Log.d("cartdata", buffer.toString());


            }
            medicinelistAdaptor = new CartlistAdaptor(getApplicationContext(), cartMedicineModels);
            chapterrecycler.setAdapter(medicinelistAdaptor);
            updatedatalist=cartMedicineModels;
            medicinelistAdaptor.setonItemClickListner(new CartlistAdaptor.OnitemClickListner() {
                @Override
                public void OnItemCLiCK(int position) {

                }

                @Override
                public void OnREmovecart(int position) {

                    cartremovedialog(position);

                }

                @Override
                public void Onplus(int position) {
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    quantityvalue= Double.parseDouble(cartMedicineModels.get(position).getQuantity());
                    startprice= Double.parseDouble(cartMedicineModels.get(position).getPrice());
                    if (quantityvalue >= 0.050)
                        finalquantity=quantityvalue + addval;
                    //quantity.setText(String.valueOf(finalquantity));
                    double fprice=startprice;
                    double total=fprice*finalquantity;
                    totalprice=total;

                    boolean isUpdate = myDb.updateData(cartMedicineModels.get(position).getId(),
                            String.valueOf(finalquantity), String.valueOf(total));
                    if(isUpdate == true) {
                        addproducts1();
                        //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addproducts1();
                        //Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
                    }

                    //price.setText(String.valueOf(totalprice));
                    quantityvalue=finalquantity;
                }

                @Override
                public void Onminus(int position) {
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    quantityvalue= Double.parseDouble(cartMedicineModels.get(position).getQuantity());
                    startprice= Double.parseDouble(cartMedicineModels.get(position).getPrice());
                    if (quantityvalue == 1){
                        double fprice=startprice;
                        double total=fprice*1;
                        // price.setText(String.valueOf(startprice));
                        Toast.makeText(getApplicationContext(),"minimum quantity",Toast.LENGTH_SHORT).show();
                        boolean isUpdate = myDb.updateData(cartMedicineModels.get(position).getId(),
                                "1", String.valueOf(total));
                        if(isUpdate == true) {
                            addproducts1();
                            Toast.makeText(getApplicationContext(), "Minimun Quantity", Toast.LENGTH_LONG).show();
                        }
                        else {
                            addproducts1();
                            Toast.makeText(getApplicationContext(), "Minimun Quantity", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        finalquantity=quantityvalue - addval;
                        double total=startprice*finalquantity;
                        totalprice=total;
                        boolean isUpdate = myDb.updateData(cartMedicineModels.get(position).getId(),
                                String.valueOf(finalquantity), String.valueOf(total));
                        if(isUpdate == true) {
                            addproducts1();
                            //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
                        }
                        else {
                            addproducts1();
                            //Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
                        }
                        // price.setText(String.valueOf(totalprice));
                        //quantity.setText(String.valueOf(finalquantity));
                        quantityvalue=finalquantity;
                    }
                }
            });
        }
    }

    void gettotalammount(){
        Cursor res = myDb.getAllData();
        float total=0f;
        float delivercharge=40f;
        while (res.moveToNext()) {
            total=total+Float.parseFloat(res.getString(8));
        }
        totalammounts=total;
        totalammount.setText(String.valueOf(total));
        if (total<799){
            deliverycharge.setText("30.0");
            Float a=Float.parseFloat(String.valueOf(total))+30;
            subtotalammount.setText(String.valueOf(a));
        }else if (total>799){
            deliverycharge.setText("00.0");
            Float a=Float.parseFloat(String.valueOf(total))+0;
            subtotalammount.setText(String.valueOf(a));
        }
        // txtdeliverycharge.setText("Free");
        //finalammount.setText(String.valueOf(total));
        //txtdeliverycharge.setText(String.valueOf(delivercharge));
        //finalammount.setText(String.valueOf(total+delivercharge));
        //mobileno.setText(StaticData.mobileno);
        //address.setText(StaticData.address);

    }

    private void cartremovedialog(final int position) {

        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
        alertDialogBuilder.setMessage("Are you sure you want to Remove?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount()<=0) {
                            chapterrecycler.setVisibility(View.GONE);
                            medicinelistAdaptor.filterdata(updatedatalist);
                        } else {
                            Integer deletedRows = myDb.deleteData(updatedatalist.get(position).getId());
                            if (deletedRows > 0) {
                                addproducts1();
                                Toast.makeText(getApplicationContext(), "Data Deleted", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Data not Deleted", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),"For Remove Click on Yes",Toast.LENGTH_SHORT).show();

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void addproducts1() {
        updatedatalist.clear();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            emptycartview.setVisibility(View.VISIBLE);
            cartview.setVisibility(View.GONE);
            // show message
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {

            emptycartview.setVisibility(View.GONE);
            cartview.setVisibility(View.VISIBLE);
            chapterrecycler.setVisibility(View.VISIBLE);

            while (res.moveToNext()) {
                updatedatalist.add(new CartMedicineModel(
                        res.getString(0), res.getString(1),
                        res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5),
                        res.getString(6), res.getString(7),
                        res.getString(8)));
            }
            medicinelistAdaptor.filterdata(updatedatalist);
            gettotalammount();
        }
    }



    private void loadcart() {

    }

}
