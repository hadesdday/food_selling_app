package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class BillDetails extends AppCompatActivity {
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> productsClone = new ArrayList<>();
    ListView listItems;
    ProductAdapter productAdapter = null;
    TextView sdt, mahd, address;
    Button savebtn;
    Bundle bundle;
    final String URL="http://192.168.1.5:82/WebService.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        sdt = (TextView) findViewById(R.id.textPhone_details);
        mahd = (TextView) findViewById(R.id.textMahd_details);
        address = (TextView) findViewById(R.id.textAddress_details);
        listItems = (ListView) findViewById(R.id.listItems);
        savebtn=(Button) findViewById(R.id.btnSave_BillDetails);
        bundle = getIntent().getExtras();
//        ArrayList<Product> products = bundle.getParcelableArrayList("products");
//        Log.i("TAG", "doGetListItems: "+products.size());
        mahd.setText(bundle.getInt("mahd")+"");
        sdt.setText(bundle.getString("sdt"));
        address.setText(bundle.getString("address"));
        doGetList(bundle.getInt("mahd"));
        productAdapter = new ProductAdapter(this, R.layout.activity_item_product, products);
        listItems.setAdapter(productAdapter);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                int masp= Integer.parseInt(soapItem.getProperty("masp").toString());
                String tensp=soapItem.getProperty("tensp").toString();
                double gia= Double.parseDouble(soapItem.getProperty("gia").toString());
                int soluongmua=Integer.parseInt(soapItem.getProperty("soluongmua").toString());
                products.add(new Product(masp,tensp,gia,soluongmua));
                productsClone.add(new Product(masp,tensp,gia,soluongmua));
            }
            productAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.i("TAG", "doGetListItems: "+e.toString());
        }
    }

    public void saveChange(){

    }
}