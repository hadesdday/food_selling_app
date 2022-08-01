package com.example.food_selling_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MainActivity2 extends AppCompatActivity {
    private final String NAMESPACE = getResources().getString(R.string.NAMESPACE1);
    private final String URL = getResources().getString(R.string.URL1);

    Spinner foodTypeList;
    ArrayList<FoodType> foodTypeArrayList;
    ArrayList<Food> foodArrayList;
    FoodAdapter foodAdapter;
    RecyclerView foodList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new doFoodTypeList().execute();
        new doFoodList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem cart = menu.findItem(R.id.cart);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Tìm kiếm món ăn");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(MainActivity2.this, FoodSearchActivity.class);
                intent.putExtra("query", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        return true;
    }

    class doFoodTypeList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(MainActivity2.this);
        private final String METHOD = "getFoodType";
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
                int foodTypeSize = soapObjectResponse.getPropertyCount();
                foodTypeArrayList = new ArrayList<>();
                foodTypeArrayList.add(new FoodType(0, "Tất cả"));

                for (int i = 0; i < foodTypeSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodTypeId = Integer.parseInt(item.getProperty("FoodTypeId").toString());
                    String foodTypeName = item.getProperty("FoodTypeName").toString();
                    foodTypeArrayList.add(new FoodType(foodTypeId, foodTypeName));
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
                Toast.makeText(MainActivity2.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeSpinner();
                exc = false;
            }
        }
    }

    class doFoodList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(MainActivity2.this);
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
                    int foodId = Integer.parseInt(item.getProperty("FoodId").toString());
                    String foodName = item.getProperty("FoodName").toString();
                    String foodImage = item.getProperty("FoodImage").toString();
                    String foodDescription = item.getProperty("FoodDescription").toString();
                    int foodPrice = Integer.parseInt(item.getProperty("FoodPrice").toString());
                    int foodTypeId = Integer.parseInt(item.getProperty("FoodTypeId").toString());
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
                Toast.makeText(MainActivity2.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeRecyclerView();
                exc = false;
            }
        }
    }

    private void makeRecyclerView() {
        foodList = findViewById(R.id.foodList);
        foodAdapter = new FoodAdapter(this, foodArrayList);
        foodList.setHasFixedSize(true);
        foodList.setLayoutManager(new GridLayoutManager(this, 2));
        foodList.setAdapter(foodAdapter);

        foodList.addOnItemTouchListener
                (new RecyclerTouchListener(this, foodList, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onTouch(View view, int position) {
                        Intent in = new Intent(MainActivity2.this, FoodByFoodIdActivity.class);
                        in.putExtra("food", foodArrayList.get(position));
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }

                    @Override
                    public void onHold(View view, int position) {
                        DecimalFormat format = new DecimalFormat("###,###,###");
                        Toast.makeText(MainActivity2.this,
                                foodArrayList.get(position).getFoodName() +
                                        " - " + format.format(foodArrayList.get(position).getFoodPrice()) + "đ",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void makeSpinner() {
        foodTypeList = findViewById(R.id.foodTypeList);
        ArrayAdapter<FoodType> foodTypeAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, foodTypeArrayList);
        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodTypeList.setAdapter(foodTypeAdapter);

        foodTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Intent intent = new Intent(MainActivity2.this, FilteredFood.class);
                    FoodType item = foodTypeAdapter.getItem(i);
                    intent.putExtra("foodtype", item);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}