package com.manoranjan.applecart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MobileverifyActivity extends AppCompatActivity {
  EditText mobileno;
  Button sendopt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileverify);
        mobileno=findViewById(R.id.edittextmobileno);
        sendopt=findViewById(R.id.sendotp);
        sendopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String  number=mobileno.getText().toString();
                if (TextUtils.isEmpty(number)){
                    Toast.makeText(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_LONG).show();
                }else if (number.length() <10){
                    Toast.makeText(getApplicationContext(),"Enter Valid Mobile Number",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Otp Send to this number",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MobileverifyActivity.this, OtpVerifyActivity.class));
                }
            }
        });


    }
}
