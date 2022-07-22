package com.example.food_selling_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class ChangePasswordActivity extends Activity {
    EditText edtUsername, edtNewPass, edtConfirmPass;
    Button btnUpdatePass;
    SoapPrimitive response;
    //adwádsa
    private static final String URL = "http://192.168.1.2:44341/WebServiceMobile.asmx?WSDL";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "changePassword";
    private static final String SOAP_ACTION = "http://tempuri.org/changePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtUsername = findViewById(R.id.edtUsername);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewPass.getText().toString().equals(edtConfirmPass.getText().toString())) {
                    if (edtUsername.getText().length() != 0 && edtUsername.getText().toString() != "") {
                        if (edtNewPass.getText().length() != 0 && edtNewPass.getText().toString() != "") {
                            if (edtConfirmPass.getText().length() != 0 && edtConfirmPass.getText().toString() != "") {
                                new webservice().execute();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập xác nhận mật khẩu mới", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class webservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(ChangePasswordActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                request.addProperty("username", edtUsername.getText().toString());
                request.addProperty("newpassword", edtNewPass.getText().toString());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                httpTransportSE.call(SOAP_ACTION, envelope);

                response = (SoapPrimitive) envelope.getResponse();
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
            Intent in = new Intent(ChangePasswordActivity.this, UpdatePasswordSusscesfull.class);
            if(response != null){
                startActivity(in);
            }else {
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu không thành công", Toast.LENGTH_LONG).show();
            }
        }
    }
}