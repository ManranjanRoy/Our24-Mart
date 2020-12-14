package com.manoranjan.applecart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.manoranjan.applecart.Activity.CartActivity;
import com.manoranjan.applecart.Activity.CatagoryActivity;
import com.manoranjan.applecart.Activity.DeliveryAddress;
import com.manoranjan.applecart.Activity.EditProfile;
import com.manoranjan.applecart.Activity.FaqActivity;
import com.manoranjan.applecart.Activity.MyProfile;
import com.manoranjan.applecart.Activity.NotificationActivity;
import com.manoranjan.applecart.Activity.OrderHistoryActivity;
import com.manoranjan.applecart.Activity.PrivacyActivity;
import com.manoranjan.applecart.Activity.SearchActivity;
import com.manoranjan.applecart.Activity.TermsconditionActivity;
import com.manoranjan.applecart.Adaptor.CatagorylistAdaptor;
import com.manoranjan.applecart.Adaptor.offersliderAdaptor;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.PresenterImp.HomeprensenterImp;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.CatagoryModel;
import com.manoranjan.applecart.presenter.HomePresenter;
import com.manoranjan.applecart.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeView {
    HomePresenter homePresenter;
    NavigationView navigationView;
    TextView navname,navemail;
    String tokencode;
    boolean loggedIn;
    GoogleApiClient googleApiClient;
    List<Slide> slideList;
    Slider slider;
    Context thiscontext;
    ProgressDialog progressDialog;
    RecyclerView catagoryrecycler,chapterrecycler2,offerrecycler,offerrecycler1,offerrecycler2;
    CatagorylistAdaptor catagorylistAdaptor,catagorylistAdaptor2;
    offersliderAdaptor offersliderAdaptor;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb=new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("our24mart");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });
        ImageView fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                String contact = "+91 9062149071"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        tokencode = sharedPreferences.getString(Configss.tokencode, "default");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_home);
        View headerView = navigationView.getHeaderView(0);
        navname = (TextView) headerView.findViewById(R.id.navname);
        navemail=(TextView)headerView.findViewById(R.id.navemail);

        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        init();
        getcount();


    }

    private void init() {
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait while Loading ...");
        progressDialog.setCancelable(false);

        slider = findViewById(R.id.slider);
        catagoryrecycler = findViewById(R.id.recyclerview);
        catagoryrecycler.setHasFixedSize(true);
        catagoryrecycler.setLayoutManager(new GridLayoutManager(thiscontext,3));

        chapterrecycler2 = findViewById(R.id.recyclerview2);
        chapterrecycler2.setHasFixedSize(true);
        chapterrecycler2.setLayoutManager(new LinearLayoutManager(thiscontext,LinearLayoutManager.HORIZONTAL,true));
        offerrecycler=findViewById(R.id.recyclerview3);
        offerrecycler.setHasFixedSize(true);
        offerrecycler.setLayoutManager(new LinearLayoutManager(thiscontext,LinearLayoutManager.HORIZONTAL,true));

        offerrecycler1=findViewById(R.id.recyclerview4);
        offerrecycler1.setHasFixedSize(true);
        offerrecycler1.setLayoutManager(new LinearLayoutManager(thiscontext,LinearLayoutManager.HORIZONTAL,true));

        offerrecycler2=findViewById(R.id.recyclerview5);
        offerrecycler2.setHasFixedSize(true);
        offerrecycler2.setLayoutManager(new LinearLayoutManager(thiscontext,LinearLayoutManager.HORIZONTAL,true));

        //create list of slides
        slideList = new ArrayList<>();
        homePresenter=new HomeprensenterImp(this);
        homePresenter.getcontext(getApplicationContext());
        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePresenter.loadslider();
                homePresenter.loadproduct();
                homePresenter.loadbestoffer();
                homePresenter.loadtrendingproducts();
                homePresenter.loadbestproducts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.catseeall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CatagoryActivity.class));
            }
        });
        findViewById(R.id.serachtext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
        homePresenter.loadslider();
        homePresenter.loadproduct();
        homePresenter.loadbestoffer();
        homePresenter.loadtrendingproducts();
        homePresenter.loadbestproducts();


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Configss.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(!loggedIn){
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            //We will start the Profile Activity
            Intent intent = new Intent(this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{

            getcount();
            //loadprofile();
            //requestMultiplePermissions();
        }

    }
    public void getcount() {
        TextView count=findViewById(R.id.cartcount);
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            // show message
            count.setText(String.valueOf(ress.getCount()));
        }else{
            count.setText(String.valueOf(ress.getCount()));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            // Handle the camera action
        }else if (id == R.id.nav_profile) {
            Intent i=new Intent(getApplicationContext(), EditProfile.class);
            startActivity(i);
        }else if (id == R.id.nav_cart) {
            Intent i=new Intent(getApplicationContext(), CartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else if (id == R.id.nav_address) {
            Intent i=new Intent(getApplicationContext(), DeliveryAddress.class);
            startActivity(i);
        }else if (id == R.id.nav_orders) {
            Intent i = new Intent(getApplicationContext(), OrderHistoryActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_catagory) {
        Intent i = new Intent(getApplicationContext(), CatagoryActivity.class);
        startActivity(i);
    }
        else if (id == R.id.nav_aboutus) {
            Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_privacy) {
            Intent i = new Intent(getApplicationContext(), PrivacyActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_terms) {
            Intent i = new Intent(getApplicationContext(), TermsconditionActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_faqs) {
            Intent i = new Intent(getApplicationContext(), FaqActivity.class);
            startActivity(i);
        }
        else  if (id==R.id.nav_share){
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Our24 Mart");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout(){

        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String roleid = sharedPreferences.getString(Configss.login_role,"Not Available");

        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        if(roleid.equals("0")) {
                            SharedPreferences preferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Configss.EMAIL_SHARED_PREF, "");
                            editor.putString(Configss.tokencode, "");

                            //Saving the sharedpreferences
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else if(roleid.equals("1")) {
                            SharedPreferences preferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Configss.EMAIL_SHARED_PREF, "");
                            editor.putString(Configss.tokencode, "");

                            //Saving the sharedpreferences
                            editor.commit();
                            LoginManager.getInstance().logOut();
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if(roleid.equals("2")) {
                            SharedPreferences preferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();
                            //Puting the value false for loggedin
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);
                            //Putting blank value to email
                            editor.putString(Configss.EMAIL_SHARED_PREF, "");
                            editor.putString(Configss.tokencode, "");

                            //Saving the sharedpreferences
                            editor.commit();
                            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            if (status.isSuccess()){
                                                Intent intent = new Intent(MainActivity.this, Login.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                                return;
                                                //gotoMainActivity();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Session not close", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        //Starting login activity

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),"For Logout Click on Yes",Toast.LENGTH_SHORT).show();

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void Showprogess() {
        progressDialog.show();
        // Toast.makeText(getContext(),"Hii",Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissproggress() {
        progressDialog.dismiss();
    }

//loading catagory
    @Override
    public void getproduct(List<CatagoryModel> catagoryModels) {
        if (catagoryModels.size()>0) {
            StaticData.cat_id = catagoryModels.get(0).getId();
            catagorylistAdaptor = new CatagorylistAdaptor(getApplicationContext(), catagoryModels);
            catagoryrecycler.setAdapter(catagorylistAdaptor);
            catagorylistAdaptor2 = new CatagorylistAdaptor(getApplicationContext(), catagoryModels);
            chapterrecycler2.setAdapter(catagorylistAdaptor2);
        }else {
            Toast.makeText(getApplicationContext(), "No Data   ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getSLider(List<Slide> slideList) {
        slider.addSlides(slideList);

    }

    @Override
    public void getBestoffer(List<Slide> slideList) {
        offersliderAdaptor offersliderAdaptor=new offersliderAdaptor(getApplicationContext(),slideList);
        offerrecycler2.setAdapter(offersliderAdaptor);
    }

    @Override
    public void getTrendingproduct(List<Slide> slideList) {
        offersliderAdaptor offersliderAdaptor=new offersliderAdaptor(getApplicationContext(),slideList);
        offerrecycler1.setAdapter(offersliderAdaptor);
    }

    @Override
    public void getbestproduct(List<Slide> slideList) {
        offersliderAdaptor offersliderAdaptor=new offersliderAdaptor(getApplicationContext(),slideList);
        offerrecycler.setAdapter(offersliderAdaptor);
    }
}
