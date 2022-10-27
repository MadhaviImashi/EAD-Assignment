package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationOwnerUpdateActivity extends AppCompatActivity {
    private String station_id;
    private JSONObject fuelShed, diesel, petrol;
    private EditText d_arrival_time, d_arrived_qty, d_finishing_time, d_arrival_date;
    private EditText p_arrival_time, p_arrived_qty, p_finishing_time, p_arrival_date;
    private Button updateFuelDetailsBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_update);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");

        d_arrival_date = findViewById(R.id.etDieselArrivalDate);
        d_arrival_time = findViewById(R.id.etDieselArrivalTime);
        d_arrived_qty = findViewById(R.id.etDieselArrivalQuantity);
        d_finishing_time = findViewById(R.id.etDieselFinishedTime);

        p_arrival_date = findViewById(R.id.etPetrolArrivalDate);
        p_arrival_time = findViewById(R.id.etPetrolArrivalTime);
        p_arrived_qty = findViewById(R.id.etPetrolArrivalQuantity);
        p_finishing_time = findViewById(R.id.etPetrolFinishedTime);

        updateFuelDetailsBtn = findViewById(R.id.etEditFuelDetails);

        //display values from the previous state
        displayAlreadySavedFuelData();

        updateFuelDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAndSaveFuelDetails();
            }
        });
    }

    public void displayAlreadySavedFuelData() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/station-details";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Toast.makeText(StationOwnerUpdateActivity.this, "got fuel station details", Toast.LENGTH_SHORT).show();

                        //get fuel station details from http response
                        fuelShed = response.getJSONObject("fuelShed");
                        diesel = fuelShed.getJSONObject("Diesel"); //access response body
                        petrol = fuelShed.getJSONObject("Petrol"); //access response body
                        String var = diesel.getString("avaiableTotalFuelAmount");
                        Log.e("inside stationManager", "diesel-arrival time:  "+ var);

                        //display diesel details
                        d_arrival_date.setText(diesel.getString("arrivalDate"));
                        d_arrival_time.setText(diesel.getString("arrivalTime"));
                        d_arrived_qty.setText(String.valueOf(diesel.getInt("arrivedQuantity")));
                        d_finishing_time.setText(diesel.getString("finishingTime"));

                        //display petrol details
                        p_arrival_date.setText(petrol.getString("arrivalDate"));
                        p_arrival_time.setText(petrol.getString("arrivalTime"));
                        p_arrived_qty.setText(String.valueOf(petrol.getInt("arrivedQuantity")));
                        p_finishing_time.setText(petrol.getString("finishingTime"));

                        Toast.makeText(StationOwnerUpdateActivity.this, "fetched fuel details", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StationOwnerUpdateActivity.this, "couldn't fetch station details", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };
        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void updateAndSaveFuelDetails() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);
        params.put("diesel_arrival_date", d_arrival_date.getText().toString());
        params.put("diesel_arrival_time", d_arrival_time.getText().toString());
        params.put("diesel_arrived_quantity", d_arrived_qty.getText().toString());
        params.put("diesel_finishing_time", d_finishing_time.getText().toString());
        params.put("petrol_arrival_date", p_arrival_date.getText().toString());
        params.put("petrol_arrival_time", p_arrival_time.getText().toString());
        params.put("petrol_arrived_quantity", p_arrived_qty.getText().toString());
        params.put("petrol_finishing_time", p_finishing_time.getText().toString());

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/update";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Log.e("inside stationManager", "details updated ");
                        Toast.makeText(StationOwnerUpdateActivity.this, "fuel details updated", Toast.LENGTH_SHORT).show();

                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    sleep(1000);
                                    startActivity(new Intent(StationOwnerUpdateActivity.this, LoginActivity.class));
                                    finish();
                                }catch (Exception e) {

                                }
                            }
                        };
                        thread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StationOwnerUpdateActivity.this, "couldn't update station details", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };
        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}