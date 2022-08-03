package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class BillDetails2 extends AppCompatActivity {
    ArrayList<ProductDomain> products = new ArrayList<>();
    ArrayList<Product> productsClone = new ArrayList<>();
    ListView listItems;
    ProductAdapter3 productAdapter = null;
    TextView sdt, mahd, address;
    EditText editBillid;
    Button btnBillid;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details2);
        URL = getResources().getString(R.string.URL);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sdt = (TextView) findViewById(R.id.textPhone_details2);
        mahd = (TextView) findViewById(R.id.textMahd_details2);
        address = (TextView) findViewById(R.id.textAddress_details2);
        editBillid = (EditText) findViewById(R.id.editInsertBillid);
        listItems = (ListView) findViewById(R.id.listItems2);
        btnBillid = (Button) findViewById(R.id.btnInsertBillid);
        productAdapter = new ProductAdapter3(this, R.layout.activity_item_product2, products);
        listItems.setAdapter(productAdapter);
        btnBillid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                doGetBillDetail(Integer.parseInt(editBillid.getText().toString()));
                new GetBillDetailWebservice().execute();
            }
        });
    }
//    public void doGetBillDetail(int billID) {
//
//    }

    public void doGetList(int idBill) {
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "getProductInBill";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("idBill", idBill);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            MarshalFloat marshal = new MarshalFloat();
            marshal.register(envelope);
            Log.i("TAG", "doGetListItems: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetListItems: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetListItems: run4");
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            Log.i("TAG", "doGetListItems: run5");
            Log.i("TAG", "doGetList: " + soapArray.getPropertyCount());
            products.clear();
            for (int i = 0; i < soapArray.getPropertyCount(); i++) {
                Log.i("TAG", "doGetList: add");
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                int idProduct = Integer.parseInt(soapItem.getProperty("idProduct").toString());
                Log.i("TAG", "get masp: " + idProduct);
                String nameProduct = soapItem.getProperty("nameProduct").toString();
                double priceProduct = Double.parseDouble(soapItem.getProperty("priceProduct").toString());
                int amount = Integer.parseInt(soapItem.getProperty("amount").toString());
                ProductDomain p = new ProductDomain(idProduct, nameProduct, "", "", (int) priceProduct, 0);
                p.setNumberInCart(amount);
                products.add(p);
            }
            productAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i("TAG", "doGetListItems: " + e.toString());
        }
    }

    private class GetBillDetailWebservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(BillDetails2.this);
        boolean result = false;

        public void doGetList(int idBill) {
            try {
                Log.i("TAG", "doGetList: run1");
                final String NAMESPACE = "http://localhost/";
                final String METHOD_NAME = "getProductInBill";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("idBill", idBill);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                MarshalFloat marshal = new MarshalFloat();
                marshal.register(envelope);
                Log.i("TAG", "doGetListItems: run2");
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Log.i("TAG", "doGetListItems: run3");
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Log.i("TAG", "doGetListItems: run4");
                SoapObject soapArray = (SoapObject) envelope.getResponse();
                Log.i("TAG", "doGetListItems: run5");
                Log.i("TAG", "doGetList: " + soapArray.getPropertyCount());
                products.clear();
                for (int i = 0; i < soapArray.getPropertyCount(); i++) {
                    Log.i("TAG", "doGetList: add");
                    SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                    int idProduct = Integer.parseInt(soapItem.getProperty("idProduct").toString());
                    Log.i("TAG", "get masp: " + idProduct);
                    String nameProduct = soapItem.getProperty("nameProduct").toString();
                    double priceProduct = Double.parseDouble(soapItem.getProperty("priceProduct").toString());
                    int amount = Integer.parseInt(soapItem.getProperty("amount").toString());
                    ProductDomain p = new ProductDomain(idProduct, nameProduct, "", "", (int) priceProduct, 0);
                    p.setNumberInCart(amount);
                    products.add(p);
                }
                productAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.i("TAG", "doGetListItems: " + e.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.i("TAG", "doGetList: run1");
                final String NAMESPACE = "http://localhost/";
                final String METHOD_NAME = "getBill";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("billID", editBillid.getText().toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                MarshalFloat marshal = new MarshalFloat();
                marshal.register(envelope);
                Log.i("TAG", "doGetListItems: run2");
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Log.i("TAG", "doGetListItems: run3");
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Log.i("TAG", "doGetListItems: run4");
                SoapObject soapArray = (SoapObject) envelope.getResponse();
                Log.i("TAG", "doGetListItems: run5");
                Log.i("TAG", "doGetList: " + soapArray.getPropertyCount());


                Log.i("TAG", "doGetList: add");
                if (soapArray.getPropertyCount() > 0) {
                    SoapObject soapItem = (SoapObject) soapArray.getProperty(0);
                    String dateBill = soapItem.getProperty("dateBill").toString();
                    String idBill = soapItem.getProperty("idBill").toString();
                    String phoneNumber = soapItem.getProperty("phoneNumber").toString();
                    int billPrice = Integer.parseInt(soapItem.getProperty("billPrice").toString());
                    String addressS = soapItem.getProperty("address").toString();
                    address.setText(addressS);
                    sdt.setText(phoneNumber);
                    mahd.setText(idBill);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doGetList(Integer.parseInt(idBill));
                        }
                    });
                } else {
                    Toast.makeText(BillDetails2.this, "Hóa đơn không tồn tại", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.i("TAG", "doGetListItems: " + e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}