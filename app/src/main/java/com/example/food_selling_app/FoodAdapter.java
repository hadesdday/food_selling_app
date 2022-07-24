package com.example.food_selling_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {
    Context context;
    ArrayList<Food> foodArrayList;

    public FoodAdapter(Context context, ArrayList<Food> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_food, parent, false);
        return new FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        Food food = foodArrayList.get(position);
        holder.foodName.setText(food.getFoodName());
        holder.foodPrice.setText(format.format(food.getFoodPrice()).concat("đ"));
        Picasso.with(context).load(food.getFoodImage())
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    static class FoodHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }
}
