package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class BillDetails3 extends AppCompatActivity {
    ArrayList<Food> products = new ArrayList<>();
    ArrayList<Product> productsClone = new ArrayList<>();
    ListView listItems;
    ProductAdapter3 productAdapter = null;
    TextView sdt, mahd, address,price;
    Button backbtn;
    Bundle bundle;

    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details3);
        URL=getResources().getString(R.string.URL);
        sdt = (TextView) findViewById(R.id.textPhone_details3);
        mahd = (TextView) findViewById(R.id.textMahd_details3);
        address = (TextView) findViewById(R.id.textAddress_details3);
        price= (TextView) findViewById(R.id.textPrice_details);
        listItems = (ListView) findViewById(R.id.listItems3);
        backbtn=(Button) findViewById(R.id.btnBackCheckout3);
        bundle = getIntent().getExtras();
//        ArrayList<Product> products = bundle.getParcelableArrayList("products");
//        Log.i("TAG", "doGetListItems: "+products.size());
        mahd.setText(bundle.getInt("mahd")+"");
        sdt.setText(bundle.getString("sdt"));
        address.setText(bundle.getString("address"));
//        doGetList(bundle.getInt("mahd"));
        new GetBillWebservice().execute();
        productAdapter = new ProductAdapter3(this, R.layout.activity_item_product2, products);
        listItems.setAdapter(productAdapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void doGetList(int idBill) {
        try{
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE="http://localhost/";
            final String METHOD_NAME="getProductInBill";
            final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
            SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("idBill",idBill);
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            MarshalFloat marshal=new MarshalFloat();
            marshal.register(envelope);
            Log.i("TAG", "doGetListItems: run2");
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            Log.i("TAG", "doGetListItems: run3");
            androidHttpTransport.call(SOAP_ACTION,envelope);
            Log.i("TAG", "doGetListItems: run4");
            SoapObject soapArray=(SoapObject) envelope.getResponse();
            Log.i("TAG", "doGetListItems: run5");
            Log.i("TAG", "doGetList: "+soapArray.getPropertyCount());
            products.clear();
            for(int i=0;i<soapArray.getPropertyCount();i++){
                Log.i("TAG", "doGetList: add");
                SoapObject soapItem=(SoapObject) soapArray.getProperty(i);
                int idProduct= Integer.parseInt(soapItem.getProperty("idProduct").toString());
                Log.i("TAG", "get masp: "+idProduct);
                String nameProduct=soapItem.getProperty("nameProduct").toString();
                double priceProduct= Double.parseDouble(soapItem.getProperty("priceProduct").toString());
                int amount=Integer.parseInt(soapItem.getProperty("amount").toString());
                Food p = new Food(idProduct, nameProduct, "", "", (int) priceProduct, 0,amount);
                products.add(p);
            }
            productAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.i("TAG", "doGetListItems: "+e.toString());
        }
    }
    private class GetBillWebservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(BillDetails3.this);
        boolean result = false;
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            try{
                Log.i("TAG", "doGetList: run1");
                final String NAMESPACE="http://localhost/";
                final String METHOD_NAME="getProductInBill";
                final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
                SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
                request.addProperty("idBill",bundle.getInt("mahd"));
                SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);
                MarshalFloat marshal=new MarshalFloat();
                marshal.register(envelope);
                Log.i("TAG", "doGetListItems: run2");
                HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
                Log.i("TAG", "doGetListItems: run3");
                androidHttpTransport.call(SOAP_ACTION,envelope);
                Log.i("TAG", "doGetListItems: run4");
                SoapObject soapArray=(SoapObject) envelope.getResponse();
                Log.i("TAG", "doGetListItems: run5");
                Log.i("TAG", "doGetList: "+soapArray.getPropertyCount());
                products.clear();
                double priceBill=0;
                for(int i=0;i<soapArray.getPropertyCount();i++){
                    Log.i("TAG", "doGetList: add");
                    SoapObject soapItem=(SoapObject) soapArray.getProperty(i);
                    int idProduct= Integer.parseInt(soapItem.getProperty("idProduct").toString());
                    Log.i("TAG", "get masp: "+idProduct);
                    String nameProduct=soapItem.getProperty("nameProduct").toString();
                    double priceProduct= Double.parseDouble(soapItem.getProperty("priceProduct").toString());
                    priceBill+=priceProduct;
                    int amount=Integer.parseInt(soapItem.getProperty("amount").toString());
                    Food p = new Food(idProduct, nameProduct, "", "", (int) priceProduct, 0,amount);
                    products.add(p);

                }
                price.setText(bundle.getDouble("price")+"");
                productAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.i("TAG", "doGetListItems: "+e.toString());
            }

            return null;
        }
        protected void onPostExecute(Void v) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}