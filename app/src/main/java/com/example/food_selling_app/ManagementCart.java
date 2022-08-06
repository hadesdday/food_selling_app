package com.example.food_selling_app;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private SharedPreferenceCart mobileDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.mobileDB = new SharedPreferenceCart(context);
    }

    //thêm sản phẩm vào giỏ hàng
    public void insertProduct(Food item) {
        ArrayList<Food> listProduct = getListCart();
        boolean exist = false;
        int n = 0;
        for(int i = 0; i < listProduct.size(); i++) {
            if(listProduct.get(i).getFoodName().equals(item.getFoodName())) {
                exist = true;
                n = i;
                break;
            }
        }

        if(exist) {
            listProduct.get(n).setNumber(item.getNumber());
        }else {
            listProduct.add(item);
        }
        mobileDB.putListObject("CartList", listProduct);
        Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng của bạn", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Food> getListCart() {
        return mobileDB.getListObject("CartList");
    }
    public void clearCart(){
        mobileDB.putListObject("CartList", new ArrayList<Food>());
    }
    // +sp
    public void plusNumberFood(ArrayList<Food> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listFood.get(position).setNumber(listFood.get(position).getNumber() + 1);
        mobileDB.putListObject("CartList", listFood);
        changeNumberItemsListener.changed();
    }

    // -sp
    public void minusNumberFood(ArrayList<Food> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if(listFood.get(position).getNumber() == 1) {
            listFood.remove(position);
        }else {
            listFood.get(position).setNumber(listFood.get(position).getNumber() - 1);
        }
        mobileDB.putListObject("CartList", listFood);
        changeNumberItemsListener.changed();
    }

    //tổng tiền
    public Double getTotalFee() {
        ArrayList<Food> listFood = getListCart();
        double fee = 0;
        for(int i = 0; i < listFood.size(); i++) {
            fee = fee + (listFood.get(i).getFoodPrice() * listFood.get(i).getNumber());
        }
        return fee;
    }
}
