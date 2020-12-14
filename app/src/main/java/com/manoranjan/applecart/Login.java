package com.manoranjan.applecart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.PresenterImp.LoginPresenterImp;
import com.manoranjan.applecart.presenter.LoginPresenter;
import com.manoranjan.applecart.view.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements LoginView, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    TextView signup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    TextView linkforgotpass;
    private AppCompatButton buttonLogin,buttonlogin1;
    LoginPresenter loginPresenter;
    ProgressDialog progressDialog;

    CardView loginButton1;
    LoginButton fblogin;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";


    CardView signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;
    //private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.linkSignupadmin);
        loginPresenter=new LoginPresenterImp(Login.this);
        loginPresenter.getcontext(getApplicationContext());
        progressDialog=new ProgressDialog(Login.this);
        progressDialog.setMessage("Please Wait login");

        editTextEmail = (EditText) findViewById(R.id.edittextmobileno);
        editTextPassword = findViewById(R.id.editTextPassword);
        //linkforgotpass = (TextView) findViewById(R.id.linkforgotpass);
        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        buttonlogin1=findViewById(R.id.buttonLogin1);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Signup.class));
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.loginfunction(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                //startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
        buttonlogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MobileverifyActivity.class));
            }
        });


        fblogin = findViewById(R.id.login_button); //fblogin
        loginButton1=findViewById(R.id.login_button1);
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        fblogin.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin.performClick();
            }
        });

        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("errofblogin","canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("errofblogin",exception.toString());
                // App code
            }
        });

        //google sign in
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,Login.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();

            String thisString=account.getDisplayName();

            String[] parts = thisString.split(" ");
            String first = parts[0];//"hello"
            String second = parts[1];//"World"
            loginbyfborgmail(account.getEmail(),first,second,"2");
            Log.d("glogin",account.getDisplayName()+account.getEmail()+account.getId());

            //userName.setText(account.getDisplayName());
            //userEmail.setText(account.getEmail());
            //userId.setText(account.getId());
            // gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }



    private void gotoProfile(){

        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

        //Intent intent=new Intent(Login.this, MyProfile.class);
        //startActivity(intent);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            Log.d("name",first_name+last_name);
                            Log.d("email",email);
                            loginbyfborgmail(email,first_name,last_name,"1"); // o for normal 1 for fb 2for gmail

                            // txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            // txtEmail.setText(email);
                            //   Picasso.with(Login.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    @Override
    public void onSucess() {
        Toast.makeText(getApplicationContext(),"Sucessfully Login",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError() {
       /* Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/
        //Toast.makeText(getApplicationContext(),"Login faild wrong email password",Toast.LENGTH_LONG).show();
        // startActivity(new Intent(getApplicationContext(),Login.class));
    }

    @Override
    public boolean validatefiled() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        /*Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        // finish();
        if (email.isEmpty() || password.isEmpty() ){
            Toast.makeText(getApplicationContext(),"Enter Correct Email Or Password",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    public void Showprogess() {
        progressDialog.show();
    }

    @Override
    public void dismissproggress() {
        progressDialog.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void loginbyfborgmail(final String email,final String fname,final  String lname,final String role){

        final ProgressDialog progressDialog=new ProgressDialog(Login.this);
        progressDialog.setMessage("Please Wait while Login....");
        progressDialog.show();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.login_by_fb,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("loginresponsefb",response.toString());
                        try {

                            JSONObject object1=new JSONObject(response);
                            if(object1.getString("status").equals("true")){
                                // JSONArray jsonArray = object1.getJSONArray("data");
                                //JSONObject inner = jsonArray.getJSONObject(0);

                                String access_token=object1.getString("access_token");
                                Log.d("accesstoken",object1.getString("access_token"));
                                Toast.makeText(getApplicationContext(),object1.getString("message"),Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = Login.this.getSharedPreferences
                                        (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //Adding values to editor
                                editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(Configss.login_role,role);
                                editor.putString(Configss.tokencode,object1.getString("access_token"));
                                editor.commit();
                                progressDialog.dismiss();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
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
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("firstname", fname);
                params.put("lastname", lname);
                params.put("name", fname+" "+lname);
                params.put("email", email);
                params.put("login_type","2");
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
