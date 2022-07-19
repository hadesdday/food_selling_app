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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context = null;
    ArrayList<Product> itemList = null;
    int layoutID;
    final String URL = "http://192.168.1.8:82/WebService.asmx";
    int currentPos = 0;

    public ProductAdapter(@NonNull Activity context, int layoutID, @NonNull ArrayList<Product> itemList) {
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
                        doDeleteItem(Integer.parseInt(txtmahd.getText().toString()), itemList.get(pos).getMasp());
                        itemList.remove(pos);
                        ProductAdapter.this.notifyDataSetChanged();
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
                currentPos=pos;
                Log.i("TAG", "mahd: " + txtmahd.getText().toString());
                Log.i("TAG", "masp: " + itemList.get(pos).getMasp());
                onChangeAmountPopup(view, Integer.parseInt(txtmahd.getText().toString()), itemList.get(pos).getMasp());


            }
        });
        return convertView;

    }

    public void doDeleteItem(int mahd, int masp) {
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "deleteIteminBill";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"Product");
            request.addProperty("mahd", mahd);
            request.addProperty("masp", masp);
//            request.addSoapObject(newProduct);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            int ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";
            if (ret <= 0)
                msg = "false";

            Log.i("TAG", "doGetList: run5:" + msg);


        } catch (Exception e) {
            Log.i("TAG", "error: " + e.toString());
        }
    }

    public void onChangeAmountPopup(View view, int mahd, int masp) {

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
                if (doChangeQuantity(mahd, masp, quantity)) {
                    Product p=itemList.get(currentPos);
                    p.setSoluongmua(quantity);
                    ProductAdapter.this.notifyDataSetChanged();
                    popupWindow.dismiss();
                }


            }
        });

    }

    public boolean doChangeQuantity(int mahd, int masp, int quantity) {
        boolean result = false;
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "changeQuantityProduct";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"Product");
            request.addProperty("mahd", mahd);
            request.addProperty("masp", masp);
            request.addProperty("soluong", quantity);
            Log.i("TAG", "onClick: quantity2: " + quantity);
//            request.addSoapObject(newProduct);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            int ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";
            if (ret <= 0) {
                msg = "false";
                result = false;
            } else {
                result = true;
            }

            Log.i("TAG", "doGetList: run5:" + msg);


        } catch (Exception e) {
            Log.i("TAG", "error: " + e.toString());
        }
        return result;
    }
}

