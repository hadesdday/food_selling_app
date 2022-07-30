package com.example.food_selling_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class LoginActivity extends Activity {
    EditText edtUser, edtPass;
    TextView txtError, txtsignup, txtforgotpass;
    Button btnLogin;
    SoapObject response;

    private static final String URL = "http://192.168.1.2:44390/WebService.asmx?WSDL";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "getInfo";
    private static final String SOAP_ACTION = "http://tempuri.org/getInfo";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Username = "usernameKey";
    public static final String Password = "passKey";
    public static final String Email = "mailKey";
    public static final String Name = "nameKey";
    public static final String Address = "addressKey";
    public static final String Phone = "phoneKey";

    SharedPreferences sharedpreferences;
    String username, password, email, name, address, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        edtUser = findViewById(R.id.edtUserS);
        edtPass = findViewById(R.id.edtPassS);
        txtError = findViewById(R.id.txtError);
        txtsignup = findViewById(R.id.txtsignup);
        txtforgotpass = findViewById(R.id.txtforgotpass);
        btnLogin = findViewById(R.id.btnSignin);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        txtforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUser.getText().length() != 0 && edtUser.getText().toString() != ""){
                    if(edtPass.getText().length() != 0 && edtPass.getText().toString() != ""){
                        new Webservice().execute();
                    }else {
                        Toast.makeText(LoginActivity.this,"Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class Webservice extends AsyncTask<String, Void, User> {
        private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected User doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                request.addProperty("user", edtUser.getText().toString());
                request.addProperty("pass", edtPass.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION, envelope);

                response = (SoapObject) envelope.getResponse();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User v) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if(response != null) {

                for(int i = 0; i < response.getPropertyCount(); i++){
                    SoapObject object = (SoapObject) response.getProperty(i);
                    username = object.getProperty("username").toString();
                    password = object.getProperty("password").toString();
                    email = object.getProperty("email").toString();
                    name = object.getProperty("name").toString();
                    address = object.getProperty("address").toString();
                    phoneNumber = object.getProperty("phoneNumber").toString();
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Username, username);
                editor.putString(Password, password);
                editor.putString(Email, email);
                editor.putString(Name, name);
                editor.putString(Address, address);
                editor.putString(Phone, phoneNumber);
                editor.commit();

                Intent myIntent = new Intent(LoginActivity.this, LogoutActivity.class);
                startActivity(myIntent);
            }else {
                txtError.setVisibility(View.VISIBLE);
            }
        }
    }
}