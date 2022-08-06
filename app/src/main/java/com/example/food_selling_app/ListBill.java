package com.example.food_selling_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ListBill extends Activity {
    ArrayList<Bill> bills = new ArrayList<>();
    private boolean reloadNeed = true;
    BillAdapter billAdapter = null;
    ListView listViewBill = null;
    ListView listViewBill2 = null;
    Button btnOrderedBill, btnFinishedBill, btnCancledBill, btnback;
    String URL;
    Intent intent;
    BottomNavigationView bottomNavigation;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static final String Username = "usernameKey";
    String accountName;

    //    final String URL="https://localhost:44364/WebServiceProject.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        URL = getResources().getString(R.string.URL);

        initBottomNav();
        btnOrderedBill = (Button) findViewById(R.id.btnOrderedBill);

        listViewBill = (ListView) findViewById(R.id.billListView);

        btnback = (Button) findViewById(R.id.btnBackCheckout);
        btnFinishedBill = (Button) findViewById(R.id.btnFinishedBill);
        btnCancledBill = (Button) findViewById(R.id.btnCancledBill);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        billAdapter = new BillAdapter(this, R.layout.activity_item_bill, bills);
        intent = new Intent(ListBill.this, BillDetails.class);
        listViewBill.setAdapter(billAdapter);
        billAdapter.notifyDataSetChanged();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        accountName = sharedpreferences.getString(Username, "");
        Log.i("TAG", "onCreate: accountName:" + accountName);
//        doGetList(1, "clientt");
        new GetBillListWebservice().execute("1", accountName);
        btnOrderedBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListBill.this, BillDetails.class);
                billAdapter = new BillAdapter(ListBill.this, R.layout.activity_item_bill, bills);
                listViewBill.setAdapter(billAdapter);

                new GetBillListWebservice().execute("1", accountName);
                billAdapter.notifyDataSetChanged();
            }
        });
        btnFinishedBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListBill.this, BillDetails3.class);
                billAdapter = new BillAdapter(ListBill.this, R.layout.activity_item_bill, bills);
                listViewBill.setAdapter(billAdapter);

                new GetBillListWebservice().execute("2", accountName);
                billAdapter.notifyDataSetChanged();
            }
        });
        btnCancledBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListBill.this, BillDetails3.class);
                billAdapter = new BillAdapter(ListBill.this, R.layout.activity_item_bill, bills);
                listViewBill.setAdapter(billAdapter);

                new GetBillListWebservice().execute("3", accountName);
                billAdapter.notifyDataSetChanged();
            }
        });
        listViewBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                Bill bill = (Bill) adapterView.getItemAtPosition(i);
//                Log.i("TAG", "doGetListItems: "+bill.getProducts().size());
//                bundle.putParcelableArrayList("products", bill.getProducts());
                bundle.putInt("mahd", bill.getBillId());
                bundle.putString("sdt", bill.getPhoneNumber());
                bundle.putString("address", bill.getAddress());
                bundle.putDouble("price", bill.getPrice());
                bundle.putDouble("rate", bill.getRate());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initBottomNav() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.bill);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(ListBill.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.otherFunction:
                    intent = new Intent(ListBill.this, OtherFunction.class);
                    startActivity(intent);
                    break;
                case R.id.order:
                    intent = new Intent(ListBill.this, MainActivity2.class);
                    startActivity(intent);
                    break;
                case R.id.bill:
                    break;
            }
            return true;
        });
    }

    public void doGetList(int status, String username) {
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "getBillList";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("status", status);
            request.addProperty("username", username);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            MarshalFloat marshal = new MarshalFloat();
            marshal.register(envelope);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetList: run4");
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            Log.i("TAG", "doGetList: run5");
            Log.i("TAG", "doGetList: " + soapArray.getPropertyCount());
            bills.clear();
            for (int i = 0; i < soapArray.getPropertyCount(); i++) {
                Log.i("TAG", "doGetList: add");
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                String idBill = soapItem.getProperty("idBill").toString();
                String dateBill = soapItem.getProperty("dateBill").toString();
                String phoneNumber = soapItem.getProperty("phoneNumber").toString();
                String address = soapItem.getProperty("address").toString();
                double billPrice = Double.parseDouble(soapItem.getProperty("billPrice").toString());
                Bill bill = new Bill(Integer.parseInt(idBill), dateBill, phoneNumber, address, billPrice);

                bills.add(bill);
            }
            billAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i("TAG", "doGetList: " + e.toString());
        }
    }

    private class GetBillListWebservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ListBill.this);
        boolean result = false;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.i("TAG", "doGetList: run1");
                final String NAMESPACE = "http://localhost/";
                final String METHOD_NAME = "getBillList";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("status", Integer.parseInt(strings[0]));
                request.addProperty("username", strings[1]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                MarshalFloat marshal = new MarshalFloat();
                marshal.register(envelope);
                Log.i("TAG", "doGetList: run2");
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Log.i("TAG", "doGetList: run3");
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Log.i("TAG", "doGetList: run4");
                SoapObject soapArray = (SoapObject) envelope.getResponse();
                Log.i("TAG", "doGetList: run5");
                Log.i("TAG", "doGetList: " + soapArray.getPropertyCount());
                bills.clear();
                for (int i = 0; i < soapArray.getPropertyCount(); i++) {
                    Log.i("TAG", "doGetList: add");
                    SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                    String idBill = soapItem.getProperty("idBill").toString();
                    String dateBill = soapItem.getProperty("dateBill").toString();
                    String phoneNumber = soapItem.getProperty("phoneNumber").toString();
                    String address = soapItem.getProperty("address").toString();
                    String rate = soapItem.getProperty("rate").toString();
                    double billPrice = Double.parseDouble(soapItem.getProperty("billPrice").toString());
                    Bill bill = new Bill(Integer.parseInt(idBill), dateBill, phoneNumber, address, billPrice);
                    bill.setRate(Double.parseDouble(rate));
                    bills.add(bill);
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(1500);
                                billAdapter.notifyDataSetChanged();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                });
                billAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.i("TAG", "doGetList: " + e.toString());
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