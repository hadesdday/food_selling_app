package com.example.food_selling_app;

import java.io.Serializable;

public class FoodRating implements Serializable {
    int RateId;
    int FoodId;
    float FoodRate;
    String FoodComment;

    public FoodRating(int rateId, int foodId, float foodRate, String foodComment) {
        RateId = rateId;
        FoodId = foodId;
        FoodRate = foodRate;
        FoodComment = foodComment;
    }

    public int getRateId() {
        return RateId;
    }

    public void setRateId(int rateId) {
        RateId = rateId;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public double getFoodRate() {
        return FoodRate;
    }

    public void setFoodRate(float foodRate) {
        FoodRate = foodRate;
    }

    public String getFoodComment() {
        return FoodComment;
    }

    public void setFoodComment(String foodComment) {
        FoodComment = foodComment;
    }
}
