package com.example.food_selling_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<Bill> {
    Activity context = null;
    ArrayList<Bill> itemList = null;
    int layoutID;

    public BillAdapter(@NonNull Activity context, int layoutID, @NonNull ArrayList<Bill> itemList) {
        super(context, layoutID, itemList);
        this.layoutID = layoutID;
        this.context = context;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =context.getLayoutInflater();
        convertView=inflater.inflate(layoutID,null);

        if (itemList.size()>0&&position>=0){
            final TextView txtmahd = (TextView)convertView.findViewById(R.id.textMaHD);
            final TextView txtngaydathang = (TextView)convertView.findViewById(R.id.textngayhd);
            final TextView txtgiahd = (TextView)convertView.findViewById(R.id.texttonghd);
            final Bill b= itemList.get(position);
            txtmahd.setText(b.getMahoadon()+"");
            txtngaydathang.setText(b.getDateBill()+"");
            txtgiahd.setText(b.getBillPrice()+"");
        }
        return convertView;

    }
}
