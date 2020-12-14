package com.manoranjan.applecart.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.PresenterImp.ProductDetailsImp;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.CartMedicineModel;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.presenter.ProductDetailsPresenter;
import com.manoranjan.applecart.view.ProductDetailsView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ProductDetailsActivity extends AppCompatActivity  implements ProductDetailsView  {
    TextView pname,pdesc,pprice,pprice2,pcat,pqty,cpincodesucess,cpincodeerror;
    Button addtocart;
    ProductDetailsPresenter productDetailsPresenter;
    ProgressDialog progressDialog;
    DatabaseHelper myDb;
    List<ProductListModel> productListModels;
    LinearLayout checkpin;
    String tokencode;
    List<Slide> slideList;
    Slider slider;
    LinearLayout addbuttonlayout,quantitylayout;
    TextView plus,minus,pquantity,remove;
    List<CartMedicineModel> cartMedicineModels, updatedatalist;
    double quantityvalue=1;
    double addval=1;
    double finalquantity;
    double startprice;
    double totalprice;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();

    List<String> array_imgs=new ArrayList<>();
    ExpandableTextView expTv1,expTv2,expTv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        myDb = new DatabaseHelper(this);
        TextView title=findViewById(R.id.title);
        title.setText("Product Details");
        initComponent();
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode, "default");

         expTv1 = (ExpandableTextView) findViewById(R.id.sample1)
                .findViewById(R.id.expand_text_view);
        TextView t=expTv1.findViewById(R.id.title);
        t.setText("Description ");
        expTv1.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                //  Toast.makeText(getApplicationContext(), isExpanded ? "Expanded" : "Collapsed", Toast.LENGTH_SHORT).show();
            }
        });



         expTv2 = (ExpandableTextView) findViewById(R.id.sample2)
                .findViewById(R.id.expand_text_view);
        TextView t1=expTv2.findViewById(R.id.title);
        t1.setText("Key Features");
        expTv2.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                //  Toast.makeText(getApplicationContext(), isExpanded ? "Expanded" : "Collapsed", Toast.LENGTH_SHORT).show();
            }
        });


         expTv3 = (ExpandableTextView) findViewById(R.id.sample3)
                .findViewById(R.id.expand_text_view);
        TextView t2=expTv3.findViewById(R.id.title);
        t2.setText("Disclaimer");
        expTv3.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                //  Toast.makeText(getApplicationContext(), isExpanded ? "Expanded" : "Collapsed", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticData.detailstype.equals("wishlist")){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    StaticData.postion = 2;
                    startActivity(i);
                }else if (StaticData.detailstype.equals("Bestseller")){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    StaticData.postion = 0;
                    startActivity(i);//Bestseller
                }else {
                    Intent i = new Intent(getApplicationContext(), ProductListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    StaticData.postion = 0;
                    startActivity(i);
                }
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
        slider = findViewById(R.id.slider);
        //create list of slides
        slideList = new ArrayList<>();
        checkpin=findViewById(R.id.checkpin);
        progressDialog=new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("Please wait while loading...");
        progressDialog.setCancelable(false);
        productListModels=new ArrayList<>();
        pname=findViewById(R.id.pname);
        pprice=findViewById(R.id.pprice);
        pprice2=findViewById(R.id.pprice2);
        pqty=findViewById(R.id.txtqty);
        //pdesc=findViewById(R.id.pdesc);
        pcat=findViewById(R.id.pcat);
        cpincodeerror=findViewById(R.id.cdpincodeerror);
        cpincodesucess=findViewById(R.id.cdpincodesucess);
        addtocart=findViewById(R.id.addtocart);
        addbuttonlayout=findViewById(R.id.addbuttonlayout);
        quantitylayout=findViewById(R.id.quantitylayout);
        pquantity=findViewById(R.id.pquantity);
        plus=findViewById(R.id.minus);
        minus=findViewById(R.id.plus);
        cartMedicineModels = new ArrayList<>();
        updatedatalist = new ArrayList<>();
        addbuttonlayout.setVisibility(View.VISIBLE);
        quantitylayout.setVisibility(View.GONE);
         productDetailsPresenter=new ProductDetailsImp(this);
         getalldata();
        productDetailsPresenter.getproductdetails();

        verifycode(StaticData.pincode);


        checkpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showalert();
            }
        });

      /*  //addtocart
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.currentitem ==null){
                    Toast.makeText(getApplicationContext(),"Select Color",Toast.LENGTH_LONG).show();
                }
                else {
                  //  String id1 = productListModels.get(0).getId();
                    String id1 = productListModels.get(0).getCatId();//id product
                    String title1 = productListModels.get(0).getName();
                    String details1 = productListModels.get(0).getDescription();
                    String image1 = productListModels.get(0).getImage();
                    String price1 = productListModels.get(0).getPrice();
                    String category_name1 = productListModels.get(0).getCatName();
                    String color = Common.currentitem.getId();
                    String quantity1 = "1";
                    //String totalprice1=price1;
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    // Double p=Double.parseDouble(quantity ) * Double.parseDouble(productModelList.get(position).getPrice());
                    String totalprice1 =
                            String.valueOf(df2.format(Double.parseDouble(quantity1) * Double.parseDouble(price1)));

                    // Toast.makeText(getApplicationContext(), "mannu", Toast.LENGTH_LONG).show();
                    boolean isInserted = myDb.insertData(
                            id1, title1, details1, image1, price1, category_name1, color, quantity1, totalprice1);
                    if (isInserted == true) {
                        getcount();
                        Toast.makeText(getApplicationContext(), "Added To Cart", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already Added", Toast.LENGTH_LONG).show();
                    }
                    //addtocartfun(position);

                    Cursor res = myDb.getAllData();
                    if (res.getCount() == 0) {
                        // show message
                        Toast.makeText(getApplicationContext(), "No Data Inserted", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //count.setText(res.getCount());
                    // //String id,title,details,image,price,category_name,color,quantity,totalprice;
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("id :" + res.getString(0));
                        buffer.append("title :" + res.getString(1));
                        buffer.append("details :" + res.getString(2));
                        buffer.append("image :" + res.getString(3));
                        buffer.append("price :" + res.getString(4));
                        buffer.append("category_name :" + res.getString(5));
                        buffer.append("store_name :" + res.getString(6));
                        buffer.append("color :" + res.getString(7));
                        buffer.append("totalprice :" + res.getString(8) + "\n\n");
                    }
                    // Show all data
                    Log.d("alldata", buffer.toString());
                    //Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();

                    StaticData.postion = 3;
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });*/
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocartfun(0);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incementfun(0);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    decerementfun(0);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        addproducts1();
        productDetailsPresenter.getproductdetails();

    }


    @Override
    public void onSucess(String msg) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void showProgress() {
        progressDialog.show();

    }

    @Override
    public void dismissprogress() {
        progressDialog.dismiss();

    }

    @Override
    public void getproductdetails(List<ProductListModel> data) {
        productListModels=data;
        getcount();
        final ProductListModel uploadCurrent = data.get(0);

         array_imgs.clear();
        String img1=uploadCurrent.getImage();
        String img2=uploadCurrent.getImage1();
        String img3=uploadCurrent.getImage2();
        String img4=uploadCurrent.getImage3();
        List<String> imgs=new ArrayList<>();
        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);
        imgs.add(img4);
        slideList.add(new Slide(0,
                ApiLinks.baseimgurl + img1, 0));
        for (int i=0;i<imgs.size(); i++) {
            if (!imgs.get(i).equals("") || !imgs.get(i).isEmpty()) {
                Log.d("imgcount", String.valueOf(i));
                array_imgs.add(ApiLinks.baseimgurl + imgs.get(i));
                slideList.add(new Slide(i + 1,
                        ApiLinks.baseimgurl + imgs.get(i), 0));
            }
        }

        slider.addSlides(slideList);
        initComponent();
        for (int i=0;i<cartMedicineModels.size();i++) {
            if (productListModels.get(0).getProduct_id().equals(cartMedicineModels.get(i).getId())) {
                addbuttonlayout.setVisibility(View.GONE);
                quantitylayout.setVisibility(View.VISIBLE);
                int quantityvalue = (int) Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                int a = quantityvalue;
                pquantity.setText(String.valueOf(quantityvalue));
            } else {
                addbuttonlayout.setVisibility(View.VISIBLE);
                quantitylayout.setVisibility(View.GONE);
                // Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }

        pname.setText(uploadCurrent.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            expTv1.setText(Html.fromHtml(uploadCurrent.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            expTv1.setText(Html.fromHtml(uploadCurrent.getDescription()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            expTv2.setText(Html.fromHtml(uploadCurrent.getKeyfeature()+"." , Html.FROM_HTML_MODE_COMPACT));
        } else {
            expTv2.setText(Html.fromHtml(uploadCurrent.getKeyfeature()+"."));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            expTv3.setText(Html.fromHtml(uploadCurrent.getDisclaimer()+".", Html.FROM_HTML_MODE_COMPACT));
        } else {
            expTv3.setText(Html.fromHtml(uploadCurrent.getDisclaimer()+"."));
        }
        //pdesc.setText(uploadCurrent.getDescription());
        //expTv1.setText(uploadCurrent.getDescription());

        pcat.setText(uploadCurrent.getCatName() +" / "+uploadCurrent.getSubCatName());
        pprice.setText(uploadCurrent.getOfferPrice());
        pprice2.setText("\u20B9"+Float.parseFloat(uploadCurrent.getPrice()));
        pprice2.setPaintFlags(pprice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pqty.setText(uploadCurrent.getQty() +" "+ uploadCurrent.getMeasurement());
    }

    private void getalldata() {
        cartMedicineModels.clear();
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            cartMedicineModels.clear();
            // show message
            Log.d("cartdata","No data");
            //Toast.makeText(mContext, "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {
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
        }
    }

    private void incementfun(int position) {
        String cartid="0";
        DecimalFormat df2 = new DecimalFormat("#.##");
        for (int i=0;i<cartMedicineModels.size();i++){
            if (productListModels.get(position).getProduct_id().equals(cartMedicineModels.get(i).getId())){
                cartid=cartMedicineModels.get(i).getId();
                quantityvalue= Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                startprice= Double.parseDouble(cartMedicineModels.get(i).getPrice());
            }
            else{
                cartid="0";
                Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
        if (quantityvalue >= 1)
            finalquantity=quantityvalue + addval;
        //quantity.setText(String.valueOf(finalquantity));
        double fprice=startprice;
        double total=fprice*finalquantity;
        totalprice=total;

        boolean isUpdate = myDb.updateData(cartid,
                String.valueOf(finalquantity), String.valueOf(total));
        if(isUpdate == true) {
            addproducts1();
            getcount();
            //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
        }
        else {
            addproducts1();
            getcount();
            Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
        }

        //price.setText(String.valueOf(totalprice));
        quantityvalue=finalquantity;
    }
    private void decerementfun(int position) {
        String cartid="0";
        DecimalFormat df2 = new DecimalFormat("#.##");
        for (int i=0;i<cartMedicineModels.size();i++){
            if (productListModels.get(position).getProduct_id().equals(cartMedicineModels.get(i).getId())){
                cartid=cartMedicineModels.get(i).getId();
                quantityvalue= Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                startprice= Double.parseDouble(cartMedicineModels.get(i).getPrice());
            }
            else{
                cartid="0";
                Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
        if (quantityvalue > 1) {
            finalquantity = quantityvalue - addval;
            //quantity.setText(String.valueOf(finalquantity));
            double fprice = startprice;
            double total = fprice * finalquantity;
            totalprice = total;

            boolean isUpdate = myDb.updateData(cartid,
                    String.valueOf(finalquantity), String.valueOf(total));
            if (isUpdate == true) {
                addproducts1();
                getcount();
                //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
            } else {
                addproducts1();
                getcount();
                Toast.makeText(getApplicationContext(), "Data not Updated", Toast.LENGTH_LONG).show();
            }
            //price.setText(String.valueOf(totalprice));
            quantityvalue = finalquantity;
        }else if (quantityvalue==1){
            Integer deletedRows = myDb.deleteData(cartid);
            if (deletedRows > 0) {
                addbuttonlayout.setVisibility(View.VISIBLE);
                quantitylayout.setVisibility(View.GONE);
                addproducts1();
               getcount();
                //Toast.makeText(mContext, "Data Deleted", Toast.LENGTH_LONG).show();

            } else {
                addbuttonlayout.setVisibility(View.VISIBLE);
                quantitylayout.setVisibility(View.GONE);
                getcount();
                // Toast.makeText(mContext, "Data not Deleted", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void addtocartfun(int position) {

        //String id,title,details,image,price,0,color,quantity,totalprice;
        String id=productListModels.get(position).getProduct_id();
        String title=productListModels.get(position).getName();
        String details=productListModels.get(position).getDescription();
        String image=productListModels.get(position).getImage();//img
        String price=productListModels.get(position).getPrice();
        String category_name=productListModels.get(position).getCatName();
        String store_name="0";
        String quantity="1.0";//gm
        DecimalFormat df2 = new DecimalFormat("#.##");
        // Double p=Double.parseDouble(quantity ) * Double.parseDouble(productModelList.get(position).getPrice());
        String totalprice=
                String.valueOf(df2.format(Double.parseDouble(quantity ) * Double.parseDouble(productListModels.get(position).getPrice())));

        // Toast.makeText(getApplicationContext(), "mannu", Toast.LENGTH_LONG).show();
        boolean isInserted = myDb.insertData(
                id,title,details,image,price,category_name,store_name,quantity,totalprice);
        if(isInserted == true){
            //notifyDataSetChanged();
            getcount();
            Toast.makeText(getApplicationContext(),"Added To Cart",Toast.LENGTH_LONG).show();}
        else {
            getcount();
            // notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Already Added", Toast.LENGTH_LONG).show();
        }
        //addtocartfun(position);

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // notifyDataSetChanged();
            // show message
            Toast.makeText(getApplicationContext(),"No Data Inserted",Toast.LENGTH_LONG).show();
            return;
        }

        //count.setText(res.getCount());
        // //String id,title,details,image,price,category_name,store_name,quantity,totalprice;
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("id :"+ res.getString(0));
            buffer.append("title :"+ res.getString(1));
            buffer.append("details :"+ res.getString(2));
            buffer.append("image :"+ res.getString(3));
            buffer.append("price :"+ res.getString(4));
            buffer.append("category_name :"+ res.getString(5));
            buffer.append("store_name :"+ res.getString(6));
            buffer.append("quantity :"+ res.getString(7));
            buffer.append("totalprice :"+ res.getString(8)+"\n\n");
        }
        // Show all data
        Log.d("alldata",buffer.toString());
        //Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();

    }
    private void addproducts1() {
        updatedatalist.clear();
        cartMedicineModels.clear();
        Cursor res = myDb.getAllData();
       /* if (res.getCount() == 0) {
            Toast.makeText(mContext, "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {*/
        while (res.moveToNext()) {
            updatedatalist.add(new CartMedicineModel(
                    res.getString(0), res.getString(1),
                    res.getString(2), res.getString(3),
                    res.getString(4), res.getString(5),
                    res.getString(6), res.getString(7),
                    res.getString(8)));
        }
        cartMedicineModels=updatedatalist;
    }

    private void getcount() {
         getalldata();
        TextView count=findViewById(R.id.cartcount);
        Cursor ress = myDb.getAllData();

        for (int i=0;i<cartMedicineModels.size();i++) {
            if (productListModels.get(0).getProduct_id().equals(cartMedicineModels.get(i).getId())) {
                Log.d("checkstatus","yes");
                addbuttonlayout.setVisibility(View.GONE);
                quantitylayout.setVisibility(View.VISIBLE);
                int quantityvalue = (int) Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                int a = quantityvalue;
                pquantity.setText(String.valueOf(quantityvalue));
            } else {
                Log.d("checkstatus","no");
                addbuttonlayout.setVisibility(View.VISIBLE);
                quantitylayout.setVisibility(View.GONE);
                // Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
        if (ress.getCount() == 0) {
            // show message
            addbuttonlayout.setVisibility(View.VISIBLE);
            quantitylayout.setVisibility(View.GONE);
            count.setText(String.valueOf(ress.getCount()));
        }else{
            count.setText(String.valueOf(ress.getCount()));
        }
    }
    private void showalert() {

        //  final EditText input = new EditText(ProductDetailsActivity.this);
        //   input.setHint("    Enter Pincode");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialoglayout)
                .setTitle("Enter Your Pincode")
                .setPositiveButton("Check", null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText input=dialog.findViewById(R.id.input);

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (input.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Reason", Toast.LENGTH_LONG).show();

                        }
                        else {

                            if (verifycode(input.getText().toString())){
                                dialog.dismiss();
                            }else{dialog.dismiss();}
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

    private boolean verifycode(final String pincode){
        //Getting values from edit texts
        final ProgressDialog progressDialog=new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("Please Wait ....");
        progressDialog.show();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.check_pincode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("pincheck",response.toString());
                        try {
                            cpincodeerror.setVisibility(View.GONE);
                            cpincodesucess.setVisibility(View.VISIBLE);
                            JSONObject object1=new JSONObject(response);
                            if(object1.getString("status").equals("true")){

                                JSONArray array=object1.getJSONArray("data");
                                JSONObject obj=array.getJSONObject(0);
                                //id,pincode,created_date,updated_date;

                                String id=obj.getString("id");
                                progressDialog.dismiss();
                                cpincodesucess.setText("Diliver To Pincode - " +pincode);

                            }else{
                                cpincodeerror.setText("Not Deliver to this pincode - "+pincode);
                                cpincodeerror.setVisibility(View.VISIBLE);
                                cpincodesucess.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                //Toast.makeText(getApplicationContext(),object1.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("access_token",tokencode);
                params.put("pincode",pincode);
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return true;
    }










//slider

    private void initComponent() {
        layout_dots = findViewById(R.id.layout_dots);
        viewPager = findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, array_imgs);

        adapterImageSlider.setItems(array_imgs);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        /**
         * auto slider
         */
        startAutoSlider(adapterImageSlider.getCount());
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 25;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.redcircel);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<String> items;

        // constructor
        private AdapterImageSlider(Activity activity, List<String> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void setItems(List<String> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView imageView = v.findViewById(R.id.image);
            displayImageOriginal(act, imageView, items, position);

            (container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((RelativeLayout) object);
        }

    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 8000);
            }
        };
        handler.postDelayed(runnable, 8000);
    }

    /**
     * Can be a method form Utils to use many times on project
     * @param context
     * @param img
     * @param url
     * @param position
     */
    private static void displayImageOriginal(Context context, ImageView img, List<String> url, int position) {
        try {
            Glide.with(context).load(url.get(position))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }



}
