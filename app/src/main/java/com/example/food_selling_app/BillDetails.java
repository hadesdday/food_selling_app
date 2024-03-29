package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class BillDetails extends Activity {
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> productsClone = new ArrayList<>();
    ListView listItems;
    ProductAdapter productAdapter = null;
    TextView sdt, mahd, address,price,rate;
    Button deletebtn,backbtn;
    Bundle bundle;

    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        URL=getResources().getString(R.string.URL);
        sdt = (TextView) findViewById(R.id.textPhone_details);
        mahd = (TextView) findViewById(R.id.textMahd_details);
        address = (TextView) findViewById(R.id.textAddress_details);
        rate=(TextView) findViewById(R.id.textRate_details);
        price= (TextView) findViewById(R.id.textPrice_details);
        listItems = (ListView) findViewById(R.id.listItems);
        deletebtn=(Button) findViewById(R.id.btnDelete_BillDetails);
        backbtn=(Button) findViewById(R.id.btnBackCheckout);

        bundle = getIntent().getExtras();
//        ArrayList<Product> products = bundle.getParcelableArrayList("products");
//        Log.i("TAG", "doGetListItems: "+products.size());
        mahd.setText(bundle.getInt("mahd")+"");
        sdt.setText(bundle.getString("sdt"));
        address.setText(bundle.getString("address"));
        rate.setText(bundle.getDouble("rate")+"");
        price.setText(bundle.getDouble("price")+"");
//        doGetList(bundle.getInt("mahd"));
        new GetBillWebservice().execute();
        productAdapter = new ProductAdapter(this, R.layout.activity_item_product, products, new UpdateBill() {
            @Override
            public void update() {
                double sum=0;
                for(Product p:products){
                    sum+=p.getTongGia();
                }
                sum*=(1-Double.parseDouble(rate.getText().toString()));
                updateBillPrice(Integer.parseInt(mahd.getText().toString()),sum);

                price.setText(sum+"");
            }
        });
        listItems.setAdapter(productAdapter);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BillDetails.this);
                AlertDialog dialog;
                builder.setTitle("Are you sure delete this item?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        doDeleteBill(bundle.getInt("mahd"));
                        new DeleteBillWebservice().execute();
                        finish();
                    }
                });
                builder.setNegativeButton("no", null);
                dialog = builder.create();
                dialog.show();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public boolean updateBillPrice(int billId,double price){
        boolean result = false;
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "updateBillPrice";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"Product");
            request.addProperty("billId", billId);
            request.addProperty("price", price);

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
    private class DeleteBillWebservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        boolean result = false;
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
                final String METHOD_NAME = "deleteBill";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"Product");
                request.addProperty("billID", bundle.getInt("mahd"));
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

            return null;
        }
        protected void onPostExecute(Void v) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    private class GetBillWebservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(BillDetails.this);
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
                    int amount=Integer.parseInt(soapItem.getProperty("amount").toString());
                    products.add(new Product(idProduct,nameProduct,priceProduct,amount));
                    productsClone.add(new Product(idProduct,nameProduct,priceProduct,amount));

                }

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
