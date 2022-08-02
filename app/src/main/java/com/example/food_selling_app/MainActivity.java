package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button billList = (Button) findViewById(R.id.btnBillList);
        billList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListBill();
            }
        });
        Button checkout = (Button) findViewById(R.id.btnCheckoutmain);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCheckout();
            }
        });
        Button btncontact = (Button) findViewById(R.id.contactbtn);
        btncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContact();
            }
        });
        Button btnBillDetail = (Button) findViewById(R.id.btnBillDetail);
        btnBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBillDetail();
            }
        });
        Button btnloginMain=(Button) findViewById(R.id.btnLoginMain);
        btnloginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Button main2=(Button) findViewById(R.id.btnMain2);
        main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        Button logout=(Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });
        Button cart=(Button) findViewById(R.id.cartbtn);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });
        Button addcart=(Button) findViewById(R.id.addcartbtn);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodByFoodIdActivity.class);
                startActivity(intent);
            }
        });
        Button foodidbtn=(Button) findViewById(R.id.productcart);
        foodidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListProductCart.class);
                startActivity(intent);
            }
        });
    }

    public void openListBill() {
        Intent intent = new Intent(this, ListBill.class);
        startActivity(intent);
    }

    public void openCheckout() {
        Intent intent = new Intent(this, Checkout.class);
        startActivity(intent);
    }

    public void openContact() {
        Intent intent = new Intent(this, Contact.class);
        startActivity(intent);
    }

    public void openBillDetail() {
        Intent intent = new Intent(this, BillDetails2.class);
        startActivity(intent);
    }
}