package com.example.mobile;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MoneyActivity extends Activity {
    EditText edtsl, edtdg, edttt;
    Button btntt;
    int sl, dg;
    SoapPrimitive response;

    private static final String URL = "http://192.168.1.4:44341/WebServiceMobile.asmx?WSDL";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "money";
    private static final String SOAP_ACTION = "http://tempuri.org/money";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        edtsl = findViewById(R.id.edtsoluong);
        edtdg = findViewById(R.id.edtdongia);
        edttt = findViewById(R.id.edtthanhtien);
        btntt = findViewById(R.id.btntinhtien);
        btntt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });
    }

    private void postData() {
        new Webservice().execute();
    }

    private class Webservice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                sl = Integer.parseInt(edtsl.getText().toString());
                dg = Integer.parseInt(edtdg.getText().toString());
                request.addProperty("quantity", sl);
                request.addProperty("unitPrice", dg);

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                MarshalFloat marshalFloat = new MarshalFloat();
                marshalFloat.register(envelope);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e) {

            }
             return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(response != null) {
                edttt.setText(response.toString());
            }else {
                Toast.makeText(MoneyActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}