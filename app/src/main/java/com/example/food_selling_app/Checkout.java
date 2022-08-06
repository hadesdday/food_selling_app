package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {
    EditText editName, editAddress, editPhone, editVoucher;
    ListView cartCheckout;
    Button btnCheckout, btnBack, btnVoucher;
    ArrayList<Food> products = new ArrayList<>();
    ProductAdapter3 productAdapter = null;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView txtTotalBill;

    String URL;
    double rate = 0.0;
    boolean hasVoucher = false;
    String usedVoucher = "";
    float total = 0;

    SharedPreferences sharedpreferences, sharedpreferences2;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Username = "usernameKey";
    public static final String Password = "passKey";
    public static final String Email = "mailKey";
    public static final String Name = "nameKey";
    public static final String Address = "addressKey";
    public static final String Phone = "phoneKey";
    public static final String MyPREFERENCES2 = "CartPrefs";
    public static final String FOODLIST = "foodListKey";
    public static final String TOTALLIST = "totalListKey";
    public boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        URL = getResources().getString(R.string.URL);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editName = (EditText) findViewById(R.id.editName_checkout);
        editAddress = (EditText) findViewById(R.id.editAddress_checkout);
        editPhone = (EditText) findViewById(R.id.editPhone_checkout);
        cartCheckout = (ListView) findViewById(R.id.listViewCart_checkout);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnBack = (Button) findViewById(R.id.btnBackCheckout);
        btnVoucher = (Button) findViewById(R.id.btnVoucher);
        txtTotalBill = (TextView) findViewById(R.id.txtTotalBill);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name = sharedpreferences.getString(Name, "");
        String address = sharedpreferences.getString(Address, "");
        String phone = sharedpreferences.getString(Phone, "");
        editName.setText(name);
        editAddress.setText(address);
        editPhone.setText(phone);
        Log.i("TAG", "onClick not login !!!!!!!!: " + (sharedpreferences.getString(Username, "") == ""));
        sharedpreferences2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
        List<Food> arrayItems;
        String serializedObject = sharedpreferences2.getString(FOODLIST, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Food>>() {
            }.getType();
            products = gson.fromJson(serializedObject, type);
        }
        total = sharedpreferences2.getFloat(TOTALLIST, 0);
        txtTotalBill.setText((total * (1 - rate)) + "");
//        products.add(new Product(1, "abc", 10000, 2));
//        products.add(new Product(2, "xyz", 50000, 3));
//        products.add(new Product(3, "asd", 100000, 1));
//        products.add(new Product(3, "asd", 100000, 1));
//        products.add(new Product(3, "asd", 100000, 2));
        productAdapter = new ProductAdapter3(this, R.layout.activity_item_product2, products);
        cartCheckout.setAdapter(productAdapter);
        ////

        LayoutInflater inflater = (LayoutInflater) Checkout.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_comfirm, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        ////

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                int totalBill = 0;
                String pttt = "COD";
                Log.i("TAG", "radiobutton:" + radioButton);
                if (editPhone.getText().toString() == "" || editAddress.getText().toString() == "" || radioButton == null) {

                    popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                    TextView showText = (TextView) popupView.findViewById(R.id.showText);
                    showText.setText("Bạn cần phải nhập đầy đủ thông tin!");
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    Button acceptChangePopup = (Button) popupView.findViewById(R.id.btnAccept);
                    acceptChangePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            return;
                        }
                    });
                } else {
                    CharSequence text = radioButton.getText();
                    if ("Chuyển khoản ngân hàng".equals(text)) {
                        pttt = "BANK";
                    } else if ("Thanh toán trực tiếp".equals(text)) {
                        pttt = "COD";
                    }
                    for (Food p : products) {
                        totalBill += p.getTotal();
                    }
                    totalBill = (int) (totalBill * (1 - rate));
                    Log.i("TAG", "onClick: insert bill:" + editName.getText().toString());
                    Log.i("TAG", "onClick: insert bill:" + editPhone.getText().toString());
                    Log.i("TAG", "onClick: insert bill:" + editAddress.getText().toString());
                    Log.i("TAG", "onClick: insert bill:" + pttt);
//                    int billID = doInsertBill(editName.getText().toString(), totalBill, editPhone.getText().toString(), editAddress.getText().toString(), pttt, sharedpreferences.getString(Username,""));
//
//                    for (Food p : products) {
//                        Log.i("TAG", "onClick: p.getFoodId():"+billID);
//                        Log.i("TAG", "onClick: p.getFoodId():"+p.getFoodId());
//                        Log.i("TAG", "onClick: p.getFoodId():"+p.getFoodName());
//                        Log.i("TAG", "onClick: p.getFoodId():"+p.getNumberInCart());
//                        Log.i("TAG", "onClick: p.getFoodId():"+p.getTotal());
//                        doInsertBillDetail(billID, p.getFoodId(), p.getNumberInCart());
//                    }
                    new Webservice().execute(new String[]{editName.getText().toString(), totalBill + "", editPhone.getText().toString(), editAddress.getText().toString(), pttt, sharedpreferences.getString(Username, ""), usedVoucher});
//                    usedVoucher(usedVoucher);

                    LayoutInflater inflater = (LayoutInflater) Checkout.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_comfirm, null);
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                    TextView showText = (TextView) popupView.findViewById(R.id.showText);
                    if (!isError) {
                        showText.setText("Đạt hàng thành công!");

                    } else {
                        showText.setText("Đạt hàng không thành công! Đã xảy ra lỗi!!");
                    }

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            if (!isError) {
                                SharedPreferences.Editor editor = sharedpreferences2.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(Checkout.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                isError = false;
                            }
                            return true;
                        }
                    });
                    Button acceptChangePopup = (Button) popupView.findViewById(R.id.btnAccept);
                    acceptChangePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (!isError) {
                                SharedPreferences.Editor editor = sharedpreferences2.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(Checkout.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                isError = false;
                            }
                            return;
                        }
                    });
                }
            }

        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasVoucher) {
                } else {
                    editVoucher = (EditText) findViewById(R.id.editVoucher);
                    String code = editVoucher.getText().toString();
                    if (code != "") {
                        rate = getRateDiscount(code) / 100;
                        if (rate == 0) {
                            popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                            TextView showText = (TextView) popupView.findViewById(R.id.showText);
                            showText.setText("voucher không tồn tại!");
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });
                            Button acceptChangePopup = (Button) popupView.findViewById(R.id.btnAccept);
                            acceptChangePopup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    return;
                                }
                            });
                        } else {
                            popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                            TextView showText = (TextView) popupView.findViewById(R.id.showText);
                            showText.setText("Nhập code thành công!");
                            txtTotalBill.setText((total * (1 - rate)) + "");
                            usedVoucher = code;
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });
                            Button acceptChangePopup = (Button) popupView.findViewById(R.id.btnAccept);
                            acceptChangePopup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    return;
                                }
                            });
                            hasVoucher = true;
                        }
                    } else {

                    }
                }
            }
        });
    }

    public int usedVoucher(String id_voucher) {
        int ret = 0;
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "usedVoucher";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id_voucher", id_voucher);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetList: run4");
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";


            Log.i("TAG", "doGetList: run5:" + ret);


        } catch (Exception e) {
            Log.i("TAG", "error: " + e.toString());
        }
        return ret;
    }

    public double getRateDiscount(String id_voucher) {
        int ret = 0;
        try {
            Log.i("TAG", "doGetList: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "getVoucher";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id_voucher", id_voucher);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetList: run4");
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";


            Log.i("TAG", "doGetList: run5:" + ret);


        } catch (Exception e) {
            Log.i("TAG", "error: " + e.toString());
        }
        return ret;
    }

    public int doInsertBill(String name, int billPrice, String phoneNumber, String address, String payment, String username, String voucher) {
        int ret = 0;
        try {
            Log.i("TAG", "doGetList insertBill: run1");
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "insertBill";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"b");
            request.addProperty("name", name);
            request.addProperty("billPrice", billPrice);
            request.addProperty("phoneNumber", phoneNumber);
            request.addProperty("address", address);
            request.addProperty("payment", payment);
            request.addProperty("username", username);
            request.addProperty("voucher", voucher);
//            request.addSoapObject(newProduct);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2");
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetList: run4");
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";


            Log.i("TAG", "doGetList: run5:" + ret);


        } catch (Exception e) {
            isError = true;
            Log.i("TAG", "error: " + e.toString());
        }
        return ret;
    }

    public int doInsertBillDetail(int billId, int foodId, int amount) {
        int ret = 0;
        try {
            Log.i("TAG", "doGetList add bill detail: run1" + billId);
            final String NAMESPACE = "http://localhost/";
            final String METHOD_NAME = "addBillDetail";
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapObject newProduct  =new SoapObject(NAMESPACE,"b");
            request.addProperty("billId", billId);
            request.addProperty("foodId", foodId);
            request.addProperty("amount", amount);
//            request.addSoapObject(newProduct);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("TAG", "doGetList: run2" + foodId);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("TAG", "doGetList: run3");
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.i("TAG", "doGetList: run4");
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            Log.i("TAG", "doGetList: run4");
            ret = Integer.parseInt(soapPrimitive.toString());
            String msg = "success";


            Log.i("TAG", "doGetList: run5:" + ret);


        } catch (Exception e) {
            isError = true;
            Log.i("TAG", "error: " + e.toString());
        }
        return ret;
    }

    private class Webservice extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(Checkout.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            int billID = doInsertBill(strings[0], Integer.parseInt(strings[1]), strings[2], strings[3], strings[4], strings[5], strings[6]);
            for (Food p : products) {
                Log.i("TAG", "onClick: p.getFoodId():" + billID);
                Log.i("TAG", "onClick: p.getFoodId():" + p.getFoodId());
                Log.i("TAG", "onClick: p.getFoodId():" + p.getFoodName());
                Log.i("TAG", "onClick: p.getFoodId():" + p.getNumber());
                Log.i("TAG", "onClick: p.getFoodId():" + p.getTotal());
                doInsertBillDetail(billID, p.getFoodId(), p.getNumber());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}