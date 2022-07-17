package com.example.food_selling_app;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int mahoadon;
    private ArrayList<Product> products;
    private String dateBill;
    private String sodienthoai;
    private String diachi;

    public Bill(int mahoadon, String dateBill, String sodienthoai, String diachi) {
        this.mahoadon = mahoadon;
        this.dateBill = dateBill;
        this.sodienthoai = sodienthoai;
        this.diachi = diachi;
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
        if (getProducts() == null) return 0;
        else {
            for (Product p : getProducts())
                tong += p.getTongGia();
            return tong;
        }
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
