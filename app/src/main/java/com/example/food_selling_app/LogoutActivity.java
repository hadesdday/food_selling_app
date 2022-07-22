package com.example.food_selling_app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobile.LoginActivity;
import com.example.mobile.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LogoutActivity extends Activity {
    Button btnLogout, btnInfo;
    SoapObject response;

    private static final String URL = "http://192.168.1.2:44341/WebServiceMobile.asmx?WSDL";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "logout";
    private static final String SOAP_ACTION = "http://tempuri.org/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new webservice().execute();
            }
        });
    }

    private class webservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(LogoutActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION, envelope);

                response = (SoapObject) envelope.getResponse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            Intent in = new Intent(LogoutActivity.this, LoginActivity.class);
            if(response != null) {
                startActivity(in);
            }else {
                Toast.makeText(LogoutActivity.this, "Đăng xuất thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }
}