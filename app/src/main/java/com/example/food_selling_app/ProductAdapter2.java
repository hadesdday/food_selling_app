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

public class ProductAdapter2 extends ArrayAdapter<Product> {
    Activity context = null;
    ArrayList<Product> itemList = null;
    int layoutID;
    final String URL = "http://192.168.1.8:82/WebService.asmx";
    int currentPos = 0;

    public ProductAdapter2(@NonNull Activity context, int layoutID, @NonNull ArrayList<Product> itemList) {
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

            final TextView txttensp = (TextView) convertView.findViewById(R.id.texttensp);
            final TextView txtngaydathang = (TextView) convertView.findViewById(R.id.textngaydathang);
            final TextView txtgiasp = (TextView) convertView.findViewById(R.id.textgiasp);
            final TextView txtsoluong = (TextView) convertView.findViewById(R.id.textsoluongsp);

            final Product p = itemList.get(position);
            Log.i("TAG", "toStirng: " + p.toString());
            txttensp.setText(p.getTensp() + "");
            txtngaydathang.setText(p.getNgaydathang() + "");
            txtgiasp.setText(p.getTongGia() + "");
            txtsoluong.setText(p.getSoluongmua() + "");

        }
        Button b = (Button) convertView.findViewById(R.id.btnCancel_itemProduct);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int pos = (int) position;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog;
                builder.setTitle("Are you sure delete this item?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("TAG", "mahd: " + txtmahd.getText().toString());
                        Log.i("TAG", "masp: " + itemList.get(pos).getMasp());
                        itemList.remove(pos);
                        ProductAdapter2.this.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("no", null);
                dialog = builder.create();
                dialog.show();

            }
        });
        Button changebtn = (Button) convertView.findViewById(R.id.btnChange);
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) position;
                currentPos = pos;
                onChangeAmountPopup(view);


            }
        });
        return convertView;

    }

    public void onChangeAmountPopup(View view) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_change_quantity, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
        EditText txtChangeQuantity = (EditText) popupView.findViewById(R.id.editQuantityPopup);
        Button btnChangeQuantityPopup = (Button) popupView.findViewById(R.id.btnAccept);
        Button btnCancleChangePopup = (Button) popupView.findViewById(R.id.btnCancelPopup);
        btnCancleChangePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                return;
            }
        });
        btnChangeQuantityPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(txtChangeQuantity.getText().toString());
                Log.i("TAG", "onClick: quantity " + quantity);

                Product p = itemList.get(currentPos);
                p.setSoluongmua(quantity);
                ProductAdapter2.this.notifyDataSetChanged();
                popupWindow.dismiss();


            }
        });

    }

}
