package com.example.food_selling_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context = null;
    ArrayList<Product> itemList = null;
    int layoutID;
    final String URL="http://192.168.1.5:82/WebService.asmx";
    public ProductAdapter(@NonNull Activity context, int layoutID, @NonNull ArrayList<Product> itemList) {
        super(context, layoutID, itemList);
        this.layoutID = layoutID;
        this.context = context;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TextView txtmahd = (TextView)context.findViewById(R.id.textMahd_details);
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
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                AlertDialog dialog;
                builder.setTitle("Are you sure delete this item?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemList.remove(pos);
                        doDeleteItem(Integer.parseInt(txtmahd.getText().toString()),itemList.get(pos).getMasp());
                        ProductAdapter.this.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("no",null);
                dialog=builder.create();
                dialog.show();

            }
        });

        return convertView;

    }

    public void doDeleteItem(int mahd,int masp){
        try{
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE="http://localhost/";
            final String METHOD_NAME="deleteIteminBill";
            final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
            SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
            SoapObject newCate  =new SoapObject(NAMESPACE,"cate");
            request.addProperty("mahd",mahd);
            request.addProperty("masp",masp);
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION,envelope);
            SoapPrimitive soapPrimitive =(SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            int ret=Integer.parseInt(soapPrimitive.toString());
            String msg="success";
            if(ret<=0)
                msg="false";

            Log.i("TAG", "doGetList: run5:"+msg);


        }catch (Exception e){
            Log.i("TAG", "error: "+e.toString());
        }
    }

}

