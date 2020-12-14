package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    TextView tname,tmobno,temail,taddress,tinteest,tpassword;
    LinearLayout showhide;
    ImageView profileimg,editprofile;
    String tokencode;
    String  click="0";
    String startpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        tname=findViewById(R.id.username);
        tmobno=findViewById(R.id.mobno);
        temail=findViewById(R.id.email);
        taddress=findViewById(R.id.address);
        tpassword=findViewById(R.id.password);
        showhide=findViewById(R.id.Showhide1);

        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode,"Not Available");
        loadprofile();

        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click.equals("0")){
                    tpassword.setText(StaticData.password);
                    click="1";
                }else if (click.equals("1")){
                    tpassword.setText(startpass.substring(0,startpass.length()-1));
                    click="0";
                }

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        loadprofile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editprofile) {
            Intent i=new Intent(getApplicationContext(), EditProfile.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadprofile(){
        //Getting values from edit texts
        final ProgressDialog progressDialog=new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Please Wait ....");
        progressDialog.show();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.view_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("loginresponse",response.toString());
                        try {

                            JSONObject object1=new JSONObject(response);
                            if(object1.getString("status").equals("true")){
                                JSONArray array=object1.getJSONArray("data");
                                JSONObject obj=array.getJSONObject(0);
                                String id=obj.getString("id");
                                String Accesstoken=obj.getString("access_token");
                                String name=obj.getString("name");
                                String email=obj.getString("email");
                                String mobileno=obj.getString("mobile");
                                String address=obj.getString("address");
                                String password=obj.getString("password");

                                tname.setText(name);
                                tmobno.setText(mobileno);
                                temail.setText(email);
                                taddress.setText(address);
                                tpassword.setText(password);

                                StaticData.uid=id;
                                StaticData.name=name;
                                StaticData.email=email;
                                StaticData.mobileno=mobileno;
                                StaticData.address=address;
                                StaticData.password=password;

                                int length=StaticData.password.length();
                                String t="*";
                                for (int i=0;i<length;i++)
                                {
                                    t=t+"*";
                                }

                                startpass=t;
                                tpassword.setText(startpass.substring(0,startpass.length()-1));
                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),object1.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("access_token",tokencode);
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
