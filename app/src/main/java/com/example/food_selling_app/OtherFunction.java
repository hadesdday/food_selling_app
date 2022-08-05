package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OtherFunction extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Username = "usernameKey";
    public static String PACKAGE_NAME;
    BottomNavigationView bottomNavigation;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_function);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        initBottomNav();

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
                    Intent intent = new Intent(OtherFunction.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });
        Button btnBackOther = findViewById(R.id.btnBackOther);
        btnBackOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherFunction.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void initBottomNav(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.otherFunction);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(OtherFunction.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.otherFunction:
                    break;
                case R.id.order:
                    intent = new Intent(OtherFunction.this, MainActivity2.class);
                    startActivity(intent);
                    break;
                case R.id.bill:
                    if (sharedpreferences.getString(Username, "") != "") {
                        intent = new Intent(OtherFunction.this, ListBill.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(OtherFunction.this, BillDetails2.class);
                        startActivity(intent);
                    }
                    break;
            }
            return true;
        });
    }
}