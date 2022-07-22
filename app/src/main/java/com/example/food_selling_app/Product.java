package com.example.food_selling_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int foodId;
    private String foodname;
    private double price;
    private int amount;
    private String dateOrdered;

    public Product(int foodId, String foodname, double price, int amount) {
        this.foodId = foodId;
        this.foodname = foodname;
        this.price = price;
        this.amount = amount;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodname() {
        return foodname;
    }

    public double getPrice() {
        return price;
    }
    public double getTongGia(){
        return price * amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Product(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {

            return new Product[size];
        }

    };

    public void readFromParcel(Parcel in) {
        foodname = in.readString();
        price = in.readDouble();
        amount = in.readInt();
        dateOrdered =in.readString();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodname);
        dest.writeDouble(price);
        dest.writeInt(amount);
        dest.writeString(dateOrdered);
    }

    @Override
    public String toString() {
        return "Product{" +
                "masp=" + foodId +
                ", tensp='" + foodname + '\'' +
                ", gia=" + price +
                ", soluongmua=" + amount +
                ", ngaydathang='" + dateOrdered + '\'' +
                '}';
    }
}
