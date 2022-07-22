package com.example.food_selling_app;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter3 extends ArrayAdapter<Product> {
    Activity context = null;
    ArrayList<Product> itemList = null;
    int layoutID;
    final String URL = "http://192.168.1.8:82/WebService.asmx";
    int currentPos = 0;

    public ProductAdapter3(@NonNull Activity context, int layoutID, @NonNull ArrayList<Product> itemList) {
        super(context, layoutID, itemList);
        this.layoutID = layoutID;
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TextView txtmahd = (TextView) context.findViewById(R.id.textMahd_details);
        LayoutInflater inflater = context.getLayoutInflater();

        convertView = inflater.inflate(layoutID, null);

        if (itemList.size() > 0 && position >= 0) {

            final TextView txttensp = (TextView) convertView.findViewById(R.id.texttensp2);
            final TextView txtngaydathang = (TextView) convertView.findViewById(R.id.textngaydathang2);
            final TextView txtgiasp = (TextView) convertView.findViewById(R.id.textgiasp2);
            final TextView txtsoluong = (TextView) convertView.findViewById(R.id.textsoluongsp2);

            final Product p = itemList.get(position);
            Log.i("TAG", "toStirng: " + p.toString());
            txttensp.setText(p.getFoodname() + "");
            txtngaydathang.setText(p.getDateOrdered() + "");
            txtgiasp.setText(p.getTongGia() + "");
            txtsoluong.setText(p.getAmount() + "");

        }
        return convertView;
    }
}
