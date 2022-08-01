package com.example.food_selling_app;

import java.io.Serializable;

public class ProductDomain implements Serializable {
    private int foodId;
    private String foodName;
    private String foodImage;
    private String foodDescription;
    private int foodPrice;
    private int foodTypeId;
    private int numberInCart;

    public ProductDomain(int foodId, String foodName, String foodImage, String foodDescription, int foodPrice, int foodTypeId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodTypeId = foodTypeId;
    }

    public ProductDomain(String foodName, String foodImage, String foodDescription, int foodPrice) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
    public double getTotal(){
        return this.foodPrice*this.numberInCart;
    }
}

