package com.example.food_selling_app;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int mahoadon;
    private ArrayList<Product> products;
    private String dateBill;

    public Bill(int mahoadon, String dateBill) {
        this.mahoadon = mahoadon;
        this.dateBill = dateBill;
    }

    public int getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(int mahoadon) {
        this.mahoadon = mahoadon;
    }

    public String getDateBill() {
        return dateBill;
    }

    public void setDateBill(String dateBill) {
        this.dateBill = dateBill;
    }

    public double getBillPrice() {
        double tong = 0;
        if (getProducts()==null) return 0;
        else {
            for (Product p : getProducts())
                tong += p.getTongGia();
            return tong;
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
