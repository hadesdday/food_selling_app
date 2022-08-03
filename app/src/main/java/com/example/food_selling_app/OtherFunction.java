package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OtherFunction extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_function);
        Button btncontact = (Button) findViewById(R.id.ContactOther);
        btncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherFunction.this, Contact.class);
                startActivity(intent);
            }
        });
        Button btnAccount = (Button) findViewById(R.id.accountOther);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                if (sharedpreferences == null) {
                    Intent intent = new Intent(OtherFunction.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(OtherFunction.this, LogoutActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}