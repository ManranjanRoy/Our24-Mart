package com.manoranjan.applecart.Activity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manoranjan.applecart.Adaptor.AddressAdaptor;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.MainActivity;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Response.AddressResponse;
import com.manoranjan.applecart.Response.OrderResponse;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.AddressModel;
import com.manoranjan.applecart.service.CountryService;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Button booknow,applynow;
    TextView addresschange;
    RelativeLayout discountlayout;
    TextView totalammount,finalammount,dname,mobileno,address,txtdeliverycharge,discountprice,codestatus;
    EditText editcode;
    String tokencode;
    String total_ammount="0",cartcount="0";
    RadioButton cashondelivary,razorpay;
    RadioButton radioslot1,radioslot2,radioslot3;
    String radiostatus="cod",radio_slot="1",paymentstatus="0";
    DatabaseHelper myDb;
    String array;
    String couponid="0";
    BottomSheetDialog dialog;
    RecyclerView addressrecyclerview;
    AddressAdaptor addressAdaptor;
    ProgressDialog progressDialog;
    AddressModel addressModelorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        progressDialog=new ProgressDialog(PaymentActivity.this);
        progressDialog.setMessage("Please Wait...");

        //array=getIntent().getStringExtra("array");
        array= StaticData.arraycart;
        //StaticData.cartarray=array;
        myDb = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode,"Not Available");
        ImageView back,cart;
        TextView title;
        TextView cartcount;
        title=findViewById(R.id.title);
        title.setText("Delivery & Time");
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PaymentActivity.this, CartActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        findViewById(R.id.cart).setVisibility(View.GONE);
        totalammount=findViewById(R.id.txttotalammount);
        finalammount=findViewById(R.id.txtfinaltotal);
        dname=findViewById(R.id.txtname);
        mobileno=findViewById(R.id.txtmobileno);
        address=findViewById(R.id.txtaddress);
        addresschange=findViewById(R.id.addresschange);
        booknow=findViewById(R.id.bookorder);
        txtdeliverycharge=findViewById(R.id.Deliverycharge);
        cashondelivary=(RadioButton)findViewById(R.id.radio_pirates);
        razorpay=(RadioButton)findViewById(R.id.radio_pirates1);
        radioslot1=findViewById(R.id.radio_slot1);
        radioslot2=findViewById(R.id.radio_slot2);
        radioslot3=findViewById(R.id.radio_slot3);
        discountlayout=findViewById(R.id.discountlayout);
        discountprice=findViewById(R.id.discountpric);
        applynow=findViewById(R.id.applybutton);
        editcode=findViewById(R.id.editcode);
        codestatus=findViewById(R.id.codestatus);
        getAddress();
        init_modal_bottomsheet();


        // cartcount();
        //carttotalamount();
        addresschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               init_modal_bottomsheet();
            }
        });
        booknow.setText("Place Order");
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ordernow();
                if (array.isEmpty()){
                    Toast.makeText(PaymentActivity.this, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
                }else if (addressModelorder==null){
                    Toast.makeText(PaymentActivity.this, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
                    init_modal_bottomsheet();
                }
                else{
                    if(StaticData.mobileno.equals("")){
                        Toast.makeText(getApplicationContext(), "Complete Your Profile to order", Toast.LENGTH_SHORT).show();
                        return;
                    }else
                    if (radiostatus.equals("cod")) {
                       placeorder("0");
                        //orderdetails();
                    } else if (radiostatus.equals("roz")) {
                        Checkout.preload(getApplicationContext());
                        //Toast.makeText(getApplicationContext(),"abc",Toast.LENGTH_SHORT).show();
                        //orderdetails1();
                        startPayment();

                    }
                }
            }
        });
        findViewById(R.id.viewalloffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(),OfferActivity.class));
            }
        });

        totalammount.setText(StaticData.totalammount);
        Float ammount=Float.parseFloat(StaticData.totalammount);
        if (ammount<799){
            txtdeliverycharge.setText("30.0");
            float f = Float.parseFloat(StaticData.totalammount) + 30;
            finalammount.setText(String.valueOf(f));
        }else if (ammount>799){
            txtdeliverycharge.setText("00.0");
            float f = Float.parseFloat(StaticData.totalammount) + 0;
            finalammount.setText(String.valueOf(f));
        }

        discountlayout.setVisibility(View.GONE);
        float ddiscount = Float.parseFloat("0.0");
        float f = Float.parseFloat(StaticData.totalammount) - ddiscount;
        discountprice.setText(String.valueOf(ddiscount));

    }

     private void startPayment() {
        // payammount=Integer.parseInt(ammount.getText().toString());
        Float payammount=Float.parseFloat(finalammount.getText().toString());
        //Float payammount=payammount1/100;

        //Toast.makeText(getApplicationContext(),"Razorpay...."+payammount,Toast.LENGTH_LONG).show();
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Our24 Mart");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Our24 Mart");

            options.put("currency", "INR");
            options.put("email", StaticData.email);
            options.put("contact",StaticData.mobileno);

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", payammount*100);
            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Sucessfully Paid "+s,Toast.LENGTH_SHORT).show();
        paymentstatus="1";
       placeorder("1");


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Failed Paid"+s,Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which RadioButton was clicked
        switch(view.getId()) {
            case R.id.radio_pirates:
                if(checked)
                    radiostatus="cod";
                booknow.setText("Place Order");
                // Toast.makeText(getApplicationContext(),radiostatus,Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_pirates1:
                if(checked)
                    radiostatus="roz";
                booknow.setText("Proceed to payment");
                // Toast.makeText(getApplicationContext(),radiostatus,Toast.LENGTH_SHORT).show();
                break;
        }

    }
    public void onRadioButtonClicked1(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which RadioButton was clicked
        switch(view.getId()) {
            case R.id.radio_slot1:
                if(checked)
                    radio_slot="1";
                // Toast.makeText(getApplicationContext(),radiostatus,Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_slot2:
                if(checked)
                    radio_slot="2";
                // Toast.makeText(getApplicationContext(),radiostatus,Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_slot3:
                if(checked)
                    radio_slot="3";
                // Toast.makeText(getApplicationContext(),radiostatus,Toast.LENGTH_SHORT).show();
                break;
        }
    }



    private void validatefields(String mobileno, String address) {

        if(TextUtils.isEmpty(mobileno)) {
            Toast.makeText(getApplicationContext(),"Please Enter Your Mobile no",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(mobileno.length() >10) {
            Toast.makeText(getApplicationContext(),"Please Enter valid  Mobile no",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(mobileno.length() <10) {
            Toast.makeText(getApplicationContext(),"Please Enter valid  Mobile no",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(),"Please Enter Your Address",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            setvalue(mobileno,address);
            // ordernow(mobileno,address);
        }
    }

    private void setvalue(String mobileno1, String address1) {
        mobileno.setText(mobileno1);
        address.setText(address1);
    }

    public void init_modal_bottomsheet() {
        getAddress();
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

    }

    private void getAddress() {
        progressDialog.show();
        CountryService countryService=new CountryService();
        countryService.getAPI().getAddress(ApiLinks.view_address,tokencode).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, retrofit2.Response<AddressResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("true")){

                    final List<AddressModel> addressModels=response.body().getData();
                    if (addressModels.size()>0) {
                        AddressModel addressModel = addressModels.get(0);
                        addressModelorder=addressModels.get(0);
                        dname.setText(addressModel.getFirstname()+" "+addressModel.getLastname());
                        mobileno.setText(addressModel.getMobile());
                        address.setText(addressModel.getAddress_1() + " " + addressModel.getAddress_2() + " \n"
                                + addressModel.getTown() + " " + addressModel.getState() + "\n Pin : " + addressModel.getPincode());
                        StaticData.mobileno = addressModel.getMobile();
                        StaticData.daddress = addressModel.getAddress_1() + " " + addressModel.getAddress_2() + " \n"
                                + addressModel.getTown() + " " + addressModel.getState() + " " + addressModel.getPincode();
                        StaticData.email = addressModel.getEmail();
                    }

                    addressAdaptor=new AddressAdaptor(getApplicationContext(),addressModels);
                    addressrecyclerview.setAdapter(addressAdaptor);
                    addressAdaptor.setonItemClickListner(new AddressAdaptor.OnitemClickListner() {
                        @Override
                        public void OnItemCLiCK(int position) {
                            dialog.cancel();
                            AddressModel addressModel=addressModels.get(position);
                            addressModelorder=addressModels.get(position);
                            dname.setText(addressModel.getFirstname()+" "+addressModel.getLastname());
                            mobileno.setText(addressModel.getMobile());
                            address.setText(addressModel.getAddress_1()+" "+ addressModel.getAddress_2()+" \n"
                                    +addressModel.getTown() +" "+addressModel.getState()+"\n Pin : "+addressModel.getPincode());
                            StaticData.mobileno=addressModel.getMobile();
                            StaticData.daddress=addressModel.getAddress_1()+" "+ addressModel.getAddress_2()+" \n"
                                    +addressModel.getTown() +" "+addressModel.getState()+" "+addressModel.getPincode();
                            StaticData.email=addressModel.getEmail();

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                progressDialog.dismiss();
                getAddress();
            }
        });

    }

    private void placeorder(String ordertype) {
        progressDialog.show();
        new CountryService().getAPI().placeorder(ApiLinks.place_order,tokencode,
                addressModelorder.getFirstname(),
                addressModelorder.getLastname(),
                addressModelorder.getMobile(),
                addressModelorder.getEmail(),
                addressModelorder.getAddress_1(),
                addressModelorder.getAddress_2(),
                addressModelorder.getTown(),
                addressModelorder.getState(),
                addressModelorder.getPincode(),
                finalammount.getText().toString(),
                array, ordertype)
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

    }


}
