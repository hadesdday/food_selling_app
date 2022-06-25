package com.example.food_selling_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context = null;
    ArrayList<Product> itemList = null;
    int layoutID;

    public ProductAdapter(@NonNull Activity context, int layoutID, @NonNull ArrayList<Product> itemList) {
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
            final TextView txttensp = (TextView)convertView.findViewById(R.id.texttensp);
            final TextView txtngaydathang = (TextView)convertView.findViewById(R.id.textngaydathang);
            final TextView txtgiasp = (TextView)convertView.findViewById(R.id.textgiasp);
            final TextView txtsoluong = (TextView)convertView.findViewById(R.id.textsoluongsp);
            final Product p= itemList.get(position);
            txttensp.setText(p.getTensp()+"");
            txtngaydathang.setText(p.getNgaydathang()+"");
            txtgiasp.setText(p.getTongGia()+"");
            txtsoluong.setText(p.getSoluongmua()+"");

        }Button b=(Button) convertView.findViewById(R.id.btnCancel_itemProduct);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int pos = (int)position;
                itemList.remove(pos);
                ProductAdapter.this.notifyDataSetChanged();            }
        });

        return convertView;

    }
}
