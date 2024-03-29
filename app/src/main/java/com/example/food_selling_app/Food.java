package com.example.food_selling_app;

import java.io.Serializable;

public class Food implements Serializable {
    int FoodId;
    String FoodName;
    String FoodImage;
    String FoodDescription;
    int FoodPrice;
    int FoodTypeId;
    private int number;

    public Food(int foodId, String foodName, String foodImage, String foodDescription, int foodPrice, int foodTypeId) {
        FoodId = foodId;
        FoodName = foodName;
        FoodImage = foodImage;
        FoodDescription = foodDescription;
        FoodPrice = foodPrice;
        FoodTypeId = foodTypeId;
    }

    public Food(int foodId, String foodName, String foodImage, String foodDescription, int foodPrice, int foodTypeId, int number) {
        FoodId = foodId;
        FoodName = foodName;
        FoodImage = foodImage;
        FoodDescription = foodDescription;
        FoodPrice = foodPrice;
        FoodTypeId = foodTypeId;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }

    public String getFoodDescription() {
        return FoodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        FoodDescription = foodDescription;
    }

    public int getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        FoodPrice = foodPrice;
    }

    public int getFoodTypeId() {
        return FoodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        FoodTypeId = foodTypeId;
    }
    public double getTotal(){
        return this.FoodPrice*this.number;
    }
}
