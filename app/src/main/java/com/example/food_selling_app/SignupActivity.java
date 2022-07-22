package com.example.food_selling_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SignupActivity extends Activity {
    EditText edtUser, edtPass, edtEmail, edtConfPass;
    Button btnSignup;
    TextView txtLogin;
    SoapPrimitive response;
    SoapObject response2;

    private static final String URL = "http://192.168.1.2:44341/WebServiceMobile.asmx?WSDL";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "register";
    private static final String SOAP_ACTION = "http://tempuri.org/register";

    private static final String METHOD_NAME2 = "checkUser";
    private static final String SOAP_ACTION2 = "http://tempuri.org/checkUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtUser = findViewById(R.id.edtUserS);
        edtPass = findViewById(R.id.edtPassS);
        edtConfPass = findViewById(R.id.edtConfPassS);
        edtEmail = findViewById(R.id.edtEmailS);
        txtLogin = findViewById(R.id.txtLoginS);
        btnSignup = findViewById(R.id.btnSignup);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtPass.getText().toString().equals(edtConfPass.getText().toString())){
                    if(edtUser.getText().length() != 0 && edtUser.getText().toString() != ""){
                        if(edtPass.getText().length() != 0 && edtPass.getText().toString() != ""){
                            if(edtConfPass.getText().length() != 0 && edtConfPass.getText().toString() != ""){
                                if(edtEmail.getText().length() != 0 && edtEmail.getText().toString() != ""){
                                    new checkUser().execute();
                                }else {
                                    Toast.makeText(SignupActivity.this,"Vui lòng nhập địa chỉ Email", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(SignupActivity.this,"Vui lòng nhập xác nhân mật khẩu", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(SignupActivity.this,"Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SignupActivity.this,"Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(SignupActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class checkUser extends AsyncTask<String, Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME2);
                request.addProperty("username", edtUser.getText().toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION2, envelope);

                response2 = (SoapObject) envelope.getResponse();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(response2 != null) {
                Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
            }else {
                new Webservice().execute();
            }
        }
    }


    private class Webservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                request.addProperty("username", edtUser.getText().toString());
                request.addProperty("password", edtPass.getText().toString());
                request.addProperty("email", edtEmail.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION, envelope);

                response = (SoapPrimitive) envelope.getResponse();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }

            Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
            if(response != null) {
                Toast.makeText(SignupActivity.this, "Register Successful", Toast.LENGTH_SHORT).show() ;
                startActivity(myIntent);
            }else {
                Toast.makeText(SignupActivity.this, "Register Failed!", Toast.LENGTH_SHORT).show() ;
            }
            super.onPostExecute(result);
        }
    }
}