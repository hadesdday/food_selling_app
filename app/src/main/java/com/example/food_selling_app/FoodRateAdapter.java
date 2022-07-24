package com.example.food_selling_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodRateAdapter extends RecyclerView.Adapter<FoodRateAdapter.FoodRateHolder> {
    Context context;
    ArrayList<FoodRating> foodRatingArrayList;

    public FoodRateAdapter(Context context, ArrayList<FoodRating> foodRatingArrayList) {
        this.context = context;
        this.foodRatingArrayList = foodRatingArrayList;
    }

    @NonNull
    @Override
    public FoodRateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foodrate, parent, false);
        return new FoodRateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRateHolder holder, int position) {
        FoodRating rating = foodRatingArrayList.get(position);
        holder.foodRate.setText(String.format("%s", rating.getFoodRate()).concat("★"));
        holder.foodComment.setText(rating.getFoodComment());
    }

    @Override
    public int getItemCount() {
        return foodRatingArrayList.size();
    }

    static class FoodRateHolder extends RecyclerView.ViewHolder {
        TextView foodRate, foodComment;

        public FoodRateHolder(@NonNull View itemView) {
            super(itemView);
            foodRate = itemView.findViewById(R.id.rate);
            foodComment = itemView.findViewById(R.id.comment);
        }
    }
}
