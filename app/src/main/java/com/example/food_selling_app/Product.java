package com.example.food_selling_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int masp;
    private String tensp;
    private double gia;
    private int soluongmua;
    private String ngaydathang;

    public Product(int masp,String tensp, double gia, int soluongmua) {
        this.tensp = tensp;
        this.gia = gia;
        this.soluongmua = soluongmua;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public double getGia() {
        return gia;
    }
    public double getTongGia(){
        return gia*soluongmua;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoluongmua() {
        return soluongmua;
    }

    public void setSoluongmua(int soluongmua) {
        this.soluongmua = soluongmua;
    }

    public String getNgaydathang() {
        return ngaydathang;
    }

    public void setNgaydathang(String ngaydathang) {
        this.ngaydathang = ngaydathang;
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
        tensp = in.readString();
        gia = in.readDouble();
        soluongmua = in.readInt();
        ngaydathang=in.readString();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tensp);
        dest.writeDouble(gia);
        dest.writeInt(soluongmua);
        dest.writeString(ngaydathang);
    }
}
