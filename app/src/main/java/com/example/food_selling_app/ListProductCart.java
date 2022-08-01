package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;


public class ListProductCart extends AppCompatActivity {
    private RecyclerView.Adapter adapter2;
    private RecyclerView recyclerViewProductList;
    TextView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_cart);

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ListProductCart.this, CartListActivity.class);
                startActivity(in);
            }
        });

        recyclerViewProduct();

    }


    private void recyclerViewProduct() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProductList = findViewById(R.id.recyclerView2);
        recyclerViewProductList.setLayoutManager(linearLayoutManager);

        ArrayList<ProductDomain> productList = new ArrayList<>();
        productList.add(new ProductDomain(1,"pizza", "pizza1", "Một trong những điều giúp Tyler1 trở nên nổi tiếng đó là nhờ vào tính cách thẳng thắn", 10000,1));
        productList.add(new ProductDomain(2,"Burger", "burger", "Và trong buổi lên sóng gần đây, Tyler1 đã bày tỏ sự thất vọng vô cùng lớn đối với Riot khi họ tung ra phiên bản 12.14", 12000,1));
        productList.add(new ProductDomain(3,"pizza2", "pizza", "Tyler1 thậm chí còn khẳng định rằng mình sẽ bay tới tận trụ sở của Riot để mở tiệc ăn mừng vào cái ngày đội cân bằng LMHT bị sa thải.", 11000,1));

        adapter2 = new ProductAdapterCart(productList);
        recyclerViewProductList.setAdapter(adapter2);
    }
}