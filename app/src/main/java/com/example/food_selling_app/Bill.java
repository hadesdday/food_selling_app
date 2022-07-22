package com.example.food_selling_app;

import java.util.ArrayList;

public class Bill {
    private int billId;
    private ArrayList<Product> products;
    private String dateBill;
    private String phoneNumber;
    private String address;

    public Bill(int billId, String dateBill, String phoneNumber, String address) {
        this.billId = billId;
        this.dateBill = dateBill;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
