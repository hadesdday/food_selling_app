package com.example.food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String NAMESPACE;
    String URL;
    BottomNavigationView bottomNavigation;
    Intent intent;
    ViewFlipper viewFlipper;
    ArrayList<Food> foodArrayList;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Username = "usernameKey";
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NAMESPACE = getResources().getString(R.string.NAMESPACE);
        URL = getResources().getString(R.string.URL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        initBottomNav();
        new doFoodList().execute();


//        Button listProductCart = (Button) findViewById(R.id.productcart);
//        listProductCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ListProductCart.class);
//                startActivity(intent);
//            }
//        });

        FloatingActionButton floatingActionButton = findViewById(R.id.cartBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });
    }

    class doFoodList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private final String METHOD = "getFood";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
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
                foodArrayList = new ArrayList<>();

                for (int i = 0; i < foodSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodId = Integer.parseInt(item.getProperty("id").toString());
                    int foodTypeId = Integer.parseInt(item.getProperty("food_type").toString());
                    String foodName = item.getProperty("name").toString();
                    String foodImage = item.getProperty("image_url").toString();
                    String foodDescription = item.getProperty("description").toString();
                    int foodPrice = Integer.parseInt(item.getProperty("price").toString());
                    foodArrayList.add(new Food(foodId, foodName, foodImage, foodDescription, foodPrice, foodTypeId));
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
                Toast.makeText(MainActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                viewFlipper();
                exc = false;
            }
        }
    }

    private void viewFlipper() {
        viewFlipper = findViewById(R.id.viewflipper);
        ArrayList<String> imageList = new ArrayList<>();

        for (int i = foodArrayList.size() - 5; i < foodArrayList.size(); i++) {
            imageList.add(foodArrayList.get(i).getFoodImage());
        }

        for (int i = 0; i < imageList.size(); i++) {
            ImageView view = new ImageView(MainActivity.this);
            Picasso.with(getApplicationContext()).load(imageList.get(i)).into(view);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(view);
        }

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out));
    }



    public void initBottomNav(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    break;
                case R.id.otherFunction:
                    intent = new Intent(MainActivity.this, OtherFunction.class);
                    startActivity(intent);
                    break;
                case R.id.order:
                    intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    break;
                case R.id.bill:
                    if (sharedpreferences.getString(Username, "") != "") {
                        intent = new Intent(MainActivity.this, ListBill.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.this, BillDetails2.class);
                        startActivity(intent);
                    }
                    break;
            }
            return true;
        });
    }
}