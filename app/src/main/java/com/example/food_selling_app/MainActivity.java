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
        Button billList=(Button) findViewById(R.id.btnBillList);
        billList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListBill();
            }
        });
        Button checkout=(Button) findViewById(R.id.btnCheckoutmain);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCheckout();
            }
        });
        Button btncontact=(Button) findViewById(R.id.contactbtn);
        btncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContact();
            }
        });
    }
    public void openListBill(){
        Intent intent=new Intent(this,ListBill.class);
        startActivity(intent);
    }
    public void openCheckout(){
        Intent intent=new Intent(this,Checkout.class);
        startActivity(intent);
    }
    public void openContact(){
        Intent intent=new Intent(this,Contact.class);
        startActivity(intent);
    }

}