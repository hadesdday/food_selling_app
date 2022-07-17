package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
    Button btnOrderedBill, btnFinishedBill, btnCancledBill,btnback;
    final String URL="http://192.168.1.7:82/WebService.asmx";
//    final String URL="https://localhost:44364/WebServiceProject.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        btnOrderedBill = (Button) findViewById(R.id.btnOrderedBill);
        listViewBill = (ListView) findViewById(R.id.billListView);
        btnback=(Button) findViewById(R.id.btnBackBillList) ;
//        Bill b1 = new Bill(1, "1/1/2022");
//        ArrayList<Product> p1 = new ArrayList<>();
//        p1.add(new Product("sp1", 10000, 1, "a"));
//        p1.add(new Product("sp2", 15000, 2, "a"));
//        p1.add(new Product("sp4", 20000, 3, "a"));
//        b1.setProducts(p1);
//        bills.add(b1);
//
//        Bill b2 = new Bill(2, "2/1/2022");
//        ArrayList<Product> p2 = new ArrayList<>();
//        p2.add(new Product("sp1", 10000, 1, "a"));
//        p2.add(new Product("sp3", 25000, 2, "a"));
//        p2.add(new Product("sp4", 20000, 3, "a"));
//        b2.setProducts(p2);
//        bills.add(b2);
//
//        Bill b3 = new Bill(3, "1/1/2022");
//        ArrayList<Product> p3 = new ArrayList<>();
//        p3.add(new Product("sp1", 10000, 1, "a"));
//        p3.add(new Product("sp4", 20000, 2, "a"));
//        p3.add(new Product("sp8", 60000, 3, "a"));
//        b3.setProducts(p3);
//        bills.add(b3);
        StrictMode.ThreadPolicy policy=new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        billAdapter = new BillAdapter(this, R.layout.activity_item_bill, bills);
        listViewBill.setAdapter(billAdapter);
        billAdapter.notifyDataSetChanged();
        doGetList();
        btnOrderedBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        listViewBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListBill.this, BillDetails.class);
                Bundle bundle = new Bundle();
                Bill bill = (Bill) adapterView.getItemAtPosition(i);
//                Log.i("TAG", "doGetListItems: "+bill.getProducts().size());
//                bundle.putParcelableArrayList("products", bill.getProducts());
                bundle.putInt("mahd", bill.getMahoadon());
                bundle.putString("sdt", bill.getSodienthoai());
                bundle.putString("address", bill.getDiachi());

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
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("TAG", "onResume: ");
        if(this.reloadNeed){
            bills=new ArrayList<>();
            billAdapter = new BillAdapter(this, R.layout.activity_item_bill, bills);
            listViewBill.setAdapter(billAdapter);
            doGetList();
            Log.i("TAG", "onResume: reload");
        }
//        this.reloadNeed = false;
    }
    public void doGetList() {
        try{
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE="http://localhost/";
            final String METHOD_NAME="getBillList";
            final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
            SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("trangthai",1);
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            MarshalFloat marshal=new MarshalFloat();
            marshal.register(envelope);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION,envelope);
            Log.i("TAG", "doGetList: run4");
            SoapObject soapArray=(SoapObject) envelope.getResponse();
            Log.i("TAG", "doGetList: run5");
            Log.i("TAG", "doGetList: "+soapArray.getPropertyCount());
            bills.clear();
            for(int i=0;i<soapArray.getPropertyCount();i++){
                Log.i("TAG", "doGetList: add");
                SoapObject soapItem=(SoapObject) soapArray.getProperty(i);
                String mahd=soapItem.getProperty("mahoadon").toString();
                String dateBill=soapItem.getProperty("dateBill").toString();
                String sodienthoai=soapItem.getProperty("sodienthoai").toString();
                String diachi=soapItem.getProperty("diachi").toString();
                double tonghoadon=Double.parseDouble(soapItem.getProperty("tonghoadon").toString());
                Bill bill= new Bill(Integer.parseInt(mahd),dateBill,sodienthoai,diachi);
                bills.add(bill);
            }
            billAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.i("TAG", "doGetList: "+e.toString());
        }
    }
}