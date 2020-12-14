package com.manoranjan.applecart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.manoranjan.applecart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    String tokencode;
    String name,mobno,email_id,address,password;
    EditText tname,tlname,tmobno,temailid,taddress,tpassword;
    TextView tid;
    ImageView profilepic;
    Button insert;
    private RequestQueue rQueue;
    private int CAMERA = 22,GALLERYDOC = 11;
    String path,path1;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profilepic=findViewById(R.id.profileimg);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choosePhotoFromGallaryDoc();
            }
        });
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.user);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tname=findViewById(R.id.username);
        tlname=findViewById(R.id.lname);
        tmobno=findViewById(R.id.mobno);
        temailid=findViewById(R.id.email);
        taddress=findViewById(R.id.address);
        insert=findViewById(R.id.save);
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tokencode = sharedPreferences.getString(Configss.tokencode,"Not Available");

        loadprofile();
       /* tname.setText(StaticData.name);
        tmobno.setText(StaticData.mobileno);
        temailid.setText(StaticData.email);
        taddress.setText(StaticData.address);*/
        // tpassword.setText(StaticData.password);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=tname.getText().toString();
                mobno=tmobno.getText().toString();
                email_id=temailid.getText().toString();
                address=taddress.getText().toString();
                //password=tpassword.getText().toString();
                validatefields();
               /* if (name.equals("") || mobno.equals("") || address.equals("") || email_id.equals("") ){
                    Toast.makeText(getApplicationContext(),"Please Enter All feilds ",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImage();
                    // imageUpload(filePath);
                }*/
            }
        });
    }

    public void choosePhotoFromGallaryDoc() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERYDOC);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERYDOC) {
            if (data != null) {

                Uri contentURI = data.getData();

                path1 = contentURI.getLastPathSegment();
                File f = new File("" + contentURI);

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    profilepic.setImageBitmap(bitmap);
                    this.bitmap=bitmap;

                    //uploadImage(bitmap);
                    //compressImage(path1);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }



    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void validatefields() {
        name=tname.getText().toString();
        mobno=tmobno.getText().toString();
        email_id=temailid.getText().toString();
        address=taddress.getText().toString();
        //password=tpassword.getText().toString();
        if(TextUtils.isEmpty(name)) {
            tname.setError("Please Enter Your Name");
            return;
        } else if(TextUtils.isEmpty(email_id)) {
            temailid.setError("Please Enter your Email");
            return;
        }else if (!TextUtils.isEmpty(email_id) && !Patterns.EMAIL_ADDRESS.matcher(email_id).matches()){
            temailid.setError("Enter Valid Email");
            return;
        }
        else if(TextUtils.isEmpty(mobno)) {
            tmobno.setError("Enter Your Mobile No");
            return;
        }
        else if(mobno.length()>10) {
            tmobno.setError("Enter Your Mobile No");
            return;
        }
      /*  else if(TextUtils.isEmpty(password)) {
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            tpassword.setError("Enter Your Password ");
            return;
        }*/
       /* else if(TextUtils.isEmpty(address)){
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            taddress.setError("Enter Your Address ");
            return;
        }*/
        else {
            uploadImage();
            //uploadImage(bitmap);

        }
    }


    private void uploadImage(){
        name=tname.getText().toString();
        mobno=tmobno.getText().toString();
        email_id=temailid.getText().toString();
        address=taddress.getText().toString();
        // password=tpassword.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(EditProfile.this,"Updating...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.update_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("updateresponse..",s.toString());
                        try {
                            JSONObject firstObject = new JSONObject(s);
                            String sucess=firstObject.getString("status");
                            Log.d("sucess..",sucess);
                            if(sucess.equals("true")) {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated Sucessfully", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditProfile.this, EditProfile.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(), "Something Went Wrong ", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //loading.dismiss();
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("firstname",name);
                params.put("lastname",tlname.getText().toString());
                params.put("email",email_id);
                params.put("mobile",mobno);
                params.put("address",address);
                // params.put("password",password);
                params.put("access_token",tokencode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadprofile(){
        //Getting values from edit texts
        final ProgressDialog progressDialog=new ProgressDialog(EditProfile.this);
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
                                String fname=obj.getString("firstname");
                                String lname=obj.getString("lastname");
                                String email=obj.getString("email");
                                String mobileno=obj.getString("mobile");
                                // String address=obj.getString("address");
                                String password=obj.getString("password");



                                tname.setText(fname);
                                tlname.setText(lname);
                                tmobno.setText(mobileno);
                                temailid.setText(email);
                                //taddress.setText(address);

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