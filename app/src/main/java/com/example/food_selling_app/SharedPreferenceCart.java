package com.example.food_selling_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class SharedPreferenceCart {
    SharedPreferences sharedpreferencesCart;
    public static final String nameCart = "ShopCart";

    public SharedPreferenceCart(Context context){
        sharedpreferencesCart = context.getSharedPreferences(nameCart, Context.MODE_PRIVATE);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedpreferencesCart.getString(key, ""), "‚‗‚")));
    }

    public ArrayList<Food> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Food> playerList =  new ArrayList<Food>();

        for(String jObjString : objStrings){
            Food player  = gson.fromJson(jObjString,  Food.class);
            playerList.add(player);
        }
        return playerList;
    }


    //Đặt ArrayList of String vào SharedPreferences bằng 'key' và lưu
    public void putListString(String key, ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        sharedpreferencesCart.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }


    public void putListObject(String key, ArrayList<Food> playerList){
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Food player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

}