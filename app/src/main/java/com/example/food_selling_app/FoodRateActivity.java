package com.example.food_selling_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class FoodRateActivity extends AppCompatActivity {
    private final String NAMESPACE = "http://192.168.0.177:44321/";
    private final String URL = "http://192.168.0.177/TheWebService/webservice.asmx?WSDL";

    RatingBar rate;
    EditText comment;
    Button send;
    ArrayList<FoodRating> foodRatingArrayList;
    RecyclerView rateList;
    FoodRateAdapter adapter;
    int foodId;
    float foodrate;
    String foodcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rate);
        rate = findViewById(R.id.foodrate);
        comment = findViewById(R.id.foodcomment);
        send = findViewById(R.id.send);

        Intent intent = getIntent();
        foodId = intent.getIntExtra("foodid", 0);

        new doFoodRate().execute();

        send.setOnClickListener(view -> {
            if (rate.getRating() == 0 || comment.getText().length() == 0)
                Toast.makeText(this, "Oops!", Toast.LENGTH_SHORT).show();
            else {
                foodrate = rate.getRating();
                foodcomment = comment.getText().toString();
                new doRate().execute();
            }
            new doFoodRate().execute();
        });
    }

    class doRate extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodRateActivity.this);
        private final String METHOD = "addFoodRating";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodId", foodId);
            soapObjectRequest.addProperty("foodRate", foodrate);
            soapObjectRequest.addProperty("foodComment", foodcomment);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            SoapObject soapObjectResponse = (SoapObject) envelope.bodyIn;
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("addFoodRatingResult");
            return primitive.toString();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Đang tải dữ liệu...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(FoodRateActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(FoodRateActivity.this, "Tải dữ liệu thành công. :)", Toast.LENGTH_SHORT).show();
                exc = false;
            }
        }
    }

    class doFoodRate extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodRateActivity.this);
        private final String METHOD = "getFoodRateByFoodId";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodId", foodId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodRateSize = soapObjectResponse.getPropertyCount();
                foodRatingArrayList = new ArrayList<>();

                for (int i = 0; i < foodRateSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int rateId = Integer.parseInt(item.getProperty("RateId").toString());
                    int foodId = Integer.parseInt(item.getProperty("FoodId").toString());
                    float foodRate = Float.parseFloat(item.getProperty("FoodRate").toString());
                    String foodComment = item.getProperty("FoodComment").toString();
                    foodRatingArrayList.add(new FoodRating(rateId, foodId, foodRate, foodComment));
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
                Toast.makeText(FoodRateActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeRecyclerView();
                exc = false;
            }
        }
    }

    private void makeRecyclerView() {
        rateList = findViewById(R.id.ratelist);
        adapter = new FoodRateAdapter(this, foodRatingArrayList);
        rateList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rateList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rateList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}