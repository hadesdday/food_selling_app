package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class ProfileActivity extends AppCompatActivity {
    EditText edtProUser, edtProPass, edtProEmail, edtProAddress, edtProPhone, edtProName;
    Button btnProUpdate;
    SoapPrimitive response;

//    private static final String URL = "http://192.168.1.2:44390/WebService.asmx?WSDL";
    private String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME1 = "updateMail";
    private static final String SOAP_ACTION1 = "http://tempuri.org/updateMail";

    private static final String METHOD_NAME2 = "updateName";
    private static final String SOAP_ACTION2 = "http://tempuri.org/updateName";

    private static final String METHOD_NAME3 = "updateAddress";
    private static final String SOAP_ACTION3 = "http://tempuri.org/updateAddress";

    private static final String METHOD_NAME4 = "updatePhoneNumber";
    private static final String SOAP_ACTION4 = "http://tempuri.org/updatePhoneNumber";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Username = "usernameKey";
    public static final String Password = "passKey";
    public static final String Email = "mailKey";
    public static final String Name = "nameKey";
    public static final String Address = "addressKey";
    public static final String Phone = "phoneKey";

    SharedPreferences sharedpreferences;
    String username, password, email, name, address, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        URL = getResources().getString(R.string.URL);

        edtProUser = findViewById(R.id.edtProUser);
        edtProPass = findViewById(R.id.edtProPass);
        edtProEmail = findViewById(R.id.edtProEmail);
        edtProAddress = findViewById(R.id.edtProAddress);
        edtProPhone = findViewById(R.id.edtProPhone);
        edtProName = findViewById(R.id.edtProName);
        btnProUpdate = findViewById(R.id.btnProUpdate);
        btnProUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateMail().execute();
            }
        });

        //get user
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString(Username, "");
        password = sharedpreferences.getString(Password, "");
        email = sharedpreferences.getString(Email, "");

        //get customer
        name = sharedpreferences.getString(Name, "");
        address = sharedpreferences.getString(Address, "");
        phone = sharedpreferences.getString(Phone, "");

        edtProUser.setText(username);
        edtProPass.setText(password);
        edtProEmail.setText(email);

        edtProName.setText(name);
        edtProAddress.setText(address);
        edtProPhone.setText(phone);
    }

    private class updateMail extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME1);
                request.addProperty("username", edtProUser.getText().toString());
                request.addProperty("newEmail", edtProEmail.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION1, envelope);

                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if(response != null) {
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                new updateName().execute();
            }else {
                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class updateName extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME2);
                request.addProperty("username", edtProUser.getText().toString());
                request.addProperty("newName", edtProName.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION2, envelope);

                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if(response != null) {
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                new updateAddress().execute();
            }else {
                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class updateAddress extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME3);
                request.addProperty("username", edtProUser.getText().toString());
                request.addProperty("newAddress", edtProAddress.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION3, envelope);

                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if(response != null) {
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                new updatePhoneNumber().execute();
            }else {
                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class updatePhoneNumber extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME4);
                request.addProperty("username", edtProUser.getText().toString());
                request.addProperty("newPhone", edtProPhone.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION4, envelope);

                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if(response != null) {
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}