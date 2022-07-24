package com.example.food_selling_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FilteredFood extends AppCompatActivity {
    private final String NAMESPACE = "http://192.168.0.177:44321/";
    private final String URL = "http://192.168.0.177/TheWebService/webservice.asmx?WSDL";

    FoodType foodType;
    TextView text;
    ArrayList<com.example.foodapp.Food> foodArrayListFiltered;
    com.example.foodapp.FoodAdapter foodAdapter;
    RecyclerView foodListFiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_food);

        Intent intent = getIntent();
        foodType = (FoodType) intent.getSerializableExtra("foodtype");

        text = findViewById(R.id.text);
        text.setText(foodType.getFoodTypeName());
        new doFoodListFiltered().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class doFoodListFiltered extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FilteredFood.this);
        private final String METHOD = "getFoodByFoodType";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodTypeId", foodType.getFoodTypeId());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);

            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodSize = soapObjectResponse.getPropertyCount();
                foodArrayListFiltered = new ArrayList<>();

                for (int i = 0; i < foodSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodId = Integer.parseInt(item.getProperty("FoodId").toString());
                    String foodName = item.getProperty("FoodName").toString();
                    String foodImage = item.getProperty("FoodImage").toString();
                    String foodDescription = item.getProperty("FoodDescription").toString();
                    int foodPrice = Integer.parseInt(item.getProperty("FoodPrice").toString());
                    int foodTypeId = Integer.parseInt(item.getProperty("FoodTypeId").toString());
                    foodArrayListFiltered.add(new com.example.foodapp.Food(foodId, foodName, foodImage, foodDescription, foodPrice, foodTypeId));
                }
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Đang tải dữ liệu...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(FilteredFood.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeRecyclerView();
                exc = false;
            }
        }
    }

    private void makeRecyclerView() {
        foodListFiltered = findViewById(R.id.foodListFiltered);
        foodAdapter = new com.example.foodapp.FoodAdapter(this, foodArrayListFiltered);
        foodListFiltered.setHasFixedSize(true);
        foodListFiltered.setLayoutManager(new GridLayoutManager(this, 2));
        foodListFiltered.setAdapter(foodAdapter);

        foodListFiltered.addOnItemTouchListener
                (new com.example.foodapp.RecyclerTouchListener(this, foodListFiltered, new com.example.foodapp.RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onTouch(View view, int position) {
                        Intent in = new Intent(FilteredFood.this, com.example.foodapp.FoodByFoodIdActivity.class);
                        in.putExtra("food", foodArrayListFiltered.get(position));
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }

                    @Override
                    public void onHold(View view, int position) {
                        DecimalFormat format = new DecimalFormat("###,###,###");
                        Toast.makeText(FilteredFood.this,
                                foodArrayListFiltered.get(position).getFoodName() +
                                        " - " + format.format(foodArrayListFiltered.get(position).getFoodPrice()) + "đ",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}