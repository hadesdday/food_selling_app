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

    //thêm sản phẩm vào giỏ
    public void insertProduct(ProductDomain item) {
        ArrayList<ProductDomain> listProduct = getListCart();
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
            listProduct.get(n).setNumberInCart(item.getNumberInCart());
        }else {
            listProduct.add(item);
        }
        mobileDB.putListObject("CartList", listProduct);
        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ProductDomain> getListCart() {
        return mobileDB.getListObject("CartList");
    }

    // +sp
    public void plusNumberFood(ArrayList<ProductDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() + 1);
        mobileDB.putListObject("CartList", listFood);
        changeNumberItemsListener.changed();
    }

    // -sp
    public void minusNumberFood(ArrayList<ProductDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if(listFood.get(position).getNumberInCart() == 1) {
            listFood.remove(position);
        }else {
            listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);
        }
        mobileDB.putListObject("CartList", listFood);
        changeNumberItemsListener.changed();
    }

    //tổng tiền
    public Double getTotalFee() {
        ArrayList<ProductDomain> listFood = getListCart();
        double fee = 0;
        for(int i = 0; i < listFood.size(); i++) {
            fee = fee + (listFood.get(i).getFoodPrice() * listFood.get(i).getNumberInCart());
        }
        return fee;
    }
}
