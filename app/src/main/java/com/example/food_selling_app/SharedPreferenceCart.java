package com.example.food_selling_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class SharedPreferenceCart {
    private static String sharedName = "shopcart";
    private SharedPreferences preferences;

    public SharedPreferenceCart(Context context){
        sharedName = sharedName + context.getPackageName();
        this.preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public ArrayList<ProductDomain> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<ProductDomain> playerList =  new ArrayList<ProductDomain>();

        for(String jObjString : objStrings){
            ProductDomain player  = gson.fromJson(jObjString,  ProductDomain.class);
            playerList.add(player);
        }
        return playerList;
    }


    //Đặt ArrayList of String vào SharedPreferences bằng 'key' và lưu
    public void putListString(String key, ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }


    public void putListObject(String key, ArrayList<ProductDomain> playerList){
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(ProductDomain player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

}