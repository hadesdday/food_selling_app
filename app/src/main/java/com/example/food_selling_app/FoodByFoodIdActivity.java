package com.example.food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FoodByFoodIdActivity extends AppCompatActivity {
    TextView foodName, foodPrice, foodDescription;
    ImageView foodImage;
    Button addcart, ratefood;
    Spinner number;
    Food food;
    int foodNumber, price;
    String name, image, description;

    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_food_id);
        doFoodByFoodId();

        managementCart = new ManagementCart(this);
    }

    private void doFoodByFoodId() {
        food = (Food) getIntent().getSerializableExtra("food");
        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDescription = findViewById(R.id.foodDescription);
        addcart = findViewById(R.id.addcart);
        ratefood = findViewById(R.id.ratefood);
        number = findViewById(R.id.number);
        DecimalFormat format = new DecimalFormat("###,###,###");


        image = food.getFoodImage();
        name = food.getFoodName();
        price = food.getFoodPrice();
        description = food.getFoodDescription();

        foodName.setText(name);
        foodDescription.setText(description);
        foodPrice.setText(format.format(food.getFoodPrice()).concat("đ"));
        Picasso.with(getApplicationContext()).load(image)
                .error(R.drawable.ic_baseline_error_24)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(foodImage);

//        int drawableResourceId = this.getResources().getIdentifier(food.getFoodImage(), "drawable", this.getPackageName());
//        Glide.with(this).load(drawableResourceId).into(foodImage);
//
//        foodName.setText(food.getFoodName());
//        foodPrice.setText(food.getFoodPrice() + "đ");
//        foodDescription.setText(food.getFoodDescription());

        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        number.setAdapter(adapter);
        number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodNumber = Integer.parseInt(adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food.setNumber(foodNumber);
                managementCart.insertProduct(food);
            }
        });

        ratefood.setOnClickListener(view -> {
            Intent intent = new Intent(this, FoodRateActivity.class);
            intent.putExtra("foodid", food.getFoodId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}