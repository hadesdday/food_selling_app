package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    Intent intent;
    public static final String MyPREFERENCES = "MyPrefs";
    public static String PACKAGE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        initBottomNav();



        Button btnBillDetail = (Button) findViewById(R.id.btnBillDetail);
        btnBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBillDetail();
            }
        });


        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });
        Button cart = (Button) findViewById(R.id.cartbtn);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });

        Button listProductCart = (Button) findViewById(R.id.productcart);
        listProductCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListProductCart.class);
                startActivity(intent);
            }
        });
    }



    public void openBillDetail() {
        Intent intent = new Intent(this, BillDetails2.class);
        startActivity(intent);
    }
    public void initBottomNav(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    break;
                case R.id.otherFunction:
                    intent = new Intent(MainActivity.this, OtherFunction.class);
                    startActivity(intent);
                    break;
                case R.id.order:
                    intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    break;
                case R.id.bill:
                    if (sharedpreferences != null) {
                        intent = new Intent(MainActivity.this, ListBill.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.this, BillDetails2.class);
                        startActivity(intent);
                    }
                    break;
            }
            return true;
        });
    }
}