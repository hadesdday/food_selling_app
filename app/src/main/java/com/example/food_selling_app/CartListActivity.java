package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalTxt, emptyTxt;
    private ScrollView scrollView;
    Button checkout;
    public static final String MyPREFERENCES = "CartPrefs";
    public static final String FOODLIST = "foodListKey";
    public static final String TOTALLIST = "totalListKey";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        checkout =(Button) findViewById(R.id.btnCheckoutCart);
        managementCart = new ManagementCart(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        initView();
        initList();
        CalculateCart();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(managementCart.getListCart());
                editor.putString(FOODLIST, json);
                float total = Math.round((managementCart.getTotalFee()) * 100) / 100;
                editor.putFloat(TOTALLIST,total);
                editor.commit();
                Intent intent = new Intent(CartListActivity.this, Checkout.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void CalculateCart() {
        double total = Math.round((managementCart.getTotalFee()) * 100) / 100;

        totalTxt.setText(total + "đ");
    }

    private void initView() {
        recyclerViewList = findViewById(R.id.cartview);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        recyclerViewList = findViewById(R.id.cartview);
    }
}